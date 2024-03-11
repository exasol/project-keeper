package com.exasol.projectkeeper.githuboutput;

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
        verify(publisherMock).publish("version", PROJECT_VERSION);
    }

    @Test
    void changesFileMissing() {
        testee(ProjectKeeperConfig.builder()).provide();
        verify(publisherMock, never()).publish(eq("release-title"), any());
        verify(publisherMock, never()).publish(eq("release-notes"), any());
    }

    // [utest->dsn~verify-release-mode.output-parameters.code-name~1]
    @Test
    void releaseTitle() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder().codeName("code name"));
        verify(publisherMock).publish("release-title", "code name");
    }

    // [utest->dsn~verify-release-mode.output-parameters.release-notes~1]
    @Test
    void releaseNotes() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder()
                .summary(ChangesFileSection.builder("## Summary").addLine("summary content").build())
                .addSection(ChangesFileSection.builder("## Features").addLine("feature content").build())
                .dependencyChangeSection(
                        ChangesFileSection.builder("## Dependency Updates").addLine("dependency content").build()));
        verify(publisherMock).publish("release-notes", """
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
        verify(publisherMock).publish("release-artifacts", "");
    }

    @Test
    void releaseArtifactsMavenSourceWithoutArtifact() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder(), AnalyzedMavenSource.builder().build());
        verify(publisherMock).publish("release-artifacts", "");
    }

    // [utest->dsn~customize-release-artifacts-hard-coded~0]
    @Test
    void releaseArtifactsErrorCodeReport() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder(),
                AnalyzedMavenSource.builder().isRootProject(true).build());
        verify(publisherMock).publish("release-artifacts",
                projectDir.resolve("target/error_code_report.json").toString());
    }

    @Test
    void releaseArtifactsMavenSourceWithArtifact() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder(), AnalyzedMavenSource.builder()
                .path(projectDir.resolve("project-dir/pom.xml")).releaseArtifactName("my-project.jar").build());
        verify(publisherMock).publish("release-artifacts",
                projectDir.resolve("project-dir/target/my-project.jar").toString());
    }

    @Test
    void releaseArtifactsMultipleArtifact() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder(),
                AnalyzedMavenSource.builder().path(projectDir.resolve("pom.xml")).releaseArtifactName("my-project1.jar")
                        .isRootProject(true).build(),
                AnalyzedMavenSource.builder().path(projectDir.resolve("module1/pom.xml"))
                        .releaseArtifactName("my-project2.jar").build());
        verify(publisherMock).publish("release-artifacts",
                projectDir.resolve("target/my-project1.jar").toString() + "\n"
                        + projectDir.resolve("module1/target/my-project2.jar").toString() + "\n"
                        + projectDir.resolve("target/error_code_report.json").toString());
    }

    @Test
    void closePublisher() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder());
        verify(publisherMock).close();
    }

    private void publish(final ProjectKeeperConfig.Builder configBuilder, final ChangesFile.Builder changesFileBuilder,
            final AnalyzedSource... sources) {
        simulateChangesFile(changesFileBuilder);
        testee(configBuilder, sources).provide();
    }

    private void simulateChangesFile(final ChangesFile.Builder changesFileBuilder) {
        final Path path = projectDir.resolve("doc/changes/changes_1.2.3.md");
        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        } catch (final IOException exception) {
            throw new UncheckedException(exception);
        }
        when(changesFileIOMock.read(path)).thenReturn(changesFileBuilder.build());
    }

    private GitHubWorkflowOutput testee(final ProjectKeeperConfig.Builder configBuilder,
            final AnalyzedSource... sources) {
        when(publisherFactoryMock.create()).thenReturn(publisherMock);
        return new GitHubWorkflowOutput(configBuilder.build(), projectDir, PROJECT_VERSION, asList(sources),
                publisherFactoryMock, changesFileIOMock);
    }
}
