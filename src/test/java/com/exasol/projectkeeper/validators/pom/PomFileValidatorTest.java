package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasNoValidationFindings;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.validationErrorMessages;
import static com.exasol.projectkeeper.validators.pom.PomTesting.POM_WITH_NO_PLUGINS;
import static com.exasol.projectkeeper.validators.pom.PomTesting.writeResourceToTempFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.ProjectKeeperModule;

class PomFileValidatorTest {

    @Test
    void testMissingPlugin(@TempDir final File tempDir) throws IOException {
        final File pomFile = writeResourceToTempFile(tempDir, POM_WITH_NO_PLUGINS);
        final PomFileValidator runner = new PomFileValidator(Arrays.asList(ProjectKeeperModule.values()),
                Collections.emptyList(), pomFile);
        assertThat(runner, validationErrorMessages(
                hasItems(containsString("E-PK-15: Missing maven plugin org.codehaus.mojo:versions-maven-plugin."))));
    }

    @Test
    void testFixMissingPlugin(@TempDir final File tempDir) throws IOException {
        final File pomFile = writeResourceToTempFile(tempDir, POM_WITH_NO_PLUGINS);
        final PomFileValidator runner = new PomFileValidator(Arrays.asList(ProjectKeeperModule.values()),
                Collections.emptyList(), pomFile);
        assertThat(runner, hasNoMoreFindingsAfterApplyingFixes());
    }

    @Test
    // [utest->dsn~modules~1]
    void testNoErrorsOnNoModules(@TempDir final File tempDir) throws IOException {
        final File pomFile = writeResourceToTempFile(tempDir, POM_WITH_NO_PLUGINS);
        final PomFileValidator runner = new PomFileValidator(Collections.emptyList(), Collections.emptyList(), pomFile);
        assertThat(runner, hasNoValidationFindings());
    }
}