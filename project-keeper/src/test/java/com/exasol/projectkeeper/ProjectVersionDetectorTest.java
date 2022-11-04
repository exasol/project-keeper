package com.exasol.projectkeeper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.sources.AnalyzedSource;

class ProjectVersionDetectorTest {

    @Test
    void testGetVersionForSingleModule() {
        final AnalyzedSource source = mockSource("1.2.3");
        final String result = new ProjectVersionDetector().detectVersion(ProjectKeeperConfig.builder().build(),
                List.of(source));
        assertThat(result, equalTo("1.2.3"));
    }

    @Test
    void testGetExplicitVersion() {
        final AnalyzedSource source = mockSource("1.2.3");
        final String result = new ProjectVersionDetector().detectVersion(
                ProjectKeeperConfig.builder().versionConfig(new ProjectKeeperConfig.FixedVersion("3.2.1")).build(),
                List.of(source));
        assertThat(result, equalTo("3.2.1"));
    }

    @ParameterizedTest
    @CsvSource({ "sub1/pom.xml, 1.0.0", "sub2/pom.xml, 2.0.0", })
    void testGetVersionFromSource(final Path path, final String expectedVersion) {
        final AnalyzedSource source1 = mockSource("1.0.0", Path.of("sub1/pom.xml"));
        final AnalyzedSource source2 = mockSource("2.0.0", Path.of("sub2/pom.xml"));
        final String result = new ProjectVersionDetector().detectVersion(
                ProjectKeeperConfig.builder().versionConfig(new ProjectKeeperConfig.VersionFromSource(path)).build(),
                List.of(source1, source2));
        assertThat(result, equalTo(expectedVersion));
    }

    @Test
    void testNoVersionFound() {
        final List<AnalyzedSource> sources = Collections.emptyList();
        final ProjectKeeperConfig config = ProjectKeeperConfig.builder().build();
        final ProjectVersionDetector versionDetector = new ProjectVersionDetector();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> versionDetector.detectVersion(config, sources));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-116: Failed to detect overall project version. Project-keeper can only auto-detect the project version for projects with exactly one source. Please configure how to detect the version in your project-keeper config."));
    }

    @Test
    void testSpecifiedSourceHasNoVersion() {
        final List<AnalyzedSource> sources = List.of(mockSource(null, Path.of("pom.xml")));
        final ProjectKeeperConfig config = ProjectKeeperConfig.builder()
                .versionConfig(new ProjectKeeperConfig.VersionFromSource(Path.of("pom.xml"))).build();
        final ProjectVersionDetector versionDetector = new ProjectVersionDetector();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> versionDetector.detectVersion(config, sources));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-115: Failed to detect overall project version. The specified source with path 'pom.xml' did not provide a version. Please specify a different source to read from or set an explicit version in your project-keeper config."));
    }

    @Test
    void testSpecifiedSourceNotFound() {
        final List<AnalyzedSource> sources = Collections.emptyList();
        final ProjectKeeperConfig config = ProjectKeeperConfig.builder()
                .versionConfig(new ProjectKeeperConfig.VersionFromSource(Path.of("pom.xml"))).build();
        final ProjectVersionDetector versionDetector = new ProjectVersionDetector();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> versionDetector.detectVersion(config, sources));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-114: Failed to detect overall project version. Could not find a source with specified path 'pom.xml'. Please make sure that you defined a source with exactly the same path. The following sources are defined in the config: []."));
    }

    private AnalyzedSource mockSource(final String version) {
        final AnalyzedSource source = mock(AnalyzedSource.class);
        when(source.getVersion()).thenReturn(version);
        return source;
    }

    private AnalyzedSource mockSource(final String version, final Path path) {
        final AnalyzedSource source = mockSource(version);
        when(source.getPath()).thenReturn(path);
        return source;
    }
}