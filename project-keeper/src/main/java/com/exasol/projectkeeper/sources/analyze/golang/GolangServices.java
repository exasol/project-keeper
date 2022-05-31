package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.Collections.emptyMap;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.FixedVersion;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.VersionConfig;
import com.exasol.projectkeeper.shared.dependencychanges.*;
import com.exasol.projectkeeper.shared.repository.GitRepository;
import com.exasol.projectkeeper.shared.repository.TaggedCommit;
import com.exasol.projectkeeper.sources.analyze.golang.GoModFile.GoModDependency;
import com.exasol.projectkeeper.sources.analyze.golang.ModuleInfo.Dependency;

/**
 * This class provides methods for retrieving information about a Golang project, e.g. its module name, dependencies and
 * dependency changes.
 */
class GolangServices {
    public static final String GOLANG_DEPENDENCY_NAME = "golang";
    private static final Logger LOGGER = Logger.getLogger(GolangServices.class.getName());
    private static final List<String> COMMAND_LIST_DIRECT_DEPDENDENCIES = List.of("go", "list", "-f",
            "{{if not .Indirect}}{{.}}{{end}}", "-m", "all");
    private static final Duration EXECUTION_TIMEOUT = Duration.ofSeconds(5);

    private final Supplier<String> projectVersion;

    GolangServices(final ProjectKeeperConfig config) {
        this(() -> extractVersion(config));
    }

    GolangServices(final Supplier<String> projectVersion) {
        this.projectVersion = projectVersion;
    }

