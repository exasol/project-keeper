package com.exasol.projectkeeper.validators;

import static com.exasol.projectkeeper.validators.ReleaseConfigValidator.RELEASE_MAVEN;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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

    @TempDir
    private Path tempDir;

    @Test
    void testAcceptNoReleaseConfig() {
        final List<ValidationFinding> findings = testee(null, emptyList()).validate();
        assertThat(findings, emptyIterable());
    }

    @Test
    void testAcceptMatchingConfigs() {
        assertNoFinding(RELEASE_MAVEN, ProjectKeeperModule.MAVEN_CENTRAL);
        assertNoFinding(RELEASE_OTHER, ProjectKeeperModule.JAR_ARTIFACT);
    }

    private void assertNoFinding(final String releasePlatform, final ProjectKeeperModule pkModule) {
        final ReleaseConfigValidator testee = testee(releasePlatform, pkSources(pkModule));
        assertThat(testee.validate(), emptyIterable());
    }

    @Test
    void testFinding165() {
        assertFinding(RELEASE_OTHER, ProjectKeeperModule.MAVEN_CENTRAL, startsWith("E-PK-CORE-165"));
    }

    @Test
    void testFinding166() {
        assertFinding(RELEASE_MAVEN, null, startsWith("E-PK-CORE-166"));
    }

    private void assertFinding(final String releasePlatform, final ProjectKeeperModule pkModule,
            final Matcher<String> matcher) {
        final ReleaseConfigValidator testee = testee(releasePlatform, pkSources(pkModule));
        final List<ValidationFinding> actual = testee.validate();
        assertThat(actual, iterableWithSize(1));
        final SimpleValidationFinding finding = (SimpleValidationFinding) actual.get(0);
        System.out.println(finding.getMessage());
        assertThat(finding.getMessage(), matcher);
    }

    private List<AnalyzedSource> pkSources(final ProjectKeeperModule pkModule) {
        final List<AnalyzedSource> result = new ArrayList<>();
        final AnalyzedMavenSource other = mock(AnalyzedMavenSource.class);
        when(other.getModules()).thenReturn(Set.of(ProjectKeeperModule.DEFAULT));
        result.add(other);
        if (pkModule != null) {
            final AnalyzedMavenSource source = mock(AnalyzedMavenSource.class);
            when(source.getModules()).thenReturn(Set.of(pkModule));
            result.add(source);
        }
        return result;
    }

    private ReleaseConfigValidator testee(final String releasePlatform, final List<AnalyzedSource> analyzedSources) {
        if (releasePlatform != null) {
            writeReleaseConfig(releasePlatform);
        }
        return new ReleaseConfigValidator(this.tempDir, analyzedSources);
    }

    private void writeReleaseConfig(final String platform) {
        try {
            final Path file = this.tempDir.resolve(ReleaseConfigValidator.RELEASE_CONFIG);
            Files.writeString(file, "release-platforms:\n" + "  - " + platform);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }
}
