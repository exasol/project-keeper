package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.TestEnvBuilder.CURRENT_VERSION;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.file.Path;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.config.ProjectKeeperConfigWriter;
import com.exasol.projectkeeper.validators.TestMavenModel;

public class ProjectKeeperAbstractIT {
    @TempDir
    protected Path projectDir;
    private static MavenIntegrationTestEnvironment mavenIntegrationTestEnvironment;

    @BeforeAll
    static void beforeAll() {
        mavenIntegrationTestEnvironment = TestEnvBuilder.getTestEnv();
    }

    @BeforeEach
    void beforeEach() throws GitAPIException {
        Git.init().setDirectory(this.projectDir.toFile()).call().close();
    }

    protected void writePomWithOneDependency(final String pomVersion, final String dependencyVersion)
            throws IOException {
        final TestMavenModel testMavenModel = new TestMavenModel();
        testMavenModel.setVersion(pomVersion);
        testMavenModel.addDependency("error-reporting-java", "com.exasol", "compile", dependencyVersion);
        testMavenModel.configureAssemblyPluginFinalName();
        testMavenModel.writeAsPomToProject(this.projectDir);
    }

    public Path getMavenRepo() {
        return mavenIntegrationTestEnvironment.getLocalMavenRepository();
    }

    protected void runFix() {
        getProjectKeeper(mock(Logger.class)).fix();
    }

    protected String assertInvalidAndGetOutput() {
        final ToStringLogger logger = new ToStringLogger();
        final ProjectKeeper projectKeeper = getProjectKeeper(logger);
        final boolean isValid = projectKeeper.verify();
        assertThat(isValid, equalTo(false));
        return logger.toString();
    }

    public void writeConfig(final ProjectKeeperConfig config) {
        new ProjectKeeperConfigWriter().writeConfig(config, this.projectDir);
    }

    protected ProjectKeeper getProjectKeeper(final Logger logger) {
        return ProjectKeeper.createProjectKeeper(logger, this.projectDir, TestMavenModel.PROJECT_ARTIFACT_ID,
                getMavenRepo(), CURRENT_VERSION);
    }

    public void assertVerifySucceeds() {
        final boolean success = getProjectKeeper(mock(Logger.class)).verify();
        assertThat(success, equalTo(true));
    }

    protected static class ToStringLogger implements Logger {
        private final StringBuilder stringBuilder = new StringBuilder();

        @Override
        public void info(final String message) {
            this.stringBuilder.append(message).append("\n");
        }

        @Override
        public void warn(final String message) {
            this.stringBuilder.append(message).append("\n");
        }

        @Override
        public void error(final String message) {
            this.stringBuilder.append(message).append("\n");
        }

        @Override
        public String toString() {
            return this.stringBuilder.toString();
        }
    }
}
