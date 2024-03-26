package com.exasol.projectkeeper.github;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.UncheckedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.shared.config.*;
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

    // [utest->dsn~verify-release-mode.output-parameters.release-tag~1]
    @Test
    void outputVersion() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder());
        verify(this.publisherMock).publish("release-tag", PROJECT_VERSION);
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
        verifyReleaseArtifacts();
    }

    @Test
    void releaseArtifactsMavenSourceWithoutArtifact() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder(), AnalyzedMavenSource.builder().build());
        verifyReleaseArtifacts();
    }

    // [utest->dsn~customize-release-artifacts-hard-coded~0]
    @Test
    void releaseArtifactsErrorCodeReport() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder(),
                AnalyzedMavenSource.builder().isRootProject(true).build());
        verifyReleaseArtifacts("target/error_code_report.json");
    }

    @Test
    void releaseArtifactsMavenSourceWithArtifact() {
        publish(ProjectKeeperConfig.builder(), ChangesFile.builder(), AnalyzedMavenSource.builder()
                .path(this.projectDir.resolve("project-dir/pom.xml")).releaseArtifactName("my-project.jar").build());
        verifyReleaseArtifacts("project-dir/target/my-project.jar");
    }

    // [utest->dsn~customize-release-artifacts-custom~0]
    @ParameterizedTest
    @CsvSource({ "pom.xml, target/custom.jar, target/custom.jar", //
            "go.mod, build/executable, build/executable", //
            "package.json, build/ext.js, build/ext.js", //
            "subModule/go.mod, build/executable, subModule/build/executable", //
            "pom.xml, target/custom-$version.jar, target/custom-$version.jar",
            "pom.xml, target/custom-${version}.jar, target/custom-1.2.3.jar",
            "subModule/pom.xml, target/custom-${version}.jar, subModule/target/custom-1.2.3.jar" })
    void releaseArtifactCustom(final String sourcePath, final String artifactPath, final String expectedPath) {
        publish(ProjectKeeperConfig.builder().sources(List.of(
                Source.builder().path(Path.of(sourcePath)).releaseArtifacts(List.of(Path.of(artifactPath))).build())),
                ChangesFile.builder());
        verifyReleaseArtifacts(expectedPath);
    }

    @Test
    void releaseArtifactsMultipleArtifact() {
        publish(ProjectKeeperConfig.builder()
                .sources(List.of(
                        Source.builder().path(Path.of("pom.xml"))
                                .releaseArtifacts(List.of(Path.of("target/custom-${version}.jar"))).build(),
                        Source.builder().path(Path.of("subModule/package.json"))
                                .releaseArtifacts(List.of(Path.of("build/custom-extension.js"))).build())),
                ChangesFile.builder(),
                AnalyzedMavenSource.builder().path(this.projectDir.resolve("pom.xml"))
                        .releaseArtifactName("my-project1.jar").isRootProject(true).build(),
                AnalyzedMavenSource.builder().path(this.projectDir.resolve("module1/pom.xml"))
                        .releaseArtifactName("my-project2.jar").build());
        verifyReleaseArtifacts("target/my-project1.jar", "module1/target/my-project2.jar",
                "target/error_code_report.json", "target/custom-" + PROJECT_VERSION + ".jar",
                "subModule/build/custom-extension.js");
    }

    private void verifyReleaseArtifacts(final String... expectedPaths) {
        final String expected = Arrays.stream(expectedPaths) //
                .map(path -> this.projectDir.resolve(path)) //
                .map(Path::toString) //
                .collect(joining("\n"));
        verify(this.publisherMock).publish("release-artifacts", expected);
    }

    @Test
    void additionalReleaseTagsEmptyForMissingProject() {
        assertAdditionalReleaseTags(PROJECT_VERSION, "");
    }

    @Test
    void additionalReleaseTagsEmptyForJavaProject() {
        assertAdditionalReleaseTags(PROJECT_VERSION, "", mavenSource("pom.xml"));
    }

    @Test
    void additionalReleaseTagsEmptyForMultiModuleJavaProject() {
        assertAdditionalReleaseTags(PROJECT_VERSION, "", mavenSource("pom.xml"), mavenSource("subModule/pom.xml"));
    }

    @Test
    void additionalReleaseTagsEmptyForNonRootJavaProject() {
        assertAdditionalReleaseTags(PROJECT_VERSION, "", mavenSource("subDir/pom.xml"));
    }

    @Test
    void additionalReleaseTagsEmptyForRootGoProject() {
        assertAdditionalReleaseTags("v" + PROJECT_VERSION, "", goSource("go.mod"));
    }

    @Test
    void additionalReleaseTagsEmptyForRootGoProjectWithMavenModule() {
        assertAdditionalReleaseTags("v" + PROJECT_VERSION, "", goSource("go.mod"), mavenSource("pom.xml"));
    }

    @Test
    void additionalReleaseTagsEmptyForRootGoProjectWithMavenSubModule() {
        assertAdditionalReleaseTags("v" + PROJECT_VERSION, "", goSource("go.mod"), mavenSource("subModule/pom.xml"));
    }

    // [utest->dsn~release-workflow.create-golang-tags~1]
    @Test
    void additionalReleaseTagsEmptyForRootGoProjectWithGoSubModule() {
        assertAdditionalReleaseTags("v" + PROJECT_VERSION, "subModule/v1.2.3", goSource("go.mod"),
                goSource("subModule/go.mod"));
    }

    @Test
    void additionalReleaseTagsForNonRootGoProjectWithMaven() {
        assertAdditionalReleaseTags(PROJECT_VERSION, "subModule/v1.2.3", goSource("subModule/go.mod"),
                mavenSource("pom.xml"));
    }

    @Test
    void multipleAdditionalReleaseTags() {
        assertAdditionalReleaseTags(PROJECT_VERSION, "subModuleA/v1.2.3\nsubModuleB/v1.2.3",
                goSource("subModuleA/go.mod"), mavenSource("pom.xml"), goSource("subModuleB/go.mod"));
    }

    private void assertAdditionalReleaseTags(final String expectedTag, final String expectedAdditionalTags,
            final Source... sources) {
        publish(ProjectKeeperConfig.builder().sources(asList(sources)), ChangesFile.builder());
        verify(this.publisherMock).publish("release-tag", expectedTag);
        verify(this.publisherMock).publish("additional-release-tags", expectedAdditionalTags);
    }

    private Source mavenSource(final String path) {
        return Source.builder().type(SourceType.MAVEN).path(Path.of(path)).build();
    }

    private Source goSource(final String path) {
        return Source.builder().type(SourceType.GOLANG).path(Path.of(path)).build();
    }

    @Test
    void additionalReleaseTagsEmptyForMissingProject() {
        assertAdditionalReleaseTags(PROJECT_VERSION, "");
    }

    @Test
    void additionalReleaseTagsEmptyForJavaProject() {
        assertAdditionalReleaseTags(PROJECT_VERSION, "", mavenSource("pom.xml"));
    }

    @Test
    void additionalReleaseTagsEmptyForMultiModuleJavaProject() {
        assertAdditionalReleaseTags(PROJECT_VERSION, "", mavenSource("pom.xml"), mavenSource("subModule/pom.xml"));
    }

    @Test
    void additionalReleaseTagsEmptyForNonRootJavaProject() {
        assertAdditionalReleaseTags(PROJECT_VERSION, "", mavenSource("subDir/pom.xml"));
    }

    @Test
    void additionalReleaseTagsEmptyForRootGoProject() {
        assertAdditionalReleaseTags("v" + PROJECT_VERSION, "", goSource("go.mod"));
    }

    @Test
    void additionalReleaseTagsEmptyForRootGoProjectWithMavenModule() {
        assertAdditionalReleaseTags("v" + PROJECT_VERSION, "", goSource("go.mod"), mavenSource("pom.xml"));
    }

    @Test
    void additionalReleaseTagsEmptyForRootGoProjectWithMavenSubModule() {
        assertAdditionalReleaseTags("v" + PROJECT_VERSION, "", goSource("go.mod"), mavenSource("subModule/pom.xml"));
    }

    // [utest->dsn~release-workflow.create-golang-tags~1]
    @Test
    void additionalReleaseTagsEmptyForRootGoProjectWithGoSubModule() {
        assertAdditionalReleaseTags("v" + PROJECT_VERSION, "subModule/v1.2.3", goSource("go.mod"),
                goSource("subModule/go.mod"));
    }

    @Test
    void additionalReleaseTagsForNonRootGoProjectWithMaven() {
        assertAdditionalReleaseTags(PROJECT_VERSION, "subModule/v1.2.3", goSource("subModule/go.mod"),
                mavenSource("pom.xml"));
    }

    @Test
    void multipleAdditionalReleaseTags() {
        assertAdditionalReleaseTags(PROJECT_VERSION, "subModuleA/v1.2.3\nsubModuleB/v1.2.3",
                goSource("subModuleA/go.mod"), mavenSource("pom.xml"), goSource("subModuleB/go.mod"));
    }

    private void assertAdditionalReleaseTags(final String expectedTag, final String expectedAdditionalTags,
            final Source... sources) {
        publish(ProjectKeeperConfig.builder().sources(asList(sources)), ChangesFile.builder());
        verify(this.publisherMock).publish("release-tag", expectedTag);
        verify(this.publisherMock).publish("additional-release-tags", expectedAdditionalTags);
    }

    private Source mavenSource(final String path) {
        return Source.builder().type(SourceType.MAVEN).path(Path.of(path)).build();
    }

    private Source goSource(final String path) {
        return Source.builder().type(SourceType.GOLANG).path(Path.of(path)).build();
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
