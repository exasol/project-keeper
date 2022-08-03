package com.exasol.projectkeeper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class BrokenLinkReplacerTest {

    @Test
    void testReplaceParameters() {
        final BrokenLinkReplacer replacer = new BrokenLinkReplacer(List.of("http://exxxample.com|http://example.com"));
        assertThat(replacer.replaceIfBroken("http://exxxample.com"), equalTo("http://example.com"));
    }

    @Test
    void testReplaceIgnoresCase() {
        final BrokenLinkReplacer replacer = new BrokenLinkReplacer(List.of("http://exxxample.com|http://example.com"));
        assertThat(replacer.replaceIfBroken("http://EXXXAMPLE.com"), equalTo("http://example.com"));
    }

    @Test
    void testReplaceParametersWithWhitespace() {
        final BrokenLinkReplacer replacer = new BrokenLinkReplacer(
                List.of("\n         http://exxxample.com|http://example.com\n       "));
        assertThat(replacer.replaceIfBroken("http://exxxample.com"), equalTo("http://example.com"));
    }

    @Test
    void testInvalidSyntax() {
        final List<String> replacementParameters = List.of("http://exxxample.com");
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new BrokenLinkReplacer(replacementParameters));
        assertThat(exception.getMessage(),
                startsWith("E-PK-CORE-55: Invalid link replacement 'http://exxxample.com'."));
    }

    @Test
    void testWithNullLink() {
        final BrokenLinkReplacer replacer = new BrokenLinkReplacer(Collections.emptyList());
        assertThat(replacer.replaceIfBroken(null), equalTo(null));
    }

    @Test
    void testWithBuiltinReplacement() {
        final BrokenLinkReplacer replacer = new BrokenLinkReplacer(Collections.emptyList());
        assertThat(replacer.replaceIfBroken("LICENSE-exasol-jdbc.txt"), equalTo("https://docs.exasol.com/connect_exasol/drivers/jdbc.htm"));
    }

    @Test
    void testBuiltinReplacementIgnoresCase() {
        final BrokenLinkReplacer replacer = new BrokenLinkReplacer(Collections.emptyList());
        assertThat(replacer.replaceIfBroken("license-EXASOL-jdbc.txt"), equalTo("https://docs.exasol.com/connect_exasol/drivers/jdbc.htm"));
    }
}