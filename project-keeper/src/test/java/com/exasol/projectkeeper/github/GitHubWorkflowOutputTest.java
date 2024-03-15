package com.exasol.projectkeeper.github;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.lang3.exception.UncheckedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.changesfile.*;

@ExtendWith(MockitoExtension.class)
class GitHubWorkflowOutputTest {

    private static final String PROJECT_VERSION = "1.2.3";
    @Mock
    OutputPublisherFactory publisherFactoryMock;
    @Mock
    WorkflowOutput publisherMock;
    @Mock
    ChangesFileIO changesFileIOMock;
    @TempDir
    Path projectDir;

    // [utest->dsn~verify-release-mode.output-parameters.project-version~1]
    @Test
    void outputVersion() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder());
        verify(this.publisherMock).publish("version", PROJECT_VERSION);
    }

    @Test
    void changesFileMissing() {
        testee(ProjectKeeperConfig.builder()).provide();
        verify(this.publisherMock, never()).publish(eq("release-title"), any());
        verify(this.publisherMock, never()).publish(eq("release-notes"), any());
    }

    // [utest->dsn~verify-release-mode.output-parameters.release-title~1]
    @Test
    void releaseTitle() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder().codeName("code name"));
        verify(this.publisherMock).publish("release-title", PROJECT_VERSION + " code name");
    }

    // [utest->dsn~verify-release-mode.output-parameters.release-notes~1]
    @Test
    void releaseNotes() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder()
                .summary(ChangesFileSection.builder("## Summary").addLine("summary content").build())
                .addSection(ChangesFileSection.builder("## Features").addLine("feature content").build())
                .dependencyChangeSection(
                        ChangesFileSection.builder("## Dependency Updates").addLine("dependency content").build()));
        verify(this.publisherMock).publish("release-notes", """
                summary content
                ## Features
                feature content
                ## Dependency Updates
                dependency content""");
    }

    // [utest->dsn~verify-release-mode.output-parameters.release-artifacts~1]
    @Test
    void releaseArtifactsNoMavenSource() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder());
        verify(this.publisherMock).publish("release-artifacts", "");
    }

    @Test
    void releaseArtifactsMavenSourceWithoutArtifact() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder(), AnalyzedMavenSource.builder().build());
        verify(this.publisherMock).publish("release-artifacts", "");
    }

    // [utest->dsn~customize-release-artifacts-hard-coded~0]
    @Test
    void releaseArtifactsErrorCodeReport() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder(),
                AnalyzedMavenSource.builder().isRootProject(true).build());
        verify(this.publisherMock).publish("release-artifacts",
                this.projectDir.resolve("target/error_code_report.json").toString());
    }

    @Test
    void releaseArtifactsMavenSourceWithArtifact() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder(), AnalyzedMavenSource.builder()
                .path(this.projectDir.resolve("project-dir/pom.xml")).releaseArtifactName("my-project.jar").build());
        verify(this.publisherMock).publish("release-artifacts",
                this.projectDir.resolve("project-dir/target/my-project.jar").toString());
    }

    @Test
    void releaseArtifactsMultipleArtifact() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder(),
                AnalyzedMavenSource.builder().path(this.projectDir.resolve("pom.xml"))
                        .releaseArtifactName("my-project1.jar").isRootProject(true).build(),
                AnalyzedMavenSource.builder().path(this.projectDir.resolve("module1/pom.xml"))
                        .releaseArtifactName("my-project2.jar").build());
        verify(this.publisherMock).publish("release-artifacts",
                this.projectDir.resolve("target/my-project1.jar").toString() + "\n"
                        + this.projectDir.resolve("module1/target/my-project2.jar").toString() + "\n"
                        + this.projectDir.resolve("target/error_code_report.json").toString());
    }

    @Test
    void closePublisher() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder());
        verify(this.publisherMock).close();
    }

    private void publish(final ProjectKeeperConfig.Builder configBuilder, final ChangesFile.Builder changesFileBuilder,
            final AnalyzedSource... sources) {
        simulateChangesFile(changesFileBuilder);
        testee(configBuilder, sources).provide();
    }

    private void simulateChangesFile(final ChangesFile.Builder changesFileBuilder) {
        final Path path = this.projectDir.resolve("doc/changes/changes_1.2.3.md");
        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        } catch (final IOException exception) {
            throw new UncheckedException(exception);
        }
        when(this.changesFileIOMock.read(path)).thenReturn(changesFileBuilder.build());
    }

    private GitHubWorkflowOutput testee(final ProjectKeeperConfig.Builder configBuilder,
            final AnalyzedSource... sources) {
        when(this.publisherFactoryMock.create()).thenReturn(this.publisherMock);
        return new GitHubWorkflowOutput(configBuilder.build(), this.projectDir, PROJECT_VERSION, asList(sources),
                this.publisherFactoryMock, this.changesFileIOMock);
    }
}
