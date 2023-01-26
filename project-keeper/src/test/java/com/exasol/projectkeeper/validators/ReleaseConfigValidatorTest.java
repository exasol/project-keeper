package com.exasol.projectkeeper.validators;

import static com.exasol.projectkeeper.validators.ReleaseConfigValidator.RELEASE_MAVEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

class ReleaseConfigValidatorTest {

    private static final String RELEASE_OTHER = "GitHub";
    private static final String NO_PLATFORMS = null;
    private static final Path NO_RELEASE_CONFING = null;
    private static final ProjectKeeperModule PK_MAVEN = ProjectKeeperModule.MAVEN_CENTRAL;
    private static final ProjectKeeperModule PK_OTHER = ProjectKeeperModule.JAR_ARTIFACT;

    @TempDir
    private Path tempDir;

    @Test
    void testIoError() throws IOException {
        Files.createDirectory(this.tempDir.resolve("release_config.yml"));
        final ReleaseConfigValidator testee = testee(NO_RELEASE_CONFING, pkSources(PK_OTHER));
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> testee.validate());
        assertThat(exception.getMessage(), startsWith("E-PK-CORE-167"));
    }

    @Test
    void testNoReleaseConfig() {
        assertNoFinding(NO_RELEASE_CONFING, PK_OTHER);
        assertNoFinding(NO_RELEASE_CONFING, PK_MAVEN);
    }

    @Test
    void testAcceptMatchingConfigs() {
        assertNoFinding(releaseConfig(RELEASE_MAVEN), PK_MAVEN);
        assertNoFinding(releaseConfig(RELEASE_OTHER), PK_OTHER);
        assertNoFinding(releaseConfig(NO_PLATFORMS), PK_OTHER);
    }

    private void assertNoFinding(final Path releaseConfig, final ProjectKeeperModule pkModule) {
        final ReleaseConfigValidator testee = testee(releaseConfig, pkSources(pkModule));
        assertThat(testee.validate(), emptyIterable());
    }

    @Test
    void testFinding165() {
        assertFinding(releaseConfig(RELEASE_OTHER), PK_MAVEN, startsWith("E-PK-CORE-165"));
        assertFinding(releaseConfig(NO_PLATFORMS), PK_MAVEN, startsWith("E-PK-CORE-165"));
    }

    @Test
    void testFinding166() {
        assertFinding(releaseConfig(RELEASE_MAVEN), PK_OTHER, startsWith("E-PK-CORE-166"));
    }

    private void assertFinding(final Path releaseConfig, final ProjectKeeperModule pkModule,
            final Matcher<String> matcher) {
        final ReleaseConfigValidator testee = testee(this.tempDir, pkSources(pkModule));
        final List<ValidationFinding> actual = testee.validate();
        assertThat(actual, iterableWithSize(1));
        final SimpleValidationFinding finding = (SimpleValidationFinding) actual.get(0);
        assertThat(finding.getMessage(), matcher);
    }

    private List<AnalyzedSource> pkSources(final ProjectKeeperModule pkModule) {
        final List<AnalyzedSource> result = new ArrayList<>();
        final AnalyzedMavenSource other = mock(AnalyzedMavenSource.class);
        when(other.getModules()).thenReturn(Set.of(ProjectKeeperModule.DEFAULT));
        result.add(other);
        result.add(mock(AnalyzedSource.class));
        if (pkModule != null) {
            final AnalyzedMavenSource source = mock(AnalyzedMavenSource.class);
            when(source.getModules()).thenReturn(Set.of(pkModule));
            result.add(source);
        }
        return result;
    }

    private ReleaseConfigValidator testee(final Path releaseConfig, final List<AnalyzedSource> analyzedSources) {
        return new ReleaseConfigValidator(this.tempDir, analyzedSources);
    }

    private Path releaseConfig(final String platform) {
        try {
            final Path file = this.tempDir.resolve(ReleaseConfigValidator.RELEASE_CONFIG);
            String content = "language: Java\n";
            if (platform != null) {
                content += "release-platforms:\n" + "  - " + platform;
            }
            return Files.writeString(file, content);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }
}
