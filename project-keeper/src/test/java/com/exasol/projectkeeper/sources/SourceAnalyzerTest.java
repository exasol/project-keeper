package com.exasol.projectkeeper.sources;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.shared.config.Source;
import com.exasol.projectkeeper.shared.config.SourceType;
import com.exasol.projectkeeper.sources.analyze.LanguageSpecificSourceAnalyzer;

@ExtendWith(MockitoExtension.class)
class SourceAnalyzerTest {

    private static final Path PROJECT_DIR = Path.of("project-dir");
    @Mock
    private LanguageSpecificSourceAnalyzer golangAnalyzerMock;
    @Mock
    private LanguageSpecificSourceAnalyzer mavenAnalyzerMock;

    @BeforeEach
    void setup() {
        lenient().when(this.golangAnalyzerMock.analyze(eq(PROJECT_DIR), anyList())).thenAnswer(this::analyzerAnswer);
        lenient().when(this.mavenAnalyzerMock.analyze(eq(PROJECT_DIR), anyList())).thenAnswer(this::analyzerAnswer);
    }

    private List<AnalyzedSource> analyzerAnswer(final InvocationOnMock invocation) {
        @SuppressWarnings("unchecked")
        final List<Source> sources = invocation.getArgument(1, List.class);
        return sources.stream().map(Source.class::cast) //
                .map(this::analyzedSource) //
                .toList();
    }

    @Test
    void emptySourceList() {
        final SourceAnalyzer analyzer = createWithAllAnalyzers();
        assertThat(analyzer.analyze(PROJECT_DIR, emptyList()), hasSize(0));
    }

    @Test
    void duplicatePathsThrowsException() {
        final SourceAnalyzer analyzer = createWithAllAnalyzers();
        final List<Source> sources = List.of(source("path", SourceType.MAVEN), source("path", SourceType.GOLANG));
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> analyzer.analyze(PROJECT_DIR, sources));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-149: Sources use duplicate paths [path]. Make sure that sources in .project-keeper.yml have unique paths."));
    }

    @Test
    void sourceNotFound() {
        final SourceAnalyzer analyzer = createWithAllAnalyzers();
        final List<Source> sources = List.of(source("path", SourceType.MAVEN));
        when(this.mavenAnalyzerMock.analyze(any(), any())).thenReturn(List.of(analyzedSource(Path.of("wrong path"))));
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> analyzer.analyze(PROJECT_DIR, sources));
        assertThat(exception.getMessage(),
                startsWith("E-PK-CORE-150: Did not find analyzed source for path 'path', all analyzed sources:"));
    }

    @RepeatedTest(10)
    void analyzeOrderIsStable() {
        final SourceAnalyzer analyzer = createWithAllAnalyzers();
        final List<AnalyzedSource> analyzedSources = analyzer.analyze(PROJECT_DIR,
                List.of(source("maven1", SourceType.MAVEN), //
                        source("go1", SourceType.GOLANG), //
                        source("maven2", SourceType.MAVEN), //
                        source("go2", SourceType.GOLANG)));
        final List<String> analyzedPaths = analyzedSources.stream().map(source -> source.getPath().toString())
                .toList();
        assertThat(analyzedPaths, contains("maven1", "go1", "maven2", "go2"));
    }

    private AnalyzedSource analyzedSource(final Source originalSource) {
        return analyzedSource(originalSource.getPath());
    }

    private AnalyzedSource analyzedSource(final Path path) {
        return AnalyzedMavenSource.builder().path(path).build();
    }

    private Source source(final String path, final SourceType type) {
        return Source.builder().path(Path.of(path)).type(type).build();
    }

    private SourceAnalyzer createWithAllAnalyzers() {
        return new SourceAnalyzer(
                Map.of(SourceType.GOLANG, this.golangAnalyzerMock, SourceType.MAVEN, this.mavenAnalyzerMock));
    }
}
