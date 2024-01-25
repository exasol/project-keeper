package com.exasol.projectkeeper.sources.analyze.generic;

import java.nio.file.Path;

/**
 * Enable to execute a {@link ShellCommand} and hence to mock this in tests.
 */
public class CommandExecutor {

    /**
     * Executes the specified {@link ShellCommand}
     *
     * @param sc               command to execute
     * @param workingDirectory directory to run the execution in
     * @return standard output of the command
     * @throws IllegalStateException if execution fails.
     */
    public String execute(final ShellCommand sc, final Path workingDirectory) throws IllegalStateException {
        final SimpleProcess process = SimpleProcess.start(workingDirectory, sc.commandline());
        process.waitUntilFinished(sc.timeout());
        return process.getOutputStreamContent();
    }
}
