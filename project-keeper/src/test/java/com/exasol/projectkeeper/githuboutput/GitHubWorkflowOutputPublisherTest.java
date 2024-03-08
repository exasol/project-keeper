package com.exasol.projectkeeper.githuboutput;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.changesfile.*;

@ExtendWith(MockitoExtension.class)
class GitHubWorkflowOutputPublisherTest {

    private static final Path PROJECT_DIR = Path.of("project-dir");
    private static final String PROJECT_VERSION = "1.2.3";
    @Mock
    OutputPublisherFactory publisherFactoryMock;
    @Mock
    OutputPublisher publisherMock;
    @Mock
    ChangesFileIO changesFileIOMock;

    // [utest->dsn~verify-release-mode.output-parameters.project-version~1]
    @Test
    void outputVersion() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder());
        verify(publisherMock).publish("version", PROJECT_VERSION);
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
        verify(publisherMock).publish("release-artifacts", "project-dir/target/error_code_report.json");
    }

    @Test
    void releaseArtifactsMavenSourceWithArtifact() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder(),
                AnalyzedMavenSource.builder().releaseArtifactName("my-project.jar").build());
        verify(publisherMock).publish("release-artifacts", "project-dir/target/my-project.jar");
    }

    @Test
    void releaseArtifactsMultipleArtifact() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder(),
                AnalyzedMavenSource.builder().releaseArtifactName("my-project1.jar").isRootProject(true).build(),
                AnalyzedMavenSource.builder().releaseArtifactName("my-project2.jar").build());
        verify(publisherMock).publish("release-artifacts", """
                project-dir/target/my-project1.jar
                project-dir/target/my-project2.jar
                project-dir/target/error_code_report.json""");
    }

    @Test
    void closePublisher() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder());
        verify(publisherMock).close();
    }

    private void publish(final ProjectKeeperConfig.Builder configBuilder, final ChangesFile.Builder changesFileBuilder,
            final AnalyzedSource... sources) {
        simulateChangesFile(changesFileBuilder);
        testee(configBuilder, sources).publish();
    }

    private void simulateChangesFile(final ChangesFile.Builder changesFileBuilder) {
        when(changesFileIOMock.read(Path.of("project-dir/doc/changes/changes_1.2.3.md")))
                .thenReturn(changesFileBuilder.build());
    }

    private GitHubWorkflowOutputPublisher testee(final ProjectKeeperConfig.Builder configBuilder,
            final AnalyzedSource... sources) {
        when(publisherFactoryMock.create()).thenReturn(publisherMock);
        return new GitHubWorkflowOutputPublisher(configBuilder.build(), PROJECT_DIR, PROJECT_VERSION, asList(sources),
                publisherFactoryMock, changesFileIOMock);
    }
}
