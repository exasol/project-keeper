package com.exasol.projectkeeper.sources.analyze;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.config.Source;
import com.exasol.projectkeeper.shared.config.SourceType;
import com.exasol.projectkeeper.sources.AnalyzedSource;

class MavenSourceAnalyzerTest {
    private static final Path PROJECT_DIR = Path.of("dir");
    private static final String OWN_VERSION = "own-version";

    @Test
    void analyzingEmptySourceListReturnsEmptyList() {
        final List<AnalyzedSource> analyzedSources = analyze(null, emptyList());
        assertThat(analyzedSources, hasSize(0));
    }

    @Test
    void analyzingSourcesWithInvalidOwnVersionFails() {
        final List<Source> mavenSources = createMavenSources();
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> analyze(OWN_VERSION, mavenSources));
        assertThat(exception.getMessage(),
                allOf(containsString("E-PK-CORE-78: Failed to run command"),
                        containsString("[ERROR] Plugin com.exasol:project-keeper-java-project-crawler:" + OWN_VERSION
                                + " or one of its dependencies could not be resolved")));
    }

    @Test
    void analyzingNonMavenSourceFails() {
        final List<Source> goSources = List.of(Source.builder().type(SourceType.GOLANG).build());
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> analyze(OWN_VERSION, goSources));
        assertThat(exception.getMessage(),
                equalTo("F-PK-CORE-93: Analyzing of GOLANG is not supported by MavenSourceAnalyzer"
                        + " This is an internal error that should not happen. Please report it by opening a GitHub issue."));
    }

    private List<Source> createMavenSources() {
        return List.of(Source.builder().type(SourceType.MAVEN).path(PROJECT_DIR.resolve("pom.xml")).build());
    }

    private List<AnalyzedSource> analyze(final String ownVersion, final List<Source> sources) {
        return new MavenSourceAnalyzer(null, ownVersion).analyze(PROJECT_DIR, sources);
    }
}
