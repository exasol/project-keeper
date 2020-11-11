package com.exasol.projectkeeper.validators;

import static com.exasol.projectkeeper.FileContentMatcher.hasContent;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasValidationFindingWithMessage;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.validationErrorMessages;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.nio.file.Path;
import java.util.regex.Pattern;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ChangelogValidatorTest {
    private static final String TEST_VERSION = "1.2.3";

    @Test
    void testValidation(@TempDir final File tempDir) {
        final MavenProject project = createProject(tempDir, TEST_VERSION);
        assertThat(new ChangelogValidator(project),
                hasValidationFindingWithMessage("E-PK-20 Could not find 'doc/changes/changes_1.2.3.md'."));
    }

    @Test
    void testValidationForSnapshotVersion(@TempDir final File tempDir) {
        final MavenProject project = createProject(tempDir, TEST_VERSION + "-SNAPSHOT");
        assertThat(new ChangelogValidator(project),
                hasValidationFindingWithMessage("E-PK-20 Could not find 'doc/changes/changes_1.2.3.md'."));
    }

    @Test
    void testFixCreatedTemplate(@TempDir final File tempDir) {
        final MavenProject project = createProject(tempDir, TEST_VERSION);
        final Log log = mock(Log.class);
        new ChangelogValidator(project).validate(finding -> finding.getFix().fixError(log));
        assertThat(tempDir.toPath().resolve(Path.of("doc", "changes", "changes_1.2.3.md")),
                hasContent(startsWith("#my-project 1.2.3, release")));
        verify(log).warn("Created 'doc/changes/changes_1.2.3.md'. Don't forget to update it's content!");
    }

    @Test
    void testInvalidAfterFix(@TempDir final File tempDir) {
        final MavenProject project = createProject(tempDir, TEST_VERSION);
        new ChangelogValidator(project).validate(finding -> finding.getFix().fixError(mock(Log.class)));
        assertThat(new ChangelogValidator(project),
                validationErrorMessages(
                        hasItems(matchesPattern(Pattern.quote("E-PK-22 Please change the content of '/tmp/") + ".*"
                                + Pattern.quote("/doc/changes/changes_1.2.3.md' by hand!")))));
    }

    private MavenProject createProject(final File tempDir, final String version) {
        final MavenProject project = new MavenProject();
        project.setVersion(version);
        project.setBasedir(tempDir);
        project.setName("my-project");
        return project;
    }
}