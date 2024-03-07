package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.TestEnvBuilder.CURRENT_VERSION;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.io.TempDir;

public class ProjectKeeperAbstractIT {
    @TempDir
    protected Path projectDir;

    protected Optional<Path> getMavenRepo() {
        return Optional.empty();
    }

    protected void runFix() {
        final ToStringLogger logger = new ToStringLogger();
        final boolean result = getProjectKeeper(logger).fix();
        assertThat("Fix succeeded. Log output: " + logger.toString(), result, is(true));
    }

    protected String assertInvalidAndGetOutput() {
        final ToStringLogger logger = new ToStringLogger();
        final ProjectKeeper projectKeeper = getProjectKeeper(logger);
        final boolean isValid = projectKeeper.verify();
        assertThat("Expected verify to fail", isValid, equalTo(false));
        return logger.toString();
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
