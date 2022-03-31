package com.exasol.projectkeeper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class ProjectKeeperLauncher {
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(ProjectKeeperLauncher.class.getName());

    public static void main(final String[] args) {
        final Path projectDir = Paths.get(".").toAbsolutePath();
        final ProjectKeeper projectKeeper = ProjectKeeper.createProjectKeeper(new JULLogger(), projectDir, null);
        final boolean success = projectKeeper.verify();
        if (!success) {
            throw new IllegalStateException("invalid");
        }
    }

    private static class JULLogger implements com.exasol.projectkeeper.Logger {

        @Override
        public void info(final String message) {
            LOGGER.info(message);
        }

        @Override
        public void warn(final String message) {
            LOGGER.warning(message);
        }

        @Override
        public void error(final String message) {
            LOGGER.severe(message);
        }
    }
}
