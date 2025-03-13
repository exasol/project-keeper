package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.config.*;
import com.exasol.projectkeeper.shared.dependencies.VersionedDependency;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChange;
import com.exasol.projectkeeper.sources.analyze.generic.*;

/**
 * This class provides methods for retrieving information about a Golang project, e.g. its module name, dependencies and
 * dependency changes.
 */
class GolangServices {
    public static final String GOLANG_DEPENDENCY_NAME = "golang";
    private static final Logger LOGGER = Logger.getLogger(GolangServices.class.getName());
    private static final List<String> COMMAND_LIST_DIRECT_DEPENDENCIES = List.of("go", "list", "-f",
            "{{if not .Indirect}}{{.}}{{end}}", "-m", "all");
    private static final Duration EXECUTION_TIMEOUT = Duration.ofSeconds(30);

    private final CommandExecutor executor;
    private final GitService git;
    private final Supplier<String> projectVersionSupplier;

    GolangServices(final ProjectKeeperConfig config) {
        this(new CommandExecutor(), new GitService(), () -> extractVersion(config));
    }

    GolangServices(final CommandExecutor executor, final GitService git,
            final Supplier<String> projectVersionSupplier) {
        this.executor = executor;
        this.git = git;
        this.projectVersionSupplier = projectVersionSupplier;
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

    Map<String, GolangDependencyLicense> getLicenses(final Path absoluteSourcePath, final String module) {
        final String[] licenses = retrieveLicenses(absoluteSourcePath, module).split("\n");
        return Arrays.stream(licenses) //
                .filter(not(String::isBlank)) //
                .map(this::convertDependencyLicense)
                .collect(toMap(GolangDependencyLicense::getModuleName, Function.identity()));
    }

    private String retrieveLicenses(final Path absoluteSourcePath, final String module) {
        final GoBinary goLicenses = GoBinary.GO_LICENSES.install();
        final ShellCommand shellCommand = ShellCommand.builder() //
                .timeout(EXECUTION_TIMEOUT) //
                .command(goLicenses.command()) //
                .args("csv", module) //
                .workingDir(absoluteSourcePath).build();
        try {
            return this.executor.execute(shellCommand).getOutputStreamContent();
        } catch (final RuntimeException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-142")
                            .message("Error starting the 'go-licenses' binary in working dir {{working dir}}.")
                            .parameter("working dir", absoluteSourcePath,
                                    "working directory where go-licenses was executed")
                            .mitigation("Verify that 'go-licenses' is installed.")
                            .mitigation("Install it by running 'go install github.com/google/go-licenses@latest'.")
                            .mitigation("If it is already installed, re-install it by running the same command.")
                            .mitigation("This might be necessary after installing a new Go version.").toString(),
                    exception);
        }
    }

    Path getModuleDir(final Path absoluteSourcePath, final String moduleName) {
        final ShellCommand shellCommand = ShellCommand.builder() //
                .timeout(Duration.ofSeconds(3)) //
                .command(GoBinary.GO.command()) //
                .args("list", "-m", "-f", "{{.Dir}}", moduleName) //
                .workingDir(absoluteSourcePath) //
                .build();
        final String output = this.executor.execute(shellCommand).getOutputStreamContent().trim();
        if (output.isEmpty()) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-160")
                    .message("Did not get directory for module {{module name}}.", moduleName).ticketMitigation()
                    .toString());
        }
        final Path path = Path.of(output).toAbsolutePath();
        LOGGER.finest(() -> "Found module dir '" + path + "' for module '" + moduleName + "'");
        if (!Files.exists(path)) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-156")
                    .message("Directory {{directory}} for module {{module name}} does not exist", path, moduleName)
                    .ticketMitigation().toString());
        }
        return path;
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
        return new GolangDependencyLicense(moduleName, licenseName, licenseUrl);
    }

    /**
     * Get information about the Golang module with it's dependencies.
     *
     * @param absoluteSourcePath the project path containing the {@code go.mod} file
     * @return module information
     */
    GoModule getModuleInfo(final Path absoluteSourcePath) {
        final SimpleProcess process = SimpleProcess.start(absoluteSourcePath, COMMAND_LIST_DIRECT_DEPENDENCIES);
        try {
            process.waitUntilFinished(EXECUTION_TIMEOUT);
        } catch (final IllegalStateException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-157").message("Failed to list direct dependencies.")
                            .mitigation("Run 'go mod tidy' and try again.").toString(),
                    exception);
        }
        final String[] output = process.getOutputStreamContent().split("\n");
        final List<VersionedDependency> dependencies = Arrays.stream(output) //
                .skip(1) // ignore first line, it is the project itself
                .map(this::convertDependency) //
                .toList();
        return new GoModule(output[0], dependencies);
    }

    private VersionedDependency convertDependency(final String line) {
        final String[] parts = line.split(" ");
        if (parts.length != 2) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-139")
                    .message("Invalid output line of command {{command}}: {{invalid line}}",
                            String.join(" ", COMMAND_LIST_DIRECT_DEPENDENCIES), line)
                    .toString());
        }
        final String moduleName = parts[0];
        final String version = parts[1];
        return VersionedDependency.builder() //
                .name(moduleName) //
                .version(version) //
                .isIndirect(false) //
                .build();
    }

    /**
     * Get a list of {@link DependencyChange}s in the given {@code go.mod} file since the latest release.
     *
     * @param projectDir      the project root dir containing the {@code .git} directory
     * @param relativeModFile the absolute path to the {@code go.mod} file
     * @return the list of {@link DependencyChange}s
     */
    // [impl -> dsn~golang-changed-dependency~1]
    List<DependencyChange> getDependencyChanges(final Path projectDir, final Path relativeModFile) {
        final Optional<GoModFile> previous = new PreviousRelease(this.git) //
                .projectDir(projectDir) //
                .currentVersion(getProjectVersion()) //
                .file(relativeModFile) //
                .getContent() //
                .map(GoModFile::parse);
        final GoModFile current = GoModFile.parse(readFile(projectDir.resolve(relativeModFile)));
        return calculateChanges(previous, current);
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
     * @param previous the content of the {@code go.mod} file from the previous release
     * @param current  the content of the current {@code go.mod} file
     * @return the list of {@link DependencyChange}s between both versions of the {@code go.mod} file
     */
    List<DependencyChange> calculateChanges(final Optional<GoModFile> previous, final GoModFile current) {
        return DependencyChanges.builder() //
                .from(previous.map(GoModFile::getDirectDependencies)) //
                .to(current.getDirectDependencies()) //
                .withChange(GOLANG_DEPENDENCY_NAME, //
                        previous.map(GoModFile::getGoVersion), //
                        Optional.ofNullable(current.getGoVersion())) //
                .build();
    }

    String getProjectVersion() {
        return this.projectVersionSupplier.get();
    }

    void installDependencies(final Path projectPath) {
        final ShellCommand sc = ShellCommand.builder() //
                .timeout(Duration.ofMinutes(2)) //
                .command(GoBinary.GO.command()) //
                .args("get", "-t", "./...") //
                .workingDir(projectPath) //
                .build();
        this.executor.execute(sc);
    }
}
