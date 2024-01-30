package com.exasol.projectkeeper.validators.changesfile;

import static com.exasol.projectkeeper.FileContentMatcher.hasContent;
import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasNoValidationFindings;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasValidationFindingWithMessage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.*;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.test.TestMavenModel;
import com.exasol.projectkeeper.validators.FindingFixHelper;
import com.exasol.projectkeeper.validators.finding.FindingsFixer;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

@Tag("integration")
class ChangesFileValidatorTest {
    private static final String A_VERSION = "1.2.3";
    private static final String A_PROJECT_NAME = "my-project";
    private static final String LINE_SEPARATOR = System.lineSeparator();

    @TempDir
    Path tempDir;

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
    void noDependencyUpdates() throws IOException {
        final AnalyzedMavenSource source = createTestSetup(new TestMavenModel(), Collections.emptyList());
        final Logger log = mock(Logger.class);
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
    void testFixCreatedTemplate() throws IOException {
        final AnalyzedMavenSource source = createTestSetup();
        final Logger log = mock(Logger.class);
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

    private ChangesFileValidator createValidator(final AnalyzedSource source) {
        return new ChangesFileValidator(A_VERSION, A_PROJECT_NAME, this.tempDir, List.of(source));
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
