package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasNoValidationFindings;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.validationErrorMessages;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.projectkeeper.*;
import com.exasol.projectkeeper.validators.TestMavenModel;
import com.exasol.projectkeeper.validators.finding.FindingsUngrouper;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;

class PomFileValidatorIT {
    private static JavaProjectCrawlerRunner javaProjectCrawlerRunner;

    @TempDir
    Path tempDir;

    @BeforeAll
    static void beforeAll() {
        final MavenIntegrationTestEnvironment testEnv = TestEnvBuilder.getTestEnv();
        javaProjectCrawlerRunner = new JavaProjectCrawlerRunner(testEnv.getLocalMavenRepository(),
                TestEnvBuilder.CURRENT_VERSION);
    }

    @Test
    void testMissingPlugin() throws IOException {
        new TestMavenModel().writeAsPomToProject(this.tempDir);
        final PomFileValidator runner = new PomFileValidator(Arrays.asList(ProjectKeeperModule.values()),
                this.tempDir.resolve("pom.xml"), javaProjectCrawlerRunner);
        assertThat(runner, validationErrorMessages(hasItems(
                containsString("E-PK-CORE-15: Missing maven plugin org.codehaus.mojo:versions-maven-plugin."))));
    }

    @Test
    void testValidationOfProjectWithParentPom() throws IOException {
        new TestMavenSetupWithParentPom().writeTo(this.tempDir);
        final PomFileValidator validator = new PomFileValidator(Arrays.asList(ProjectKeeperModule.values()),
                this.tempDir.resolve("pom.xml"), javaProjectCrawlerRunner);
        final List<SimpleValidationFinding> findings = new FindingsUngrouper().ungroupFindings(validator.validate());
        assertThat("findings should not have a fix since this is too complex for project-keeper.",
                findings.get(0).hasFix(), equalTo(false));
    }

    @Test
    void testFixMissingPlugin() throws IOException {
        new TestMavenModel().writeAsPomToProject(this.tempDir);
        final PomFileValidator runner = new PomFileValidator(Arrays.asList(ProjectKeeperModule.values()),
                this.tempDir.resolve("pom.xml"), javaProjectCrawlerRunner);
        assertThat(runner, hasNoMoreFindingsAfterApplyingFixes());
    }

    @Test
    // [utest->dsn~modules~1]
    void testNoErrorsOnNoModules() throws IOException {
        new TestMavenModel().writeAsPomToProject(this.tempDir);
        final PomFileValidator runner = new PomFileValidator(Collections.emptyList(), this.tempDir.resolve("pom.xml"),
                javaProjectCrawlerRunner);
        assertThat(runner, hasNoValidationFindings());
    }
}