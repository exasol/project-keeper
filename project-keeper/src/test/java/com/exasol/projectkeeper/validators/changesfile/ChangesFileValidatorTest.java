package com.exasol.projectkeeper.validators.changesfile;

import static com.exasol.projectkeeper.FileContentMatcher.hasContent;
import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasNoValidationFindings;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasValidationFindingWithMessage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.*;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource.AnalyzedMavenSourceBuilder;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.test.TestMavenModel;
import com.exasol.projectkeeper.validators.FindingFixHelper;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile.Builder;
import com.exasol.projectkeeper.validators.finding.*;

// [utest->dsn~verify-release-mode.verify-changes-file~1]
@Tag("integration")
@ExtendWith(MockitoExtension.class)
class ChangesFileValidatorTest {
    private static final String A_VERSION = "1.2.3";
    private static final String A_PROJECT_NAME = "my-project";
    private static final String LINE_SEPARATOR = "\n";

    @TempDir
    Path tempDir;
    @Mock
    ChangesFileIO changesFileIOMock;

    @Test
    void testValidation() throws IOException {
        final AnalyzedMavenSource source = createTestSetup();
        assertThat(createValidator(source),
                hasValidationFindingWithMessage("E-PK-CORE-56: Could not find required file 'doc" + File.separator
                        + "changes" + File.separator + "changes_1.2.3.md'."));
    }

    @Test
    void testValidationForSnapshotVersion() throws IOException {
        final AnalyzedMavenSource source = createTestSetup();
        assertThat(new ChangesFileValidator(A_VERSION + "-SNAPSHOT", A_PROJECT_NAME, this.tempDir, List.of(source)),
                hasNoValidationFindings());
    }

    @Test
    void noDependencyUpdates(@Mock final Logger log) throws IOException {
        final AnalyzedMavenSource source = createTestSetup(new TestMavenModel(), Collections.emptyList());
        createValidator(source).validate().forEach(finding -> new FindingsFixer(log).fixFindings(List.of(finding)));
        final Path changesFile = this.tempDir.resolve(Path.of("doc", "changes", "changes_1.2.3.md"));
        assertThat(changesFile,
                hasContent(equalTo("# my-project 1.2.3, released 2024-??-??" + LINE_SEPARATOR + LINE_SEPARATOR //
                        + "Code name:" + LINE_SEPARATOR + LINE_SEPARATOR //
                        + "## Summary" + LINE_SEPARATOR + LINE_SEPARATOR //
                        + "## Features" + LINE_SEPARATOR + LINE_SEPARATOR //
                        + "* ISSUE_NUMBER: description" + LINE_SEPARATOR + LINE_SEPARATOR)));

        verify(log).info("Created 'doc" + File.separator + "changes" + File.separator
                + "changes_1.2.3.md'. Don't forget to update its content!");
        final List<ValidationFinding> findings = createValidator(source).validate();
        assertThat(findings, empty());
    }

    @Test
    void testFixCreatedTemplate(@Mock final Logger log) throws IOException {
        final AnalyzedMavenSource source = createTestSetup();
        createValidator(source).validate().forEach(finding -> new FindingsFixer(log).fixFindings(List.of(finding)));
        final Path changesFile = this.tempDir.resolve(Path.of("doc", "changes", "changes_1.2.3.md"));
        assertThat(changesFile,
                hasContent(equalTo("# my-project 1.2.3, released 2024-??-??" + LINE_SEPARATOR + LINE_SEPARATOR //
                        + "Code name:" + LINE_SEPARATOR + LINE_SEPARATOR //
                        + "## Summary" + LINE_SEPARATOR + LINE_SEPARATOR //
                        + "## Features" + LINE_SEPARATOR + LINE_SEPARATOR //
                        + "* ISSUE_NUMBER: description" + LINE_SEPARATOR + LINE_SEPARATOR //
                        + "## Dependency Updates" + LINE_SEPARATOR + LINE_SEPARATOR //
                        + "### Compile Dependency Updates" + LINE_SEPARATOR + LINE_SEPARATOR
                        + "* Added `com.example:my-lib:1.2.3`" + LINE_SEPARATOR)));
        verify(log).info("Created 'doc" + File.separator + "changes" + File.separator
                + "changes_1.2.3.md'. Don't forget to update its content!");
    }

    @Test
    void testFixContainsDependencyUpdates() throws IOException {
        final TestMavenModel model = new TestMavenModel();
        model.addDependency();
        final AnalyzedMavenSource source = createTestSetup(model);
        createValidator(source).validate().forEach(FindingFixHelper::fix);
        final Path changesFile = this.tempDir.resolve(Path.of("doc", "changes", "changes_1.2.3.md"));
        assertThat(changesFile, hasContent(endsWith("## Dependency Updates" + LINE_SEPARATOR + LINE_SEPARATOR //
                + "### Compile Dependency Updates" + LINE_SEPARATOR + LINE_SEPARATOR //
                + "* Added `com.example:my-lib:1.2.3`" + LINE_SEPARATOR)));
    }

