package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency.Type;
import com.exasol.projectkeeper.sources.analyze.golang.ModuleInfo.Dependency;

class GolangDependencyCalculator {

    private final ModuleInfo moduleInfo;
    private final Path projectPath;
    private final GolangServices golangServices;

    private Map<String, GolangDependencyLicense> compileDependencyLicenses;
    private Map<String, GolangDependencyLicense> allLicenses;

    private GolangDependencyCalculator(final GolangServices golangServices, final Path projectPath,
            final ModuleInfo moduleInfo) {
        this.golangServices = golangServices;
        this.projectPath = projectPath;
        this.moduleInfo = moduleInfo;
    }

    /**
     * Get the dependencies of a Golang project including their licenses.
     *
     * @param golangServices the {@link GolangServices} for retrieving project information
     * @param moduleInfo     the module info of the project
     * @param projectPath    the project path
     * @return dependencies including licenses
     */
    // [impl -> dsn~golang-dependency-licenses~1]
    static ProjectDependencies calculateDependencies(final GolangServices golangServices, final Path projectPath,
            final ModuleInfo moduleInfo) {
        final GolangDependencyCalculator calculator = new GolangDependencyCalculator(golangServices, projectPath,
                moduleInfo);
        return calculator.getDependencies();
    }

    private ProjectDependencies getDependencies() {
        this.compileDependencyLicenses = fetchLicenses("./...");
        this.allLicenses = new HashMap<>(this.compileDependencyLicenses);
        final List<ProjectDependency> projectDependencies = this.moduleInfo.getDependencies().stream()
                .map(this::convertDependency).collect(toList());
        return new ProjectDependencies(projectDependencies);
    }

    private ProjectDependency convertDependency(final Dependency dependency) {
        return ProjectDependency.builder() //
                .name(dependency.getModuleName()) //
                .type(getDependencyType(dependency.getModuleName())) //
                .websiteUrl(null) //
                .licenses(List.of(getLicense(dependency.getModuleName()).toLicense())) //
                .build();
    }

    private GolangDependencyLicense getLicense(final String moduleName) {
        if (this.allLicenses.containsKey(moduleName)) {
            return this.allLicenses.get(moduleName);
        }
        return fetchLicense(moduleName);
    }

    private GolangDependencyLicense fetchLicense(final String moduleName) {
        final Map<String, GolangDependencyLicense> licenses = fetchLicenses(moduleName);
        this.allLicenses.putAll(licenses);
        final GolangDependencyLicense license = licenses.get(moduleName);
        if (license == null) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-147").message(
                    "No license found for test dependency module {{module name}}, all licenses: {{all licenses}}",
                    moduleName, licenses).toString());
        }
        return license;
    }

    private Map<String, GolangDependencyLicense> fetchLicenses(final String moduleName) {
        return this.golangServices.getLicenses(this.projectPath, moduleName);
    }

    /**
     * Get the dependency type of a given module.
     * <p>
     * Note: this is a heuristic. Go does not distinguish between compile and test dependencies in {@code go.mod}. This
     * implementation uses the fact that the {@code go-licenses} tool only returns licenses for compile dependencies and
     * omits test dependencies.
     *
     * @param moduleName the module name
     * @return the module's depenency type
     */
    private Type getDependencyType(final String moduleName) {
        return this.compileDependencyLicenses.containsKey(moduleName) ? Type.COMPILE : Type.TEST;
    }
}
