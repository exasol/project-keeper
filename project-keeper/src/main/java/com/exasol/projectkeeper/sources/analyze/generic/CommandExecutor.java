package com.exasol.projectkeeper.sources.analyze.generic;

import java.nio.file.Path;

/**
 * Enable to execute a {@link ShellCommand} and hence to mock this in tests.
 */
public class CommandExecutor {

    /**
     * Executes the specified {@link ShellCommand}.
     *
     * @param sc               command to execute
     * @param workingDirectory directory to run the execution in
     * @return standard and error output of the command
     * @throws IllegalStateException if execution fails.
     */
    public ProcessResult execute(final ShellCommand sc, final Path workingDirectory) throws IllegalStateException {
        final SimpleProcess process = SimpleProcess.start(workingDirectory, sc.commandLine());
        process.waitUntilFinished(sc.timeout());
        return process.getResult();
    }

    /**
     * Executes the specified {@link ShellCommand}.
     *
     * @param sc command to execute
     * @return standard and error output of the command
     * @throws IllegalStateException if execution fails.
     */
    public ProcessResult execute(final ShellCommand sc) throws IllegalStateException {
        return execute(sc, sc.workingDir().orElse(null));
    }
}