    @Test
    void testValidationOnFixedFile() throws IOException {
        final TestMavenModel model = new TestMavenModel();
        model.addDependency();
        final AnalyzedMavenSource source = createTestSetup(model);
        createValidator(source).validate().forEach(FindingFixHelper::fix);
        assertThat(createValidator(source), hasNoMoreFindingsAfterApplyingFixes());
    }

    @Test
    void testValidateChangesFileMissing() {
        final List<ValidationFinding> findings = createValidatorWithChangesFileMock(
                AnalyzedMavenSource.builder().projectName("name").build()).validate();
        assertFinding(findings, "E-PK-CORE-56: Could not find required file 'doc/changes/changes_1.2.3.md'.");
    }

    @Test
    void testValidateChangesFileMissingSummary() throws IOException {
        simulateChangesFile(ChangesFile.builder().projectName("name"));
        final List<ValidationFinding> findings = createValidatorWithChangesFileMock(defaultSource().build()).validate();
        assertFinding(findings,
                "E-PK-CORE-193: Section '## Summary' is missing in 'doc/changes/changes_1.2.3.md'. Add section.");
    }

    @Test
    void testValidateChangesFileEmptySummary() throws IOException {
        simulateChangesFile(
                ChangesFile.builder().projectName("name").summary(ChangesFileSection.builder("## Summary").build()));
        final List<ValidationFinding> findings = createValidatorWithChangesFileMock(defaultSource().build()).validate();
        assertThat(findings, empty()); // This is validated in ChangesFileReleaseValidator
    }

    @Test
    void testValidateChangesFileSummaryWithBlankLines() throws IOException {
        simulateChangesFile(ChangesFile.builder().projectName("name")
                .summary(ChangesFileSection.builder("## Summary").addLine("   ").build()));
        final List<ValidationFinding> findings = createValidatorWithChangesFileMock(defaultSource().build()).validate();
        assertThat(findings, empty()); // This is validated in ChangesFileReleaseValidator
    }

    @Test
    void testValidateChangesFileMissingProjectName() throws IOException {
        simulateChangesFile(
                ChangesFile.builder().summary(ChangesFileSection.builder("## Summary").addLine("content").build()));
        final List<ValidationFinding> findings = createValidatorWithChangesFileMock(defaultSource().build()).validate();
        assertFinding(findings,
                "E-PK-CORE-195: Project name in 'doc/changes/changes_1.2.3.md' is missing. Add a project name.");
    }

    @Test
    void testValidateChangesFileBlankProjectName() throws IOException {
        simulateChangesFile(ChangesFile.builder().projectName("   ")
                .summary(ChangesFileSection.builder("## Summary").addLine("content").build()));
        final List<ValidationFinding> findings = createValidatorWithChangesFileMock(defaultSource().build()).validate();
        assertFinding(findings,
                "E-PK-CORE-195: Project name in 'doc/changes/changes_1.2.3.md' is missing. Add a project name.");
    }

    private AnalyzedMavenSourceBuilder defaultSource() {
        return AnalyzedMavenSource.builder().dependencyChanges(DependencyChangeReport.builder().build());
    }

    private void simulateChangesFile(final Builder builder) throws IOException {
        final Path path = tempDir.resolve("doc/changes/changes_" + A_VERSION + ".md");
        Files.createDirectories(path.getParent());
        Files.createFile(path);
        when(changesFileIOMock.read(path)).thenReturn(builder.build());
    }

    private void assertFinding(final List<ValidationFinding> findings, final String expectedMessage) {
        assertAll(() -> assertThat(findings, hasSize(1)),
                () -> assertThat(((SimpleValidationFinding) findings.get(0)).getMessage(), equalTo(expectedMessage)));
    }

    private Validator createValidator(final AnalyzedSource source) {
        return new ChangesFileValidator(A_VERSION, A_PROJECT_NAME, this.tempDir, List.of(source));
    }

    private Validator createValidatorWithChangesFileMock(final AnalyzedSource source) {
        return new ChangesFileValidator(A_VERSION, A_PROJECT_NAME, this.tempDir, List.of(source), changesFileIOMock);
    }

    private AnalyzedMavenSource createTestSetup() throws IOException {
        return createTestSetup(new TestMavenModel());
    }

    private AnalyzedMavenSource createTestSetup(final TestMavenModel mavenModel) throws IOException {
        final List<DependencyChange> list = List.of(new NewDependency("com.example", "my-lib", "1.2.3"));
        return createTestSetup(mavenModel, list);
    }

    private AnalyzedMavenSource createTestSetup(final TestMavenModel mavenModel,
            final List<DependencyChange> dependencyChanges) throws IOException {
        mavenModel.writeAsPomToProject(this.tempDir);
        return AnalyzedMavenSource.builder().path(this.tempDir.resolve("pom.xml")).projectName(mavenModel.getName())
                .dependencyChanges(DependencyChangeReport.builder().typed(Type.COMPILE, dependencyChanges).build()) //
                .build();
    }
}
