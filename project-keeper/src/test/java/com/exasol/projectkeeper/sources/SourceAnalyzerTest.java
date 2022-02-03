package com.exasol.projectkeeper.sources;

import static com.exasol.projectkeeper.ProjectKeeperModule.JAR_ARTIFACT;
import static com.exasol.projectkeeper.config.ProjectKeeperConfig.SourceType.MAVEN;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.validators.TestMavenModel;

class SourceAnalyzerTest {
    private static final Set<ProjectKeeperModule> MODULES = Set.of(JAR_ARTIFACT);
    @TempDir
    Path tempDir;

    @Test
    void testAnalyze() throws IOException {
        new TestMavenModel().writeAsPomToProject(this.tempDir);
        final Path path = this.tempDir.resolve("pom.xml");
        final List<AnalyzedSource> result = new SourceAnalyzer().analyze(this.tempDir, List.of(
                ProjectKeeperConfig.Source.builder().modules(MODULES).path(path).advertise(true).type(MAVEN).build()));
        assertThat(result, Matchers.contains(new AnalyzedMavenSource(path, MODULES, true,
                TestMavenModel.PROJECT_ARTIFACT_ID, TestMavenModel.PROJECT_NAME, true)));
    }
}