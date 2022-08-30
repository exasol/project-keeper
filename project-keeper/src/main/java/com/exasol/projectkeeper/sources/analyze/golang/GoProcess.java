package com.exasol.projectkeeper.sources.analyze.golang;

import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

class GoProcess {
    private static final Logger LOGGER = Logger.getLogger(GoProcess.class.getName());

    SimpleProcess start(final Path workingDirectory, final GoBinary binary, final String... args) {
        final String command = binary.command();
        LOGGER.finest(() -> "Executing go binary " + command);
        final List<String> commandLine = new ArrayList<>();
        commandLine.add(command);
        commandLine.addAll(Arrays.asList(args));
        return SimpleProcess.start(workingDirectory, commandLine);
    }
}
