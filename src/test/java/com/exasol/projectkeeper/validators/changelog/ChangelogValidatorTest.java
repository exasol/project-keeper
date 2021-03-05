package com.exasol.projectkeeper.validators.changelog;

import static com.exasol.projectkeeper.FileContentMatcher.hasContent;
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

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ChangelogValidatorTest {
    private static final String TEST_VERSION = "1.2.3";

    @TempDir
    File tempDir;

    @BeforeEach
    void beforeEach() throws GitAPIException {
        Git.init().setDirectory(this.tempDir).call().close();
    }

    @Test
    void testValidation() throws IOException {
        final MavenProject project = createProject(this.tempDir, TEST_VERSION);
        assertThat(new ChangelogValidator(project),
                hasValidationFindingWithMessage("E-PK-20: Could not find 'doc/changes/changes_1.2.3.md'."));
    }

    @Test
    void testValidationForSnapshotVersion() throws IOException {
        final MavenProject project = createProject(this.tempDir, TEST_VERSION + "-SNAPSHOT");
        assertThat(new ChangelogValidator(project), hasNoValidationFindings());
    }

    @Test
    void testFixCreatedTemplate() throws IOException {
        final MavenProject project = createProject(this.tempDir, TEST_VERSION);
        final Log log = mock(Log.class);
        new ChangelogValidator(project).validate(finding -> finding.getFix().fixError(log));
        final Path changesFile = this.tempDir.toPath().resolve(Path.of("doc", "changes", "changes_1.2.3.md"));
        assertThat(changesFile, hasContent(startsWith("# my-project 1.2.3, release")));
        verify(log).warn("Created 'doc/changes/changes_1.2.3.md'. Don't forget to update it's content!");
    }

    @Test
    void testFixContainsDependencyUpdates() throws IOException {
        final TestMavenModel model = new TestMavenModel();
        model.addDependency();
        final MavenProject project = createProject(this.tempDir, TEST_VERSION, model);
        final Log log = mock(Log.class);
        new ChangelogValidator(project).validate(finding -> finding.getFix().fixError(log));
        final Path changesFile = this.tempDir.toPath().resolve(Path.of("doc", "changes", "changes_1.2.3.md"));
        assertThat(changesFile, hasContent(containsString(TestMavenModel.DEPENDENCY_ARTIFACT_ID)));
    }

    @Test
    void testValidationOnFixedFile() throws IOException {
        final TestMavenModel model = new TestMavenModel();
        model.addDependency();
        final MavenProject project = createProject(this.tempDir, TEST_VERSION, model);
        new ChangelogValidator(project).validate(finding -> finding.getFix().fixError(mock(Log.class)));
        assertThat(new ChangelogValidator(project), hasNoValidationFindings());
    }

    private MavenProject createProject(final File tempDir, final String version) throws IOException {
        return createProject(tempDir, version, new TestMavenModel());
    }

    private MavenProject createProject(final File tempDir, final String version, final TestMavenModel mavenModel)
            throws IOException {
        mavenModel.writeAsPomToProject(tempDir.toPath());
        final MavenProject project = new MavenProject(mavenModel);
        project.setVersion(version);
        project.setBasedir(tempDir);
        project.setName("my-project");
        return project;
    }
}