    // [impl -> dsn~golang-project-version~1]
    private static String extractVersion(final ProjectKeeperConfig config) {
        final VersionConfig versionConfig = config.getVersionConfig();
        if (versionConfig == null) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-146")
                    .message("Version config is missing.")
                    .mitigation("Add a fixed version to your .project-keeper.yml, e.g. version: 1.2.3.").toString());
        } else if (versionConfig instanceof FixedVersion) {
            return ((FixedVersion) versionConfig).getVersion();
        } else {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-136")
                    .message("Version config has unexpected type {{type}}, expected a fixed version.",
                            versionConfig.getClass().getName())
                    .mitigation("Add a fixed version to your .project-keeper.yml, e.g. version: 1.2.3.").toString());
        }
    }

    Map<String, GolangDependencyLicense> getLicenses(final Path projectPath, final String module) {
        final SimpleProcess process;
        try {
            process = GoProcess.start(projectPath, List.of("go-licenses", "csv", module));
        } catch (final IllegalStateException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-142")
                    .message("Error starting the 'go-licenses' binary.")
                    .mitigation("Verify that 'go-licenses' is installed.")
                    .mitigation("Install it by running 'go install github.com/google/go-licenses@latest'.").toString(),
                    exception);
        }
        process.waitUntilFinished(EXECUTION_TIMEOUT);
        return Arrays.stream(process.getOutputStreamContent().split("\n")) //
                .filter(not(String::isBlank)) //
                .map(this::convertDependencyLicense)
                .collect(toMap(GolangDependencyLicense::getModuleName, Function.identity()));
    }

    private GolangDependencyLicense convertDependencyLicense(final String line) {
        final String[] parts = line.split(",");
        if (parts.length != 3) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-132").message(
                    "Invalid output line of command go-licenses: {{invalid line}}, expected 3 fields but got {{actual field count}}",
                    line, parts.length).toString());
        }
        final String moduleName = parts[0];
        final String licenseUrl = parts[1];
        final String licenseName = parts[2];
        LOGGER.finest(() -> "Found dependency '" + moduleName + "' with license '" + licenseName + "' and url '"
                + licenseUrl + "'");
        return new GolangDependencyLicense(moduleName, licenseName, licenseUrl);
    }

    /**
     * Get information about the Golang module with it's dependencies.
     *
     * @param projectPath the project path containing the {@code go.mod} file
     * @return module information
     */
    ModuleInfo getModuleInfo(final Path projectPath) {
        final SimpleProcess process = SimpleProcess.start(projectPath, COMMAND_LIST_DIRECT_DEPDENDENCIES);
        process.waitUntilFinished(EXECUTION_TIMEOUT);
        final String[] output = process.getOutputStreamContent().split("\n");
        final List<Dependency> dependencies = Arrays.stream(output) //
                .skip(1) // ignore first line, it is the project itself
                .map(this::convertDependency) //
                .collect(toList());
        return ModuleInfo.builder().moduleName(output[0]).dependencies(dependencies).build();
    }

    private Dependency convertDependency(final String line) {
        final String[] parts = line.split(" ");
        if (parts.length != 2) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-139")
                    .message("Invalid output line of command {{command}}: {{invalid line}}",
                            String.join(" ", COMMAND_LIST_DIRECT_DEPDENDENCIES), line)
                    .toString());
        }
        final String moduleName = parts[0];
        final String version = parts[1];
        LOGGER.finest(() -> "Found dependency in go.mod: '" + moduleName + "' with version '" + version + "'");
        return Dependency.builder().moduleName(moduleName).version(version).build();
    }

    /**
     * Get a list of {@link DependencyChange}s in the given {@code go.mod} file since the latest release.
     *
     * @param projectDir the project root dir containing the {@code .git} directory
     * @param modFile    the absolute path to the {@code go.mod} file
     * @return the list of {@link DependencyChange}s
     */
    // [impl -> dsn~golang-changed-dependency~1]
    List<DependencyChange> getDependencyChanges(final Path projectDir, final Path modFile) {
        final Optional<GoModFile> lastReleaseModFile = getLastReleaseModFileContent(projectDir, modFile)
                .map(GoModFile::parse);
        final GoModFile currentModFile = GoModFile.parse(readFile(modFile));
        return calculateChanges(lastReleaseModFile.orElse(null), currentModFile);
    }

    private String readFile(final Path file) {
        try {
            return Files.readString(file, StandardCharsets.UTF_8);
        } catch (final IOException exception) {
            throw new UncheckedIOException(
                    ExaError.messageBuilder("E-PK-CORE-135").message("Error loading file {{file}}", file).toString(),
                    exception);
        }
    }

    /**
     * Calculate the list of {@link DependencyChange}s between the given {@link GoModFile}s.
     *
     * @param oldModFile the content of the 'old' {@code go.mod} file from the previous release
     * @param newModFile the content of the current {@code go.mod} file
     * @return the list of {@link DependencyChange}s between both versions of the {@code go.mod} file
     */
    List<DependencyChange> calculateChanges(final GoModFile oldModFile, final GoModFile newModFile) {
        return new DependencyChangeCalculator(oldModFile, newModFile).calculateChanges();
    }

    private Optional<String> getLastReleaseModFileContent(final Path projectDir, final Path modFile) {
        try (GitRepository repo = GitRepository.open(projectDir)) {
            final Path relativeModFilePath = projectDir.relativize(modFile);
            return repo.findLatestReleaseCommit(getProjectVersion())
                    .map(tag -> getContent(repo, relativeModFilePath, tag));
        }
    }

    String getProjectVersion() {
        return this.projectVersion.get();
    }

    private String getContent(final GitRepository repo, final Path relativeModFilePath, final TaggedCommit tag) {
        try {
            return repo.getFileFromCommit(relativeModFilePath, tag.getCommit());
        } catch (final FileNotFoundException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-134")
                    .message("Go module file {{module file}} does not exist at tag {{tag}}", relativeModFilePath,
                            tag.getTag())
                    .toString(), exception);
        }
    }

    private static class DependencyChangeCalculator {
        private final Map<String, GoModDependency> oldDependencies;
        private final Map<String, GoModDependency> newDependencies;
        private final GoModFile oldModFile;
        private final GoModFile newModFile;
        private final List<DependencyChange> changes = new ArrayList<>();

        private DependencyChangeCalculator(final GoModFile oldModFile, final GoModFile newModFile) {
            this.oldModFile = oldModFile;
            this.newModFile = newModFile;
            this.oldDependencies = oldModFile == null ? emptyMap()
                    : oldModFile.getDirectDependencies().stream()
                            .collect(toMap(GoModDependency::getName, Function.identity()));
            this.newDependencies = newModFile.getDirectDependencies().stream()
                    .collect(toMap(GoModDependency::getName, Function.identity()));
        }

        List<DependencyChange> calculateChanges() {
            calculateGoVersionChanges();
            calculateDependencyChanges();
            return this.changes;
        }

        private void calculateGoVersionChanges() {
            final String oldVersion = this.oldModFile == null ? null : this.oldModFile.getGoVersion();
            final String newVersion = this.newModFile.getGoVersion();
            if ((oldVersion == null) && (newVersion != null)) {
                dependencyAdded(GOLANG_DEPENDENCY_NAME, newVersion);
            } else if ((oldVersion != null) && (newVersion == null)) {
                dependencyRemoved(GOLANG_DEPENDENCY_NAME, oldVersion);
            } else if ((oldVersion != null) && !oldVersion.equals(newVersion)) {
                dependencyUpdated(GOLANG_DEPENDENCY_NAME, oldVersion, newVersion);
            }
        }

        private void calculateDependencyChanges() {
            for (final Entry<String, GoModDependency> entry : this.newDependencies.entrySet()) {
                final String depName = entry.getKey();
                final String newVersion = entry.getValue().getVersion();
                if (this.oldDependencies.containsKey(depName)) {
                    final String oldVersion = this.oldDependencies.get(depName).getVersion();
                    if (!oldVersion.equals(newVersion)) {
                        dependencyUpdated(depName, oldVersion, newVersion);
                    }
                } else {
                    dependencyAdded(depName, newVersion);
                }
            }
            for (final Entry<String, GoModDependency> entry : this.oldDependencies.entrySet()) {
                final String depName = entry.getKey();
                if (!this.newDependencies.containsKey(depName)) {
                    dependencyRemoved(depName, entry.getValue().getVersion());
                }
            }
        }

        private void dependencyAdded(final String module, final String version) {
            this.changes.add(new NewDependency(null, module, version));
        }

        private void dependencyRemoved(final String module, final String version) {
            this.changes.add(new RemovedDependency(null, module, version));
        }

        private void dependencyUpdated(final String module, final String oldVersion, final String newVersion) {
            this.changes.add(new UpdatedDependency(null, module, oldVersion, newVersion));
        }
    }
}