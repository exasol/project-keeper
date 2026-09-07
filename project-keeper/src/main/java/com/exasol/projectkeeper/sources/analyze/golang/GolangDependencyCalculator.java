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

    private Map<String, List<GolangDependencyLicense>> compileDependencyLicenses;
    private Map<String, List<GolangDependencyLicense>> allLicenses;

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
        this.allLicenses = fetchLicensesIncludingTests();
        final List<ProjectDependency> projectDependencies = this.moduleInfo.getDependencies().stream()
                .map(this::convertDependency).toList();
        return new ProjectDependencies(projectDependencies);
    }

    private ProjectDependency convertDependency(final VersionedDependency dependency) {
        return ProjectDependency.builder()
                .name(dependency.getName())
                .type(getDependencyType(dependency.getName()))
                .websiteUrl(null)
                .licenses(getLicenses(dependency))
                .build();
    }

    private List<License> getLicenses(final VersionedDependency dependency) {
        return getLicenses(dependency.getName()).stream().map(GolangDependencyLicense::toLicense).toList();
    }

    private List<GolangDependencyLicense> getLicenses(final String moduleName) {
        if (this.allLicenses.containsKey(moduleName)) {
            return this.allLicenses.get(moduleName);
        }
        final Optional<List<GolangDependencyLicense>> prefixMatch = this.allLicenses.entrySet().stream()
                .filter(e -> e.getKey().startsWith(moduleName))
                .map(Entry::getValue).findAny();
        if (prefixMatch.isPresent()) {
            return prefixMatch.get();
        }
        throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-147").message(
                "No license found for test dependency module {{module name}}, all licenses: {{all licenses}}",
                moduleName, this.allLicenses).toString());
    }

    private Map<String, List<GolangDependencyLicense>> fetchLicensesForMainModule() {
        return this.golangServices.getLicenses(this.projectPath);
    }

    private Map<String, List<GolangDependencyLicense>> fetchLicensesIncludingTests() {
        return this.golangServices.getLicensesIncludingTests(this.projectPath);
    }

    /**
     * Get the dependency type of a given module.
     * <p>
     * Note: this is a heuristic. Go does not distinguish between compile and test dependencies in {@code go.mod}. This
     * implementation uses the fact that the normal {@code go-licenses} invocation omits test dependencies and returns test dependencies only with
     * {@code --include_tests}.
     *
     * @param moduleName the module name
     * @return the module's dependency type
     */
    private Type getDependencyType(final String moduleName) {
        return this.compileDependencyLicenses.containsKey(moduleName) ? Type.COMPILE : Type.TEST;
    }
}
