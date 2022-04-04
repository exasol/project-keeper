package com.exasol.projectkeeper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;

/**
 * This is the main entry point for launching Project Keeper on the command line.
 * <p>
 * Currently this can only be used for starting PK from an IDE. See https://github.com/exasol/project-keeper/issues/277
 * for a complete command line interface.
 */
public class ProjectKeeperLauncher {
    private static final String GOAL_VERIFY = "verify";
    private static final String GOAL_FIX = "fix";
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(ProjectKeeperLauncher.class.getName());

    /**
     * The main entry point for launching Project Keeper.
     * 
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        verifyCommandLineArguments(args);
        final String goal = args[0];
        runProjectKeeper(goal);
    }

    private static void runProjectKeeper(final String goal) {
        final ProjectKeeper projectKeeper = createProjectKeeper();
        final boolean success = goal.equals(GOAL_FIX) ? projectKeeper.fix() : projectKeeper.verify();
        if (!success) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-141").message("Failed to run project keeper {{goal}}", goal)
                            .mitigation("See log messages above for details.").toString());
        }
    }

    private static ProjectKeeper createProjectKeeper() {
        final Path projectDir = Paths.get(".").toAbsolutePath();
        return ProjectKeeper.createProjectKeeper(new JULLogger(), projectDir, null);
    }

    private static void verifyCommandLineArguments(final String[] args) {
        if (args.length != 1 || !(args[0].equals(GOAL_FIX) || args[0].equals(GOAL_VERIFY))) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-140")
                    .message("Got no or invalid command line argument {{arguments}}.", Arrays.toString(args))
                    .mitigation("Please only specify arguments '" + GOAL_VERIFY + "' or '" + GOAL_FIX + "'.")
                    .toString());
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
