package com.exasol.projectkeeper.sources.analyze.generic;

import static java.util.Arrays.asList;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.exasol.projectkeeper.OsCheck;
import com.exasol.projectkeeper.OsCheck.OSType;

/**
 * This class allows building and starting a {@code mvn} command.
 */
public class MavenProcessBuilder {
    private final List<String> command = new ArrayList<>();
    private Path workingDir = null;

    private MavenProcessBuilder() {
        // Use create() method
    }

    /**
     * Create a new builder.
     * 
     * @return new builder
     */
    public static MavenProcessBuilder create() {
        final MavenProcessBuilder builder = new MavenProcessBuilder();
        builder.addArgument(getMavenExecutable());
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
        command.addAll(asList(arguments));
        return this;
    }

    /**
     * Add the given argument to the command.
     * 
     * @param argument argument to add
     * @return {@code this} for fluent programming
     */
    public MavenProcessBuilder addArgument(final String argument) {
        command.add(argument);
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
     * Build the command and run it.
     * 
     * @return the running {@link SimpleProcess}
     */
    public SimpleProcess startSimpleProcess() {
        return SimpleProcess.start(workingDir, command);
    }

    private static String getMavenExecutable() {
        final OSType osType = new OsCheck().getOperatingSystemType();
        if (osType == OSType.WINDOWS) {
            return "mvn.cmd";
        } else {
            return "mvn";
        }
    }
}
