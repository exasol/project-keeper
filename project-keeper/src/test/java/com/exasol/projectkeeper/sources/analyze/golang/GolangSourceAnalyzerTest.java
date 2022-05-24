package com.exasol.projectkeeper.sources.analyze.golang;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.SourceType.GOLANG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Source;

class GolangSourceAnalyzerTest {

    @TempDir
    Path tempDir;

    @Test
    void testInvalidPath() {
        final List<Source> sources = List.of(Source.builder().type(GOLANG).path(this.tempDir).build());
        final GolangSourceAnalyzer analyzer = new GolangSourceAnalyzer(
                ProjectKeeperConfig.builder().sources(sources).build());
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyzer.analyze(this.tempDir, sources));
        assertThat(exception.getMessage(), matchesPattern(
                "\\QE-PK-CORE-133: Invalid path \\E.*\\Q for go source. The path must point to a \"go.mod\" file.\\E"));
    }
}