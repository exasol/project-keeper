package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.shared.dependencies.*;
import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;

@ExtendWith(MockitoExtension.class)
class GolangDependencyCalculatorTest {

    private static final Path PROJECT_PATH = Paths.get("project-path");
    private static final String PROJECT_MODULE_NAME = "main-module";
    @Mock
    private GolangServices golangServicesMock;

    @Test
    void emptyDependencyList() {
        simulateMainModuleLicenses(emptyMap());
        assertThat(calculate(), hasSize(0));
    }

    @Test
    void calculatorIgnoresLicenses() {
        simulateMainModuleLicenses(Map.of("mod1", license("mod1", "lic1", "url1")));
        assertThat(calculate(), hasSize(0));
    }

    @Test
    void onlyCompileDependencies() {
        simulateMainModuleLicenses(Map.of("comp1", license("mod1", "lic1", "url1")));
        final List<ProjectDependency> dependencies = calculate(dep("comp1", "ver1"));
        assertThat(dependencies, hasSize(1));
        assertThat(dependencies, contains(expectedDep("comp1", "lic1", "url1", Type.COMPILE)));
    }

    @Test
    void onlyTestDependencies() {
        simulateMainModuleLicenses(Map.of());
        simulateLicenses("test1", Map.of("test1", license("test1", "lic1", "url1")));
        final List<ProjectDependency> dependencies = calculate(dep("test1", "ver1"));
        assertThat(dependencies, hasSize(1));
        assertThat(dependencies, contains(expectedDep("test1", "lic1", "url1", Type.TEST)));
    }

    @Test
    void mixedDependencies() {
        simulateMainModuleLicenses(Map.of("comp2", license("mod2", "lic2", "url2")));
        simulateLicenses("test1", Map.of("test1", license("test1", "lic1", "url1")));
        final List<ProjectDependency> dependencies = calculate(dep("test1", "ver1"), dep("comp2", "ver2"));
        assertThat(dependencies, hasSize(2));
        assertThat(dependencies, contains(expectedDep("test1", "lic1", "url1", Type.TEST),
                expectedDep("comp2", "lic2", "url2", Type.COMPILE)));
    }

    @Test
    void dependencyWithPrefixFound() {
        simulateMainModuleLicenses(Map.of("dep1/suffix", license("dep1", "lic1", "url1")));
        final List<ProjectDependency> dependencies = calculate(dep("dep1", "ver1"));
        assertThat(dependencies, hasSize(1));
        assertThat(dependencies, contains(expectedDep("dep1", "lic1", "url1", Type.TEST)));
    }

    @Test
    void licenseNotFound() {
        simulateMainModuleLicenses(Map.of());
        simulateLicenses("test1", Map.of());
        final VersionedDependency dep = dep("test1", "ver1");
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> calculate(dep));
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-147: No license found for test dependency module 'test1', all licenses: {}"));
    }

    private ProjectDependency expectedDep(final String name, final String licencesName, final String licenseUrl,
            final Type type) {
        return ProjectDependency.builder() //
                .type(type) //
                .name(name) //
                .licenses(List.of(new License(licencesName, licenseUrl))) //
                .build();
    }

    private VersionedDependency dep(final String moduleName, final String version) {
        return VersionedDependency.builder().name(moduleName).version(version).isIndirect(false).build();
    }

    private GolangDependencyLicense license(final String moduleName, final String licenseName,
            final String licenseUrl) {
        return new GolangDependencyLicense(moduleName, licenseName, licenseUrl);
    }

    private void simulateMainModuleLicenses(final Map<String, GolangDependencyLicense> licenses) {
        when(this.golangServicesMock.getLicenses(PROJECT_PATH, "./...")).thenReturn(licenses);
    }

    private void simulateLicenses(final String moduleName, final Map<String, GolangDependencyLicense> licenses) {
        final Path modulePath = Paths.get("modulePath");
        when(this.golangServicesMock.getModuleDir(PROJECT_PATH, moduleName)).thenReturn(modulePath);
        when(this.golangServicesMock.getLicenses(modulePath, moduleName)).thenReturn(licenses);
    }

    private List<ProjectDependency> calculate(final VersionedDependency... goModDependencies) {
        return GolangDependencyCalculator.calculateDependencies(this.golangServicesMock, PROJECT_PATH,
                new GoModule(PROJECT_MODULE_NAME, asList(goModDependencies))).getDependencies();
    }
}
