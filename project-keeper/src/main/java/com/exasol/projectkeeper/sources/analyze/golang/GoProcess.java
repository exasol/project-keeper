package com.exasol.projectkeeper.sources.analyze.golang;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class GoProcess {
    private static final Logger LOGGER = Logger.getLogger(GoProcess.class.getName());
    private Path goPath;

    SimpleProcess start(final Path workingDirectory, final List<String> command) {
        final List<String> commandToExecute = new ArrayList<>(command);
        final Path binaryPath = getGoBinPath().resolve("bin").resolve(command.get(0));
        if (Files.exists(binaryPath)) {
            LOGGER.finest(() -> "Using go binary path " + binaryPath);
            commandToExecute.set(0, binaryPath.toString());
        }
        return SimpleProcess.start(workingDirectory, commandToExecute);
    }

    private Path getGoBinPath() {
        if (this.goPath == null) {
            this.goPath = readGoPath();
        }
        return this.goPath;
    }

    private static Path readGoPath() {
        final SimpleProcess process = SimpleProcess.start(List.of("go", "env", "GOPATH"));
        process.waitUntilFinished(Duration.ofSeconds(1));
        final Path goPath = Path.of(process.getOutputStreamContent().trim());
        LOGGER.fine(() -> "Found GOPATH '" + goPath + "'");
        return goPath;
    }
}
