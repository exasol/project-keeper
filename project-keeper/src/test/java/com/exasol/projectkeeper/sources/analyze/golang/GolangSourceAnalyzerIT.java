package com.exasol.projectkeeper.sources.analyze.golang;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.SourceType.GOLANG;
import static java.util.Collections.emptySet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.*;
import com.exasol.projectkeeper.shared.dependencies.License;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.*;
import com.exasol.projectkeeper.sources.AnalyzedGolangSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.test.GolangProjectFixture;

class GolangSourceAnalyzerIT {

    @TempDir
    Path projectDir;

    private GolangProjectFixture fixture;

    @BeforeEach
    void setup() {
        this.fixture = new GolangProjectFixture(this.projectDir);
        this.fixture.gitInit();
    }

    @AfterEach
    void teardown() {
        this.fixture.close();
    }

    @Test
    void testInvalidPath() {
        final List<Source> sources = List.of(Source.builder().type(GOLANG).path(this.projectDir).build());
        final ProjectKeeperConfig config = ProjectKeeperConfig.builder().sources(sources).build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyzeSingleProject(config));
        assertThat(exception.getMessage(), matchesPattern(
                "\\QE-PK-CORE-133: Invalid path \\E.*\\Q for go source. The path must point to a \"go.mod\" file.\\E"));
    }

    @Test
    void testMissingVersionConfig() {
        this.fixture.prepareProjectFiles();
        final ProjectKeeperConfig config = this.fixture.createDefaultConfig().versionConfig(null).build();
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> analyzeSingleProject(config));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-146: Version config is missing. Add a fixed version to your .project-keeper.yml, e.g. version: 1.2.3."));
    }

    @Test
    void testWrongVersionConfigType() {
        this.fixture.prepareProjectFiles();
        final ProjectKeeperConfig config = this.fixture.createDefaultConfig()
                .versionConfig(new VersionFromSource(this.projectDir.resolve("file"))).build();
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> analyzeSingleProject(config));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-136: Version config has unexpected type 'com.exasol.projectkeeper.shared.config.ProjectKeeperConfig$VersionFromSource', expected a fixed version. Add a fixed version to your .project-keeper.yml, e.g. version: 1.2.3."));
    }

    @Test
    void testGoModuleInProjectDirectory() {
        prepareProjectFiles(Paths.get("."));
        final ProjectKeeperConfig config = this.fixture.createDefaultConfig().build();
        final AnalyzedSource analyzedProject = analyzeSingleProject(config);
        assertAll( //
                () -> assertCommonProperties(analyzedProject), //
                () -> assertIsrootProject(analyzedProject, true), //
                () -> assertThat("project path", analyzedProject.getPath(), equalTo(Paths.get("go.mod"))),
                () -> assertThat("project name", analyzedProject.getProjectName(),
                        equalTo(this.projectDir.getFileName().toString())));
    }

    @Test
    void testGoModuleInSubdirectory() {
        prepareProjectFiles(Paths.get("subdir"));
        final ProjectKeeperConfig config = ProjectKeeperConfig.builder()
                .sources(List.of(ProjectKeeperConfig.Source.builder().modules(emptySet()).type(SourceType.GOLANG)
                        .path(Paths.get("subdir").resolve("go.mod")).build()))
                .versionConfig(new ProjectKeeperConfig.FixedVersion(this.fixture.getProjectVersion())).build();
        final AnalyzedSource analyzedProject = analyzeSingleProject(config);
        assertAll( //
                () -> assertCommonProperties(analyzedProject), //
                () -> assertIsrootProject(analyzedProject, false),
                () -> assertThat("project path", analyzedProject.getPath(), equalTo(Paths.get("subdir/go.mod"))),
                () -> assertThat("project name", analyzedProject.getProjectName(), equalTo("subdir")));
    }

    @Test
    void testWithAbsoluteSourcePath() {
        prepareProjectFiles(Paths.get("subdir"));
        final Path modPath = this.projectDir.resolve("subdir/go.mod");
        final ProjectKeeperConfig config = ProjectKeeperConfig.builder()
                .sources(List.of(ProjectKeeperConfig.Source.builder().modules(emptySet()).type(SourceType.GOLANG)
                        .path(modPath).build()))
                .versionConfig(new ProjectKeeperConfig.FixedVersion(this.fixture.getProjectVersion())).build();
        final AnalyzedSource analyzedProject = analyzeSingleProject(config);
        assertAll( //
                () -> assertCommonProperties(analyzedProject), //
                () -> assertIsrootProject(analyzedProject, false),
                () -> assertThat("project path", analyzedProject.getPath(), equalTo(modPath)),
                () -> assertThat("project name", analyzedProject.getProjectName(), equalTo("subdir")));
    }

    private void prepareProjectFiles(final Path moduleDir) {
        this.fixture.prepareProjectFiles(moduleDir, "1.15");
        this.fixture.gitAddCommitTag("1.2.2");
        this.fixture.prepareProjectFiles(moduleDir, "1.16");
    }

    private void assertCommonProperties(final AnalyzedSource analyzedProject) {
        assertAll( //
                () -> assertDependencyLicenses(analyzedProject.getDependencies().getDependencies()),
                () -> assertDependencyChanges(analyzedProject.getDependencyChanges()),
                () -> assertThat("module name", ((AnalyzedGolangSource) analyzedProject).getModuleName(),
                        equalTo("github.com/exasol/my-module")),
                () -> assertThat("advertise", analyzedProject.isAdvertise(), is(true)),
                () -> assertThat("version", analyzedProject.getVersion(), equalTo(this.fixture.getProjectVersion())));
    }

    private void assertIsrootProject(final AnalyzedSource analyzedProject, final boolean expectedValue) {
        assertThat("is root project", ((AnalyzedGolangSource) analyzedProject).isRootProject(), is(expectedValue));
    }

    private void assertDependencyChanges(final DependencyChangeReport dependencyChanges) {
        assertAll(() -> assertThat("plugin dependencies", dependencyChanges.getPluginDependencyChanges(), hasSize(0)),
                () -> assertThat("runtime dependencies", dependencyChanges.getRuntimeDependencyChanges(), hasSize(0)),
                () -> assertThat("compile dependencies", dependencyChanges.getCompileDependencyChanges(),
                        contains(updatedDep("golang", "1.15", "1.16"))),
                () -> assertThat("test dependencies", dependencyChanges.getTestDependencyChanges(), hasSize(0)));
    }

    private DependencyChange updatedDep(final String name, final String oldVersion, final String newVersion) {
        return new UpdatedDependency(null, name, oldVersion, newVersion);
    }

    private void assertDependencyLicenses(final List<ProjectDependency> dependencies) {
        final ProjectDependency dependency1 = ProjectDependency.builder().name("github.com/exasol/exasol-driver-go")
                .licenses(List.of(new License("MIT", "https://github.com/exasol/exasol-driver-go/blob/v0.4.0/LICENSE")))
                .type(Type.COMPILE).build();
        final ProjectDependency dependency2 = ProjectDependency.builder()
                .name("github.com/exasol/exasol-test-setup-abstraction-server/go-client")
                .licenses(List.of(new License("MIT",
                        "https://github.com/exasol/exasol-test-setup-abstraction-server/blob/0dd00179907c/go-client/LICENSE")))
                .type(Type.TEST).build();
        assertAll(() -> assertThat(dependencies, hasSize(2)),
                () -> assertThat(dependencies, contains(dependency1, dependency2)));
    }

    private AnalyzedSource analyzeSingleProject(final ProjectKeeperConfig config) {
        final GolangSourceAnalyzer analyzer = new GolangSourceAnalyzer(config);
        final List<AnalyzedSource> analyzedSources = analyzer.analyze(this.projectDir, config.getSources());
        assertThat(analyzedSources, hasSize(1));
        return analyzedSources.get(0);
    }
}