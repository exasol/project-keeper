package com.exasol.projectkeeper.cli;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ProjectKeeper;

/**
 * This is the main entry point for launching Project Keeper on the command line.
 */
public class ProjectKeeperLauncher {
    static {
        configureLogging();
    }
    private static final Logger LOGGER = Logger.getLogger(ProjectKeeperLauncher.class.getName());
    private static final String GOAL_VERIFY = "verify";
    private static final String GOAL_VERIFY_RELEASE = "verify-release";
    private static final String GOAL_FIX = "fix";
    private static final String GOAL_UPDATE_DEPENDENCIES = "update-dependencies";
    private static final Map<String, ProjectKeeperGoal> ACCEPT_GOALS = Map.of( //
            GOAL_VERIFY, ProjectKeeper::verify, //
            GOAL_VERIFY_RELEASE, ProjectKeeper::verifyRelease, //
            GOAL_FIX, ProjectKeeper::fix, //
            GOAL_UPDATE_DEPENDENCIES, ProjectKeeper::updateDependencies //
    );

    private final Path currentWorkingDir;

    ProjectKeeperLauncher(final Path currentWorkingDir) {
        try {
            this.currentWorkingDir = currentWorkingDir.toRealPath();
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CLI-4")
                    .message("Failed to get absolute path of current directory.").toString(), exception);
        }
    }

    @SuppressWarnings("java:S4792") // Logger configuration is safe, we don't log sensitive information.
    private static void configureLogging() {
        try (final InputStream is = ProjectKeeperLauncher.class.getClassLoader()
                .getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(is);
        } catch (final IOException exception) {
            LOGGER.log(Level.WARNING, ExaError.messageBuilder("W-PK-CLI-3")
                    .message("Failed to load logging configuration.").ticketMitigation().toString(), exception);
        }
    }

    /**
     * The main entry point for launching Project Keeper.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        final ProjectKeeperLauncher launcher = new ProjectKeeperLauncher(Path.of("."));
        launcher.start(args);
    }

    void start(final String[] args) {
        verifyCommandLineArguments(args);
        final String goal = args[0];
        runProjectKeeper(goal);
    }

    private void runProjectKeeper(final String goal) {
        final ProjectKeeper projectKeeper = createProjectKeeper();
        final ProjectKeeperGoal method = getProjectKeeperGoal(goal);
        final boolean success = method.execute(projectKeeper);
        if (!success) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CLI-1").message("Failed to run project keeper {{goal}}", goal)
                            .mitigation("See log messages above for details.").toString());
        }
    }

    private ProjectKeeperGoal getProjectKeeperGoal(final String goal) {
        final ProjectKeeperGoal failure = pk -> {
            LOGGER.warning(() -> ExaError.messageBuilder("E-PK-CLI-5").message("Goal {{goal}} not supported.", goal)
                    .mitigation("Use one of the supported goals: {{supported goals}}", ACCEPT_GOALS.keySet())
                    .toString());
            return false;
        };
        return ACCEPT_GOALS.getOrDefault(goal, failure);
    }

    private ProjectKeeper createProjectKeeper() {
        return ProjectKeeper.createProjectKeeper(new JULLogger(), this.currentWorkingDir, null);
    }

    private void verifyCommandLineArguments(final String[] args) {
        if ((args == null) || (args.length != 1) || !(GOAL_FIX.equals(args[0]) || GOAL_VERIFY.equals(args[0])
                || GOAL_UPDATE_DEPENDENCIES.equals(args[0]))) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CLI-2")
                    .message("Got no or invalid command line argument {{arguments}}.", Arrays.toString(args))
                    .mitigation("Please only specify arguments {{supported goals}}.", ACCEPT_GOALS.keySet())
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

    @FunctionalInterface
    private interface ProjectKeeperGoal {
        boolean execute(ProjectKeeper projectKeeper);
    }
}
