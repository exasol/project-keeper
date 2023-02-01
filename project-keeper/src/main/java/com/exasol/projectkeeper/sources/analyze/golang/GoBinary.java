package com.exasol.projectkeeper.sources.analyze.golang;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.OsCheck;
import com.exasol.projectkeeper.sources.analyze.generic.CommandExecutor;
import com.exasol.projectkeeper.sources.analyze.generic.ShellCommand;

/**
 * Represents an executable go binary.
 */
public class GoBinary {

    /**
     * {@link GoBinary} for {@code go} itself.
     */
    public static final GoBinary GO = new GoBinary(null, "go") {
        @Override
        boolean isInstalled() {
            return true;
        }
    };

    /**
     * {@link GoBinary} for tool {@code go-licenses}
     */
    public static final GoBinary GO_LICENSES = new GoBinary("github.com/google/go-licenses@v1.6.0", "go-licenses");

    private static final Duration INSTALLATION_TIMEOUT = Duration.ofMinutes(2);
    private static final Logger LOGGER = Logger.getLogger(GoBinary.class.getName());
    private final CommandExecutor executor;
    private final String moduleName;
    private final String binaryName;
    private Path goPath;

    GoBinary(final String moduleName, final String name) {
        this(new CommandExecutor(), moduleName, name);
    }

    GoBinary(final CommandExecutor executor, final String moduleName, final String binaryName) {
        this.moduleName = moduleName;
        this.binaryName = binaryName;
        this.executor = executor;
    }

    /**
     * Check if current {@link GoBinary} is already installed and install it if required.
     *
     * @return this for fluent programming
     */
    public GoBinary install() {
        if (isInstalled()) {
            return this;
        }
        LOGGER.info(() -> "Installing missing go binary " + nameWithSuffix());
        final ShellCommand shellCommand = ShellCommand.builder() //
                .timeout(INSTALLATION_TIMEOUT) //
                .command(GO.command()) //
                .args("install", this.moduleName) //
                .build();
        try {
            this.executor.execute(shellCommand, null);
        } catch (final IllegalStateException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-161")
                    .message("Error installing go binary {{binary}}.", this.binaryName).toString(), exception);
        }
        return this;
    }

    boolean isInstalled() {
        return Files.exists(path());
    }

    /**
     * @return command for execution in a process
     */
    public String command() {
        final Path path = path();
        if (Files.exists(path)) {
            return path.toString();
        }
        return this.binaryName;
    }

    Path path() {
        return getGoBinPath().resolve("bin").resolve(nameWithSuffix());
    }

    String nameWithSuffix() {
        return this.binaryName + OsCheck.suffix(".exe");
    }

    private Path getGoBinPath() {
        if (this.goPath == null) {
            this.goPath = readGoPath();
        }
        return this.goPath;
    }

    private static Path readGoPath() {
        final SimpleProcess process = SimpleProcess.start(List.of("go", "env", "GOPATH"));
        process.waitUntilFinished(Duration.ofSeconds(5));
        final Path goPath = Path.of(process.getOutputStreamContent().trim());
        LOGGER.fine(() -> "Found GOPATH '" + goPath + "'");
        return goPath;
    }
}
