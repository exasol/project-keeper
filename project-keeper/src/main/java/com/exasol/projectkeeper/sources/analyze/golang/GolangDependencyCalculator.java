package com.exasol.projectkeeper.sources.analyze.golang;

import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.dependencies.*;
import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;

class GolangDependencyCalculator {

    private final GoModule moduleInfo;
    private final Path projectPath;
    private final GolangServices golangServices;

    private Map<String, GolangDependencyLicense> compileDependencyLicenses;
    private Map<String, GolangDependencyLicense> allLicenses;

    private GolangDependencyCalculator(final GolangServices golangServices, final Path projectPath,
            final GoModule moduleInfo) {
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
            final GoModule moduleInfo) {
        golangServices.installDependencies(projectPath);
        final GolangDependencyCalculator calculator = new GolangDependencyCalculator(golangServices, projectPath,
                moduleInfo);
        return calculator.getDependencies();
    }

    private ProjectDependencies getDependencies() {
        this.compileDependencyLicenses = fetchLicensesForMainModule();
        this.allLicenses = new HashMap<>(this.compileDependencyLicenses);
        final List<ProjectDependency> projectDependencies = this.moduleInfo.getDependencies().stream()
                .map(this::convertDependency).toList();
        return new ProjectDependencies(projectDependencies);
    }

    private ProjectDependency convertDependency(final VersionedDependency dependency) {
        return ProjectDependency.builder() //
                .name(dependency.getName()) //
                .type(getDependencyType(dependency.getName())) //
                .websiteUrl(null) //
                .licenses(List.of(getLicense(dependency.getName()).toLicense())) //
                .build();
    }

    private GolangDependencyLicense getLicense(final String moduleName) {
        if (this.allLicenses.containsKey(moduleName)) {
            return this.allLicenses.get(moduleName);
        }
        final Optional<GolangDependencyLicense> prefixMatch = this.allLicenses.entrySet().stream() //
                .filter(e -> e.getKey().startsWith(moduleName)) //
                .map(Entry::getValue).findAny();
        if (prefixMatch.isPresent()) {
            return prefixMatch.get();
        }
        return fetchLicense(moduleName);
    }

    private GolangDependencyLicense fetchLicense(final String moduleName) {
        final Map<String, GolangDependencyLicense> licenses = fetchAllLicenses(moduleName);
        this.allLicenses.putAll(licenses);
        final GolangDependencyLicense license = licenses.get(moduleName);
        if (license == null) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-147").message(
                    "No license found for test dependency module {{module name}}, all licenses: {{all licenses}}",
                    moduleName, licenses).toString());
        }
        return license;
    }

    private Map<String, GolangDependencyLicense> fetchLicensesForMainModule() {
        return this.golangServices.getLicenses(this.projectPath, "./...");
    }

    private Map<String, GolangDependencyLicense> fetchAllLicenses(final String moduleName) {
        final Path moduleDir = this.golangServices.getModuleDir(this.projectPath, moduleName);
        return this.golangServices.getLicenses(moduleDir, moduleName);
    }

    /**
     * Get the dependency type of a given module.
     * <p>
     * Note: this is a heuristic. Go does not distinguish between compile and test dependencies in {@code go.mod}. This
     * implementation uses the fact that the {@code go-licenses} tool only returns licenses for compile dependencies and
     * omits test dependencies.
     *
     * @param moduleName the module name
     * @return the module's dependency type
     */
    private Type getDependencyType(final String moduleName) {
        return this.compileDependencyLicenses.containsKey(moduleName) ? Type.COMPILE : Type.TEST;
    }
}
