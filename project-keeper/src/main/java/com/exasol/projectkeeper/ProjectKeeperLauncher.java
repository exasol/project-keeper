package com.exasol.projectkeeper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;

/**
 * This is the main entry point for launching Project Keeper on the command line.
 */
public class ProjectKeeperLauncher {
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(ProjectKeeperLauncher.class.getName());
    private static final String GOAL_VERIFY = "verify";
    private static final String GOAL_FIX = "fix";

    private final Path currentWorkingDir;

    ProjectKeeperLauncher(final Path currentWorkingDir) {
        this.currentWorkingDir = currentWorkingDir.toAbsolutePath();
    }

    /**
     * The main entry point for launching Project Keeper.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        final ProjectKeeperLauncher launcher = new ProjectKeeperLauncher(Paths.get("."));
        launcher.start(args);
    }

    void start(final String[] args) {
        verifyCommandLineArguments(args);
        final String goal = args[0];
        runProjectKeeper(goal);
    }

    private void runProjectKeeper(final String goal) {
        final ProjectKeeper projectKeeper = createProjectKeeper();
        final boolean success = goal.equals(GOAL_FIX) ? projectKeeper.fix() : projectKeeper.verify();
        if (!success) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-141").message("Failed to run project keeper {{goal}}", goal)
                            .mitigation("See log messages above for details.").toString());
        }
    }

    private ProjectKeeper createProjectKeeper() {
        return ProjectKeeper.createProjectKeeper(new JULLogger(), this.currentWorkingDir, null);
    }

    private void verifyCommandLineArguments(final String[] args) {
        if ((args == null) || (args.length != 1) || !(GOAL_FIX.equals(args[0]) || GOAL_VERIFY.equals(args[0]))) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-140")
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
