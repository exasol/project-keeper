package com.exasol.projectkeeper.sources.analyze.golang;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.SourceType.GOLANG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.VersionFromSource;
import com.exasol.projectkeeper.shared.dependencies.License;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency.Type;
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

    @Test
    void testInvalidPath() {
        final List<Source> sources = List.of(Source.builder().type(GOLANG).path(this.projectDir).build());
        final ProjectKeeperConfig config = ProjectKeeperConfig.builder().sources(sources).build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> analyze(config));
        assertThat(exception.getMessage(), matchesPattern(
                "\\QE-PK-CORE-133: Invalid path \\E.*\\Q for go source. The path must point to a \"go.mod\" file.\\E"));
    }

    @Test
    void testMissingVersionConfig() {
        this.fixture.prepareProjectFiles();
        final ProjectKeeperConfig config = this.fixture.createDefaultConfigWithAbsolutePath().versionConfig(null)
                .build();
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> analyze(config));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-146: Version config is missing. Add a fixed version to your .project-keeper.yml, e.g. version: 1.2.3."));
    }

    @Test
    void testWrongVersionConfigType() {
        this.fixture.prepareProjectFiles();
        final ProjectKeeperConfig config = this.fixture.createDefaultConfigWithAbsolutePath()
                .versionConfig(new VersionFromSource(this.projectDir.resolve("file"))).build();
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> analyze(config));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-136: Version config has unexpected type 'com.exasol.projectkeeper.shared.config.ProjectKeeperConfig$VersionFromSource', expected a fixed version. Add a fixed version to your .project-keeper.yml, e.g. version: 1.2.3."));
    }

    @Test
    void testGetDependencyLicenses() {
        this.fixture.prepareProjectFiles();
        final ProjectKeeperConfig config = this.fixture.createDefaultConfigWithAbsolutePath().build();
        final List<AnalyzedSource> analyzedSources = analyze(config);
        assertThat(analyzedSources, hasSize(1));
        final List<ProjectDependency> dependencies = analyzedSources.get(0).getDependencies().getDependencies();
        assertThat(dependencies, hasSize(2));
        final ProjectDependency dependency1 = ProjectDependency.builder().name("github.com/exasol/exasol-driver-go")
                .licenses(List.of(new License("MIT", "https://github.com/exasol/exasol-driver-go/blob/v0.4.0/LICENSE")))
                .type(Type.COMPILE).build();
        final ProjectDependency dependency2 = ProjectDependency.builder()
                .name("github.com/exasol/exasol-test-setup-abstraction-server/go-client")
                .licenses(List.of(new License("MIT",
                        "https://github.com/exasol/exasol-test-setup-abstraction-server/blob/0dd00179907c/go-client/LICENSE")))
                .type(Type.TEST).build();
        assertThat(dependencies, contains(dependency1, dependency2));
    }

    private List<AnalyzedSource> analyze(final ProjectKeeperConfig config) {
        final GolangSourceAnalyzer analyzer = new GolangSourceAnalyzer(config);
        return analyzer.analyze(this.projectDir, config.getSources());
    }
}