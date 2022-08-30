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
     * {@link GoBinary} for {@link go} itself.
     */
    public static final GoBinary GO = new GoBinary(null, "go", null) {
        @Override
        boolean isInstalled() {
            return true;
        }
    };

    /**
     * {@link GoBinary} for go-licenses tool.
     */
    public static final GoBinary GO_LICENSES = new GoBinary("github.com/google", "go-licenses", "latest");

    private static final Duration INSTALLATION_TIMEOUT = Duration.ofMinutes(2);
    private static final Logger LOGGER = Logger.getLogger(GoProcess.class.getName());
    private final String sourceUrl;
    private final String name;
    private final String version;
    private Path goPath;

    GoBinary(final String sourceUrl, final String name, final String version) {
        this.sourceUrl = sourceUrl;
        this.name = name;
        this.version = version;
    }

    /**
     * Check if current {@link GoBinary} is already installed and install it if required.
     *
     * @param workingDirectory working directory to execute potential installation
     * @param goProcess        go process to launch potential installation
     * @return this for fluent programming
     */
    public GoBinary install(final Path workingDirectory, final GoProcess goProcess) {
        if (isInstalled()) {
            return this;
        }
        LOGGER.info(() -> "Installing missing go binary " + nameWithSuffix());
        final SimpleProcess process;
        try {
            process = goProcess.start(workingDirectory, GO, "install", installSource());
        } catch (final IllegalStateException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-161")
                    .message("Error installing go binary {{binary}}.", this.name).toString(), exception);
        }
        process.waitUntilFinished(INSTALLATION_TIMEOUT);
        return this;
    }

    String installSource() {
        return String.format("%s/%s@%s", this.sourceUrl, this.name, this.version);
    }

    /**
     * @return {@code true} if the binary is already installed
     */
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
        return this.name;
    }

    /**
     * @return absolute path to binary including platform suffix
     */
    Path path() {
        return getGoBinPath().resolve("bin").resolve(nameWithSuffix());
    }

    String nameWithSuffix() {
        return this.name + (isWindows() ? ".exe" : "");
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
