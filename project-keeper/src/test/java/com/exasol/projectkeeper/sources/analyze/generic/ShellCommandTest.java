package com.exasol.projectkeeper.sources.analyze.generic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;

class ShellCommandTest {
    @Test
    void simpleCommand() {
        final ShellCommand cmd = ShellCommand.builder() //
                .command("main") //
                .args("-l", "abc") //
                .build();
        assertThat(cmd.name(), equalTo("main"));
        assertThat(cmd.commandLine(), equalTo(List.of("main", "-l", "abc")));
    }

    @Test
    void subCommand() {
        final Duration duration = Duration.ofMinutes(2);
        final ShellCommand cmd = ShellCommand.builder() //
                .command("main", "sub") //
                .timeout(duration) //
                .args("a", "b", "c") //
                .build();
        assertThat(cmd.name(), equalTo("main sub"));
        assertThat(cmd.timeout(), equalTo(duration));
        assertThat(cmd.commandLine(), equalTo(List.of("main", "sub", "a", "b", "c")));
    }
}
