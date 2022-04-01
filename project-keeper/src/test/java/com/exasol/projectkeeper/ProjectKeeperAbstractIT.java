package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.TestEnvBuilder.CURRENT_VERSION;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import java.nio.file.Path;
import java.util.Optional;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.config.ProjectKeeperConfigWriter;

public class ProjectKeeperAbstractIT {
    @TempDir
    protected Path projectDir;

    @BeforeEach
    void beforeEach() throws GitAPIException {
        Git.init().setDirectory(this.projectDir.toFile()).call().close();
    }

    protected Optional<Path> getMavenRepo() {
        return Optional.empty();
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

    protected void writeConfig(final ProjectKeeperConfig config) {
        new ProjectKeeperConfigWriter().writeConfig(config, this.projectDir);
    }

    protected ProjectKeeper getProjectKeeper(final Logger logger) {
        return ProjectKeeper.createProjectKeeper(logger, this.projectDir, getMavenRepo().orElse(null), CURRENT_VERSION);
    }

    protected void assertVerifySucceeds() {
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
