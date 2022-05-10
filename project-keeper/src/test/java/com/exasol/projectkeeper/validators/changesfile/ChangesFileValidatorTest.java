package com.exasol.projectkeeper.validators.changesfile;

import static com.exasol.projectkeeper.FileContentMatcher.hasContent;
import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasNoValidationFindings;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasValidationFindingWithMessage;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
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
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.shared.dependencychanges.NewDependency;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.test.TestMavenModel;
import com.exasol.projectkeeper.validators.FindingFixHelper;
import com.exasol.projectkeeper.validators.finding.FindingsFixer;

@Tag("integration")
class ChangesFileValidatorTest {
    private static final String A_VERSION = "1.2.3";
    private static final String A_PROJECT_NAME = "my-project";

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
    void testFixCreatedTemplate() throws IOException {
        final AnalyzedMavenSource source = createTestSetup();
        final Logger log = mock(Logger.class);
        createValidator(source).validate().forEach(finding -> new FindingsFixer(log).fixFindings(List.of(finding)));
        final Path changesFile = this.tempDir.resolve(Path.of("doc", "changes", "changes_1.2.3.md"));
        assertThat(changesFile, hasContent(startsWith("# my-project 1.2.3, release")));
        verify(log).warn("Created 'doc" + File.separator + "changes" + File.separator
                + "changes_1.2.3.md'. Don't forget to update it's content!");
    }

    @Test
    void testFixContainsDependencyUpdates() throws IOException {
        final TestMavenModel model = new TestMavenModel();
        model.addDependency();
        final AnalyzedMavenSource source = createTestSetup(model);
        createValidator(source).validate().forEach(FindingFixHelper::fix);
        final Path changesFile = this.tempDir.resolve(Path.of("doc", "changes", "changes_1.2.3.md"));
        assertThat(changesFile, hasContent(containsString("my-lib")));
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
        mavenModel.writeAsPomToProject(this.tempDir);
        return AnalyzedMavenSource.builder().path(this.tempDir.resolve("pom.xml")).projectName(mavenModel.getName())
                .dependencyChanges(
                        new DependencyChangeReport(List.of(new NewDependency("com.example", "my-lib", "1.2.3")),
                                Collections.emptyList(), Collections.emptyList(), Collections.emptyList()))
                .build();
    }
}