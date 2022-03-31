package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ProjectVersionDetector;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChange;
import com.exasol.projectkeeper.shared.repository.GitRepository;
import com.exasol.projectkeeper.shared.repository.TaggedCommit;
import com.exasol.projectkeeper.sources.analyze.golang.ModuleInfo.Dependency;

public class GolangServices {
    private static final List<String> COMMAND_LIST_DIRECT_DEPDENDENCIES = List.of("go", "list", "-f",
            "{{if not .Indirect}}{{.}}{{end}}", "-m", "all");

    private static final Logger LOGGER = Logger.getLogger(GolangServices.class.getName());

    private static final Duration EXECUTION_TIMEOUT = Duration.ofSeconds(5);

    private final Supplier<String> projectVersionSupplier;

    public GolangServices(final ProjectKeeperConfig config) {
        this.projectVersionSupplier = extractVersion(config);
    }

    private static Supplier<String> extractVersion(final ProjectKeeperConfig config) {
        return () -> new ProjectVersionDetector().detectVersion(config, emptyList());
    }

    public List<ProjectDependency> getDependencies(final ModuleInfo moduleInfo, final Path projectPath) {
        final List<ProjectDependency> dependencies = new ArrayList<>(moduleInfo.getDependencies().size());
        final Map<String, GolangDependencyLicense> golangLicenses = getLicenses(projectPath, "./...");
        for (final Dependency dependency : moduleInfo.getDependencies()) {
            final String websiteUrl = null;
            final String moduleName = dependency.getModuleName();
            final Type dependencyType = golangLicenses.containsKey(moduleName) ? Type.COMPILE : Type.TEST;
            final GolangDependencyLicense license = golangLicenses.containsKey(moduleName)
                    ? golangLicenses.get(moduleName)
                    : null;

            dependencies.add(ProjectDependency.builder().name(moduleName).type(dependencyType).websiteUrl(websiteUrl)
                    .licenses(license == null ? emptyList() : List.of(license.toLicense())) //
                    .build());
        }
        return dependencies;
    }

    private Map<String, GolangDependencyLicense> getLicenses(final Path projectPath, final String module) {
        final SimpleProcess process = SimpleProcess.start(projectPath, List.of("go-licenses", "csv", module));
        return Arrays.stream(process.getOutput(EXECUTION_TIMEOUT).split("\n")) //
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
        return new GolangDependencyLicense(parts[0], parts[1], parts[2]);
    }

    public ModuleInfo getModuleInfo(final Path projectPath) {
        final SimpleProcess process = SimpleProcess.start(projectPath, COMMAND_LIST_DIRECT_DEPDENDENCIES);
        final String[] output = process.getOutput(EXECUTION_TIMEOUT).split("\n");
        final List<Dependency> dependencies = Arrays.stream(output) //
                .skip(1) // ignore first line, it is the project itself
                .map(this::convertDependency) //
                .collect(toList());
        return ModuleInfo.builder().moduleName(output[0]).dependencies(dependencies).build();
    }

    private Dependency convertDependency(final String line) {
        final String[] parts = line.split(" ");
        if (parts.length != 2) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-124")
                    .message("Invalid output line of command {{command}}: {{invalid line}}",
                            String.join(" ", COMMAND_LIST_DIRECT_DEPDENDENCIES), line)
                    .toString());
        }
        return Dependency.builder().moduleName(parts[0]).version(parts[1]).build();
    }

    public List<DependencyChange> getDependencyChanges(final Path projectDir, final Path modFile) {
        final GoModFile lastReleaseModFile = GoModFile.parse(getLastReleaseModFileContent(projectDir, modFile));
        return emptyList();
    }

    private String getLastReleaseModFileContent(final Path projectDir, final Path modFile) {
        try (GitRepository repo = GitRepository.open(projectDir)) {
            final Optional<TaggedCommit> tag = repo.findLatestReleaseCommit(this.projectVersionSupplier.get());
            if (tag.isEmpty()) {
                throw new IllegalStateException(
                        ExaError.messageBuilder("E-PK-CORE-133").message("Latest release commit not found").toString());
            }
            final Path relativeModFilePath = projectDir.relativize(modFile);
            try {
                return repo.getFileFromCommit(relativeModFilePath, tag.get().getCommit());
            } catch (final FileNotFoundException exception) {
                throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-134")
                        .message("Go module file {{module file}} does not exist at tag {{tag}}", relativeModFilePath,
                                tag.get().getTag())
                        .toString(), exception);
            }
        }
    }

}