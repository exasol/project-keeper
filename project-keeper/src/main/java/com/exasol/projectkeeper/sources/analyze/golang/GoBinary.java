package com.exasol.projectkeeper.sources.analyze.golang;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;

/**
 * Represents an executable go binary.
 */
public class GoBinary {

    /**
     * {@link GoBinary} for {@code go} itself.
     */
    public static final GoBinary GO = new GoBinary(null, "go", null) {
        @Override
        boolean isInstalled() {
            return true;
        }
    };

    /**
     * {@link GoBinary} for tool {@code go-licenses}
     */
    public static final GoBinary GO_LICENSES = new GoBinary("github.com/google/go-licenses@latest", "go-licenses");

    private static final Duration INSTALLATION_TIMEOUT = Duration.ofMinutes(2);
    private static final Logger LOGGER = Logger.getLogger(GoBinary.class.getName());
    private final GoProcess goProcess;
    private final String moduleName;
    private final String binaryName;
    private Path goPath;

    GoBinary(final String sourceUrl, final String name) {
        this(new GoProcess(), sourceUrl, name);
    }

    GoBinary(final GoProcess goProcess, final String moduleName, final String binaryName) {
        this.goProcess = goProcess;
        this.moduleName = moduleName;
        this.binaryName = binaryName;
    }

    /**
     * Check if current {@link GoBinary} is already installed and install it if required.
     *
     * @param workingDirectory working directory to execute potential installation
     * @param goProcess        go process to launch potential installation
     * @return this for fluent programming
     */
    public GoBinary install() {
        if (isInstalled()) {
            return this;
        }
        LOGGER.info(() -> "Installing missing go binary " + nameWithSuffix());
        final SimpleProcess process;
        try {
            process = this.goProcess.start(null, GO, "install", this.moduleName);
        } catch (final IllegalStateException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-161")
                    .message("Error installing go binary {{binary}}.", this.binaryName).toString(), exception);
        }
        process.waitUntilFinished(INSTALLATION_TIMEOUT);
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
        return this.binaryName + (isWindows() ? ".exe" : "");
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

    private static boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }
}
