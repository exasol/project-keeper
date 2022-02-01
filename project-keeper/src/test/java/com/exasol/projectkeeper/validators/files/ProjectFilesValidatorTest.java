package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.validationErrorMessages;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.validators.FindingFixHelper;

//[utest->dsn~required-files-validator~1]
class ProjectFilesValidatorTest {
    private static final Set<ProjectKeeperModule> DEFAULT_MODULE = Set.of(ProjectKeeperModule.DEFAULT);
    private @TempDir Path tempDir;

    @Test
    void testValidation() throws IOException {
        Files.createDirectories(this.tempDir.resolve(".settings"));
        Files.createFile(this.tempDir.resolve(Path.of(".settings", "org.eclipse.jdt.ui.prefs")));
        final ProjectFilesValidator validator = new ProjectFilesValidator(this.tempDir, getMvnSourceWithAllModules(),
                mock(Logger.class));
        assertThat(validator,
                validationErrorMessages(hasItems(
                        "E-PK-CORE-17: Missing required: '.settings" + File.separator + "org.eclipse.jdt.core.prefs'",
                        "E-PK-CORE-18: Outdated content: '.settings" + File.separator + "org.eclipse.jdt.ui.prefs'")));
    }

    private List<ProjectKeeperConfig.Source> getMvnSourceWithAllModules() {
        return List.of(ProjectKeeperConfig.Source.builder().type(ProjectKeeperConfig.SourceType.MAVEN)
                .modules(DEFAULT_MODULE).path(this.tempDir.resolve("pom.xml")).build());
    }

    @Test
    void testFix() {
        final ProjectFilesValidator validator = new ProjectFilesValidator(this.tempDir, getMvnSourceWithAllModules(),
                mock(Logger.class));
        assertThat(validator, hasNoMoreFindingsAfterApplyingFixes());
    }

    @Test
    void testDifferentContent() throws IOException {
        final ProjectFilesValidator validator = new ProjectFilesValidator(this.tempDir, getMvnSourceWithAllModules(),
                mock(Logger.class));
        fixAllFindings(validator);
        final Path testFile = this.tempDir.resolve(".settings" + File.separator + "org.eclipse.jdt.core.prefs");
        changeFile(testFile);
        assertThat(validator, validationErrorMessages(contains(
                "E-PK-CORE-18: Outdated content: '.settings" + File.separator + "org.eclipse.jdt.core.prefs'")));
    }

    @Test
    void testFixDifferentContent() throws IOException {
        final ProjectFilesValidator validator = new ProjectFilesValidator(this.tempDir, getMvnSourceWithAllModules(),
                mock(Logger.class));
        fixAllFindings(validator);
        final Path testFile = this.tempDir.resolve(Path.of(".settings", "org.eclipse.jdt.core.prefs"));
        changeFile(testFile);
        assertThat(validator, hasNoMoreFindingsAfterApplyingFixes());
    }

    @Test
    void testMultiSourceProject() {
        final List<ProjectKeeperConfig.Source> sources = List.of(//
                ProjectKeeperConfig.Source.builder().type(ProjectKeeperConfig.SourceType.MAVEN).modules(DEFAULT_MODULE)
                        .path(this.tempDir.resolve("pom.xml")).build(), //
                ProjectKeeperConfig.Source.builder().type(ProjectKeeperConfig.SourceType.MAVEN).modules(DEFAULT_MODULE)
                        .path(this.tempDir.resolve("sub-project/pom.xml")).build());
        final ProjectFilesValidator validator = new ProjectFilesValidator(this.tempDir, sources, mock(Logger.class));
        fixAllFindings(validator);
        assertAll(//
                () -> assertThat(this.tempDir.resolve(".github/workflows/ci-build.yml").toFile(), anExistingFile()),
                () -> assertThat(this.tempDir.resolve(".settings/org.eclipse.jdt.core.prefs").toFile(),
                        anExistingFile()),
                () -> assertThat(this.tempDir.resolve("sub-project/.settings/org.eclipse.jdt.core.prefs").toFile(),
                        anExistingFile())//
        );
    }

    private void fixAllFindings(final ProjectFilesValidator validator) {
        validator.validate().forEach(FindingFixHelper::fix);
    }

    private void changeFile(final Path testFile) throws IOException {
        Files.writeString(testFile, "something");
    }
}