package com.exasol.projectkeeper.sources.analyze.generic;

import static java.util.Arrays.asList;

import java.nio.file.Path;
import java.time.Duration;
import java.util.*;

import com.exasol.projectkeeper.OsCheck;

/**
 * This class allows building and starting a project-local Maven Wrapper command.
 */
public class MavenProcessBuilder {
    private final List<String> args = new ArrayList<>();
    private final Path projectDirectory;
    private Path workingDir = null;
    private Duration timeout = null;

    private MavenProcessBuilder(final Path projectDirectory) {
        this.projectDirectory = Objects.requireNonNull(projectDirectory, "projectDirectory");
    }

    /**
     * Create a new builder.
     * 
     * @param projectDirectory the project directory
     * @return new builder
     */
    public static MavenProcessBuilder create(final Path projectDirectory) {
        final MavenProcessBuilder builder = new MavenProcessBuilder(projectDirectory);
        builder.addArgument("--batch-mode");
        return builder;
    }

    /**
     * Add the given arguments to the command.
     *
     * @param arguments arguments to add
     * @return {@code this} for fluent programming
     */
    public MavenProcessBuilder addArguments(final String... arguments) {
        args.addAll(asList(arguments));
        return this;
    }

    /**
     * Add the given argument to the command.
     *
     * @param argument argument to add
     * @return {@code this} for fluent programming
     */
    public MavenProcessBuilder addArgument(final String argument) {
        args.add(argument);
        return this;
    }

    /**
     * Define the working directory where to execute the command. Default: {@code null}.
     *
     * @param workingDir working dir
     * @return {@code this} for fluent programming
     */
    public MavenProcessBuilder workingDir(final Path workingDir) {
        this.workingDir = workingDir;
        return this;
    }

    /**
     * Set the timeout for running the process.
     *
     * @param timeout process timeout
     * @return {@code this} for fluent programming
     */
    public MavenProcessBuilder timeout(final Duration timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Build a {@link ShellCommand} that can be started with {@link CommandExecutor}.
     *
     * @return the built command
     */
    public ShellCommand buildCommand() {
        return ShellCommand.builder().command(getMavenExecutable()) //
                .args(this.args) //
                .timeout(this.timeout) //
                .workingDir(workingDir) //
                .build();
    }

    private String getMavenExecutable() {
        return OsCheck.getMavenWrapperPath(this.projectDirectory).toString();
    }
}
