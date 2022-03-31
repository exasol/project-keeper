package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.dependencies.License;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency.Type;
import com.exasol.projectkeeper.sources.analyze.golang.ModuleInfo.Dependency;

public class GolangServices {
    private static final List<String> COMMAND_LIST_DIRECT_DEPDENDENCIES = List.of("go", "list", "-f",
            "{{if not .Indirect}}{{.}}{{end}}", "-m", "all");
    private static final List<String> COMMAND_LIST_LICENSES = List.of("go-licenses", "csv", "./...");

    private static final Logger LOGGER = Logger.getLogger(GolangServices.class.getName());

    private static final Duration EXECUTION_TIMEOUT = Duration.ofSeconds(5);

    public String getModuleName(final Path projectPath) {
        final SimpleProcess process = SimpleProcess.start(projectPath, List.of("go", "list"));
        final String moduleName = process.getOutput(EXECUTION_TIMEOUT).trim();
        LOGGER.info(() -> "Found module name '" + moduleName + "' for project " + projectPath);
        return moduleName;
    }

    public List<ProjectDependency> getDependencies(final Path projectPath) {
        final ModuleInfo moduleInfo = getModuleInfo(projectPath);
        final List<ProjectDependency> dependencies = new ArrayList<>(moduleInfo.getDependencies().size());
        final Map<String, GolangDependencyLicense> golangLicenses = getLicenses(projectPath);
        for (final Dependency dependency : moduleInfo.getDependencies()) {
            final String websiteUrl = null;
            final GolangDependencyLicense license = golangLicenses.get(dependency.getModuleName());
            final Type dependencyType = license == null ? Type.TEST : Type.COMPILE;
            final List<License> licenses = license == null ? emptyList()
                    : List.of(new License(license.getLicenseName(), license.getLicenseUrl()));
            dependencies.add(ProjectDependency.builder().name(dependency.getModuleName()).type(dependencyType)
                    .websiteUrl(websiteUrl).licenses(licenses).build());
        }
        return dependencies;
    }

    private Map<String, GolangDependencyLicense> getLicenses(final Path projectPath) {
        final SimpleProcess process = SimpleProcess.start(projectPath, COMMAND_LIST_LICENSES);
        return Arrays.stream(process.getOutput(EXECUTION_TIMEOUT).split("\n")) //
                .map(this::convertDependencyLicense)
                .collect(toMap(GolangDependencyLicense::getModuleName, Function.identity()));
    }

    private GolangDependencyLicense convertDependencyLicense(final String line) {
        final String[] parts = line.split(",");
        if (parts.length != 3) {
            throw new IllegalStateException(ExaError.messageBuilder("").message(
                    "Invalid output line of command {{command}}: {{invalid line}}, expected 3 fields but got {{actual field count}}",
                    String.join(" ", COMMAND_LIST_LICENSES), line, parts.length).toString());
        }
        return new GolangDependencyLicense(parts[0], parts[1], parts[2]);
    }

    private ModuleInfo getModuleInfo(final Path projectPath) {
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
            throw new IllegalStateException(
                    ExaError.messageBuilder("").message("Invalid output line of command {{command}}: {{invalid line}}",
                            String.join(" ", COMMAND_LIST_DIRECT_DEPDENDENCIES), line).toString());
        }
        return Dependency.builder().moduleName(parts[0]).version(parts[1]).build();
    }
}