package com.exasol.projectkeeper.sources.analyze.golang;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class GoProcess {
    private static final Logger LOGGER = Logger.getLogger(GoProcess.class.getName());

    private GoProcess() {
        // not instantiable
    }

    static SimpleProcess start(final Path workingDirectory, final List<String> command) {
        List<String> commandToExecute = new ArrayList<>(command);
        Path goBinPath = getGoBinPath();
        Path binaryPath = goBinPath.resolve("bin").resolve(command.get(0));
        if (Files.exists(binaryPath)) {
            LOGGER.finest(() -> "Using go binary path " + binaryPath);
            commandToExecute.set(0, binaryPath.toString());
        }
        return SimpleProcess.start(workingDirectory, commandToExecute);
    }

    private static Path getGoBinPath() {
        SimpleProcess process = SimpleProcess.start(List.of("go", "env", "GOPATH"));
        return Path.of(process.getOutput(Duration.ofSeconds(1)).trim());
    }
}
