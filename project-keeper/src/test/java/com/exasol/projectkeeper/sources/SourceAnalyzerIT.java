package com.exasol.projectkeeper.sources;

import static com.exasol.projectkeeper.ProjectKeeperModule.JAR_ARTIFACT;
import static com.exasol.projectkeeper.config.ProjectKeeperConfig.SourceType.MAVEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.TestEnvBuilder;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.validators.TestMavenModel;

@Tag("integration")
class SourceAnalyzerIT {
    private static final Set<ProjectKeeperModule> MODULES = Set.of(JAR_ARTIFACT);
    private static final MavenIntegrationTestEnvironment TEST_ENV = TestEnvBuilder.getTestEnv();
    @TempDir
    Path tempDir;

    @Test
    void testAnalyze() throws IOException, GitAPIException {
        Git.init().setDirectory(this.tempDir.toFile()).call().close();
        new TestMavenModel().writeAsPomToProject(this.tempDir);
        final Path path = this.tempDir.resolve("pom.xml");
        final List<AnalyzedSource> result = SourceAnalyzer
                .create(null, TEST_ENV.getLocalMavenRepository(), TestEnvBuilder.CURRENT_VERSION)
                .analyze(this.tempDir, List.of(ProjectKeeperConfig.Source.builder().modules(MODULES).path(path)
                        .advertise(true).type(MAVEN).build()));
        final AnalyzedMavenSource first = (AnalyzedMavenSource) result.get(0);
        assertAll(//
                () -> assertThat(result, Matchers.hasSize(1)), () -> assertThat(first.getPath(), equalTo(path)),
                () -> assertThat(first.getModules(), equalTo(MODULES)),
                () -> assertThat(first.isAdvertise(), equalTo(true)),
                () -> assertThat(first.getArtifactId(), equalTo(TestMavenModel.PROJECT_ARTIFACT_ID)),
                () -> assertThat(first.getProjectName(), equalTo(TestMavenModel.PROJECT_NAME)),
                () -> assertThat(first.getVersion(), equalTo(TestMavenModel.PROJECT_VERSION)),
                () -> assertThat(first.getDependencies(), not(nullValue())),
                () -> assertThat(first.getDependencyChanges(), not(nullValue()))//
        );
    }
}