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

import org.apache.maven.plugin.logging.Log;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.projectkeeper.ExcludedFilesMatcher;
import com.exasol.projectkeeper.TestEnvBuilder;
import com.exasol.projectkeeper.validators.TestMavenModel;

class ChangesFileValidatorIT {
    private static final String A_VERSION = "1.2.3";
    private static final String A_PROJECT_NAME = "my-project";

    @TempDir
    File tempDir;

    private static Path testMavenRepo;

    @BeforeAll
    static void beforeAll() {
        final MavenIntegrationTestEnvironment testEnv = TestEnvBuilder.getTestEnv();
        testMavenRepo = testEnv.getLocalMavenRepository();
    }

    @BeforeEach
    void beforeEach() throws GitAPIException {
        Git.init().setDirectory(this.tempDir).call().close();
    }

    @Test
    void testValidation() throws IOException {
        createTestSetup();
        assertThat(createValidator(), hasValidationFindingWithMessage("E-PK-56: Could not find required file 'doc"
                + File.separator + "changes" + File.separator + "changes_1.2.3.md'."));
    }

    @Test
    void testValidationForSnapshotVersion() throws IOException {
        createTestSetup();
        assertThat(new ChangesFileValidator(A_VERSION + "-SNAPSHOT", A_PROJECT_NAME, this.tempDir.toPath(),
                new ExcludedFilesMatcher(Collections.emptyList()), testMavenRepo, TestEnvBuilder.CURRENT_VERSION),
                hasNoValidationFindings());
    }

    @Test
    void testFixCreatedTemplate() throws IOException {
        createTestSetup();
        final Log log = mock(Log.class);
        createValidator().validate().forEach(finding -> finding.getFix().fixError(log));
        final Path changesFile = this.tempDir.toPath().resolve(Path.of("doc", "changes", "changes_1.2.3.md"));
        assertThat(changesFile, hasContent(startsWith("# my-project 1.2.3, release")));
        verify(log).warn("Created 'doc" + File.separator + "changes" + File.separator
                + "changes_1.2.3.md'. Don't forget to update it's content!");
    }

    @Test
    void testFixContainsDependencyUpdates() throws IOException {
        final TestMavenModel model = new TestMavenModel();
        model.addDependency();
        createTestSetup(model);
        final Log log = mock(Log.class);
        createValidator().validate().forEach(finding -> finding.getFix().fixError(log));
        final Path changesFile = this.tempDir.toPath().resolve(Path.of("doc", "changes", "changes_1.2.3.md"));
        assertThat(changesFile, hasContent(containsString(TestMavenModel.DEPENDENCY_ARTIFACT_ID)));
    }

    @Test
    void testValidationOnFixedFile() throws IOException {
        final TestMavenModel model = new TestMavenModel();
        model.addDependency();
        createTestSetup(model);
        createValidator().validate().forEach(finding -> finding.getFix().fixError(mock(Log.class)));
        assertThat(createValidator(), hasNoMoreFindingsAfterApplyingFixes());
    }

    private ChangesFileValidator createValidator() {
        return new ChangesFileValidator(A_VERSION, A_PROJECT_NAME, this.tempDir.toPath(),
                new ExcludedFilesMatcher(Collections.emptyList()), testMavenRepo, TestEnvBuilder.CURRENT_VERSION);
    }

    private void createTestSetup() throws IOException {
        createTestSetup(new TestMavenModel());
    }

    private void createTestSetup(final TestMavenModel mavenModel) throws IOException {
        mavenModel.writeAsPomToProject(this.tempDir.toPath());
    }
}