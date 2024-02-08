package com.exasol.projectkeeper.sources.analyze.generic;

import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

import com.exasol.projectkeeper.OsCheck;

/**
 * Enables to execute arbitrary shell commands.
 */
public class ShellCommand {
    static final Logger LOGGER = Logger.getLogger(ShellCommand.class.getName());

    /**
     * @return A builder for new instances of {@link ShellCommand}.
     */
    public static Builder builder() {
        return new Builder();
    }

    private Duration timeout;
    private String mainCommand;
    private String subCommand;
    private Path workingDir = null;
    private final List<String> options = new ArrayList<>();

    /**
     * @return name of the command mainly for messages to the user.
     */
    public String name() {
        return this.mainCommand + (this.subCommand == null ? "" : " " + this.subCommand);
    }

    /**
     * @return Complete command line with command, optional sub command and all command line arguments and options.
     */
    public List<String> commandline() {
        final List<String> result = new ArrayList<>();
        result.add(this.mainCommand);
        if (this.subCommand != null) {
            result.add(this.subCommand);
        }
        result.addAll(this.options);
        return result;
    }

    /**
     * @return Timeout for command execution. If command execution does not terminate after this duration then the
     *         executor will raise a time-out exception.
     */
    public Duration timeout() {
        return this.timeout;
    }

    /**
     * Working directory for the process.
     * 
     * @return working dir
     */
    public Optional<Path> workingDir() {
        return Optional.ofNullable(this.workingDir);
    }

    /**
     * Builder for a new instance of {@link ShellCommand}.
     */
    public static class Builder {
        private final ShellCommand shellCommand = new ShellCommand();

        /**
         * @param timeout duration to allow to wait for the command to terminate during execution.
         * @return this for fluent programming
         */
        public Builder timeout(final Duration timeout) {
            this.shellCommand.timeout = timeout;
            return this;
        }

        /**
         * @param workingDir optional working directory for the process
         * @return this for fluent programming
         */
        public Builder workingDir(final Path workingDir) {
            this.shellCommand.workingDir = workingDir;
            return this;
        }

        /**
         * @param command name of the command to execute. On Windows platform probably with a suffix like ".exe" or
         *                ".cmd", see {@link OsCheck#suffix}
         * @return this for fluent programming
         */
        public Builder command(final String command) {
            return command(command, null);
        }

        /**
         * Command with sub command.
         *
         * @param main name of main command
         * @param sub  name of sub command
         * @return this for fluent programming
         */
        public Builder command(final String main, final String sub) {
            this.shellCommand.mainCommand = main;
            this.shellCommand.subCommand = sub;
            return this;
        }

        /**
         * @param value additional options and arguments to the command
         * @return this for fluent programming
         */
        public Builder args(final String... value) {
            return this.args(Arrays.asList(value));
        }

        /**
         * @param value additional options and arguments to the command
         * @return this for fluent programming
         */
        public Builder args(final List<String> value) {
            this.shellCommand.options.addAll(value);
            return this;
        }

        /**
         * @return new instance of {@link ShellCommand}.
         */
        public ShellCommand build() {
            return this.shellCommand;
        }
    }
}
