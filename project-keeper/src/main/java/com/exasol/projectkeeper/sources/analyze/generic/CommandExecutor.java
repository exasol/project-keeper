package com.exasol.projectkeeper.sources.analyze.generic;

import java.nio.file.Path;
import java.util.logging.Logger;

import com.exasol.projectkeeper.sources.analyze.golang.SimpleProcess;

/**
 * Enable to execute a {@link ShellCommand} and hence to mock this in tests.
 */
public class CommandExecutor {
    static final Logger LOGGER = Logger.getLogger(CommandExecutor.class.getName());

    /**
     * Executes the specified {@link ShellCommand}
     *
     * @param sc               command to execute
     * @param workingDirectory directory to run the execution in
     * @return standard output of the command
     * @throws IllegalStateException if execution fails.
     */
    public String execute(final ShellCommand sc, final Path workingDirectory) throws IllegalStateException {
        LOGGER.finest(() -> "Executing command " + sc.name());
        final SimpleProcess process = SimpleProcess.start(workingDirectory, sc.commandline());
        process.waitUntilFinished(sc.timeout());
        return process.getOutputStreamContent();
    }
}
