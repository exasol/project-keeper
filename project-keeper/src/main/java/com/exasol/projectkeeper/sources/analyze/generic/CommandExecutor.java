package com.exasol.projectkeeper.sources.analyze.generic;

import java.nio.file.Path;
import java.util.logging.Logger;

import com.exasol.projectkeeper.sources.analyze.golang.SimpleProcess;

public class CommandExecutor {
    static final Logger LOGGER = Logger.getLogger(CommandExecutor.class.getName());

    public String execute(final ShellCommand sc, final Path workingDirectory) throws IllegalStateException {
        LOGGER.finest(() -> "Executing command " + sc.name());
        final SimpleProcess process = SimpleProcess.start(workingDirectory, sc.commandline());
        process.waitUntilFinished(sc.timeout());
        return process.getOutputStreamContent();
    }
}
