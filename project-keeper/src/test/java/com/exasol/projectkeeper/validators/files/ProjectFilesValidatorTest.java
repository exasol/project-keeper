package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasNoValidationFindings;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.validationErrorMessages;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.shared.config.BuildConfig;
import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.FindingFixHelper;

//[utest->dsn~required-files-validator~1]
class ProjectFilesValidatorTest {
    private static final String OWN_VERSION = "version";
    private static final Set<ProjectKeeperModule> DEFAULT_MODULE = Set.of(ProjectKeeperModule.DEFAULT);

    @TempDir
    Path tempDir;

    @Test
    void testValidation() throws IOException {
        Files.createDirectories(this.tempDir.resolve(".settings"));
        Files.createFile(this.tempDir.resolve(Path.of(".settings", "org.eclipse.jdt.ui.prefs")));
        assertThat(testee(), validationErrorMessages( //
                hasItems("E-PK-CORE-17: Missing required file: '.settings/org.eclipse.jdt.core.prefs'",
                        "E-PK-CORE-18: Outdated content: '.settings/org.eclipse.jdt.ui.prefs'")));
    }

    private List<AnalyzedSource> getMvnSourceWithDefaultModule() {
        return List.of(AnalyzedMavenSource.builder().path(this.tempDir.resolve("pom.xml")).modules(DEFAULT_MODULE)
                .isRootProject(true).build());
    }

    @Test
    void testFix() {
        assertThat(testee(), hasNoMoreFindingsAfterApplyingFixes());
    }

    @Test
    void testDifferentContent() throws IOException {
        final ProjectFilesValidator validator = testee();
        fixAllFindings(validator);
        final Path testFile = this.tempDir.resolve(".settings/org.eclipse.jdt.core.prefs");
        changeFile(testFile);
        assertThat(validator, validationErrorMessages(
                contains("E-PK-CORE-18: Outdated content: '.settings/org.eclipse.jdt.core.prefs'")));
    }

    @Test
    void testFixDifferentContent() throws IOException {
        final ProjectFilesValidator validator = testee();
        fixAllFindings(validator);
        final Path testFile = this.tempDir.resolve(Path.of(".settings", "org.eclipse.jdt.core.prefs"));
        changeFile(testFile);
        assertThat(validator, hasNoMoreFindingsAfterApplyingFixes());
    }

    @Test
    void testMissingFileWithAnyContent() throws IOException {
        final ProjectFilesValidator validator = testee();
        fixAllFindings(validator);
        final Path testFile = this.tempDir.resolve("release_config.yml");
        deleteFile(testFile);
        assertThat(validator, validationErrorMessages(
                contains("E-PK-CORE-17: Missing required file: '" + testFile.getFileName() + "'")));
    }

    @Test
    void testRequireExistsIgnoresContent() throws IOException {
        final ProjectFilesValidator validator = testee();
        fixAllFindings(validator);
        final Path testFile = this.tempDir.resolve("release_config.yml");
        changeFile(testFile);
        assertThat(validator, hasNoValidationFindings());
    }

    @Test
    void testRequireExistsDoesNotModifyContent() throws IOException {
        final ProjectFilesValidator validator = testee();
        fixAllFindings(validator);
        final Path testFile = this.tempDir.resolve("release_config.yml");
        changeFile(testFile);
        assertAll(() -> assertThat(validator, hasNoMoreFindingsAfterApplyingFixes()),
                () -> assertThat(new String(Files.readAllBytes(testFile), StandardCharsets.UTF_8),
                        equalTo("something")));
    }

    @Test
    void testFixMissingFileWithAnyContent() throws IOException {
        final ProjectFilesValidator validator = testee();
        fixAllFindings(validator);
        final Path testFile = this.tempDir.resolve("release_config.yml");
        deleteFile(testFile);
        assertAll(() -> assertThat(validator, hasNoMoreFindingsAfterApplyingFixes()),
                () -> assertTrue(Files.exists(testFile),
                        "File " + testFile + " does not exist after fixing the findings"));
    }

    @Test
    void testMultiSourceProject() {
        final List<AnalyzedSource> sources = List.of(//
                AnalyzedMavenSource.builder().path(this.tempDir.resolve("pom.xml")).modules(DEFAULT_MODULE)
                        .isRootProject(true).build(),
                AnalyzedMavenSource.builder().path(this.tempDir.resolve("sub-project/pom.xml")).modules(DEFAULT_MODULE)
                        .isRootProject(false).build());
        fixAllFindings(testee(sources));
        assertAll(//
                () -> assertThat(this.tempDir.resolve(".github/workflows/ci-build.yml").toFile(), anExistingFile()),
                () -> assertThat(this.tempDir.resolve(".settings/org.eclipse.jdt.core.prefs").toFile(),
                        anExistingFile()),
                () -> assertThat(this.tempDir.resolve("sub-project/.settings/org.eclipse.jdt.core.prefs").toFile(),
                        anExistingFile())//
        );
    }

    @Test
    void testCiBuildRunnerOS() {
        final List<AnalyzedSource> sources = List.of(//
                AnalyzedMavenSource.builder().path(this.tempDir.resolve("pom.xml")).modules(DEFAULT_MODULE)
                        .isRootProject(true).build());
        fixAllFindings(testee(sources));
        final Path ciBuildWorkflow = this.tempDir.resolve(".github/workflows/ci-build.yml");
        final Path releaseDroidOriginalChecksumWorkflow = this.tempDir
                .resolve(".github/workflows/release_droid_prepare_original_checksum.yml");
        assertAll(//
                () -> assertThat(ciBuildWorkflow.toFile(), anExistingFile()),
                () -> assertThat(Files.readString(ciBuildWorkflow), containsString("    runs-on: ci-runner-os")),
                () -> assertThat(releaseDroidOriginalChecksumWorkflow.toFile(), anExistingFile()),
                () -> assertThat(Files.readString(releaseDroidOriginalChecksumWorkflow),
                        containsString("    runs-on: ci-runner-os")));
    }

    private ProjectFilesValidator testee() {
        return testee(getMvnSourceWithDefaultModule());
    }

    private ProjectFilesValidator testee(final List<AnalyzedSource> sources) {
        return ProjectFilesValidator.builder() //
                .projectDirectory(this.tempDir) //
                .analyzedSources(sources) //
                .logger(mock(Logger.class)) //
                .projectKeeperVersion(OWN_VERSION) //
                .ciBuildConfig(BuildConfig.builder().runnerOs("ci-runner-os").build()) //
                .build();
    }

    private void fixAllFindings(final ProjectFilesValidator validator) {
        validator.validate().forEach(FindingFixHelper::fix);
    }

    private void changeFile(final Path testFile) throws IOException {
        Files.writeString(testFile, "something");
    }

    private void deleteFile(final Path testFile) throws IOException {
        Files.delete(testFile);
    }
}
