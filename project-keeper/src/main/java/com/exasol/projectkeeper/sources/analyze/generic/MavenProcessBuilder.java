package com.exasol.projectkeeper.sources.analyze.generic;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.exasol.projectkeeper.OsCheck;
import com.exasol.projectkeeper.OsCheck.OSType;

/**
 * This class allows building and starting a {@code mvn} command.
 */
public class MavenProcessBuilder {

    private final List<String> command = new ArrayList<>();

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
     * Build the command that can be used as argument for {@link ProcessBuilder}.
     * 
     * @return the command
     */
    public List<String> build() {
        return List.copyOf(this.command);
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
