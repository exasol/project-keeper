package com.exasol.projectkeeper.sources.analyze.npm;

import java.time.Duration;

import com.exasol.projectkeeper.OsCheck;
import com.exasol.projectkeeper.sources.analyze.generic.ShellCommand;

public class NpmCommand {

    private static final String NPM = "npm" + OsCheck.suffix(".cmd");
    private static final String NPX = "npx" + OsCheck.suffix(".cmd");

    public static final ShellCommand LIST_DEPENDENCIES = ShellCommand.builder() //
            .command(NPM, "list") //
            .timeout(Duration.ofMinutes(2)) //
            .args("--location=project", "--depth=0", "--json") //
            .build();
    public static final ShellCommand LICENSE_CHECKER = ShellCommand.builder() //
            .command(NPX, "license-checker") //
            .timeout(Duration.ofMinutes(2)) //
            .args("--location=project", "--direct", "--json") //
            .build();
}
