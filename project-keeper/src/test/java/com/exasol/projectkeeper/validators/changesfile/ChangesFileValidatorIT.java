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
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.TestEnvBuilder;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.FindingFixHelper;
import com.exasol.projectkeeper.validators.TestMavenModel;
import com.exasol.projectkeeper.validators.finding.FindingsFixer;

@Tag("integration")
class ChangesFileValidatorIT {
    private static final String A_VERSION = "1.2.3";
    private static final String A_PROJECT_NAME = "my-project";

    @TempDir
    Path tempDir;

    private static Path testMavenRepo;

    @BeforeAll
    static void beforeAll() {
        final MavenIntegrationTestEnvironment testEnv = TestEnvBuilder.getTestEnv();
        testMavenRepo = testEnv.getLocalMavenRepository();
    }

    @BeforeEach
    void beforeEach() throws GitAPIException {
        Git.init().setDirectory(this.tempDir.toFile()).call().close();
    }

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
        assertThat(new ChangesFileValidator(A_VERSION + "-SNAPSHOT", A_PROJECT_NAME, this.tempDir, testMavenRepo,
                TestEnvBuilder.CURRENT_VERSION, List.of(source)), hasNoValidationFindings());
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
        assertThat(changesFile, hasContent(containsString(TestMavenModel.DEPENDENCY_ARTIFACT_ID)));
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
        return new ChangesFileValidator(A_VERSION, A_PROJECT_NAME, this.tempDir, testMavenRepo,
                TestEnvBuilder.CURRENT_VERSION, List.of(source));
    }

    private AnalyzedMavenSource createTestSetup() throws IOException {
        return createTestSetup(new TestMavenModel());
    }

    private AnalyzedMavenSource createTestSetup(final TestMavenModel mavenModel) throws IOException {
        mavenModel.writeAsPomToProject(this.tempDir);
        return new AnalyzedMavenSource(this.tempDir.resolve("pom.xml"), null, true, null, mavenModel.getName(), true);
    }
}