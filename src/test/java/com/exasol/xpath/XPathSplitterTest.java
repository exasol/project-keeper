package com.exasol.xpath;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import org.junit.jupiter.api.Test;

class XPathSplitterTest {

    @Test
    void testSimplePath() {
        assertThat(XPathSplitter.split("test"), contains("test"));
    }

    @Test
    void testCompoundPath() {
        assertThat(XPathSplitter.split("test/other"), contains("test", "other"));
    }

    @Test
    void testRoot() {
        assertThat(XPathSplitter.split("/test"), contains("/test"));
    }

    @Test
    void testWithLookahead() {
        assertThat(XPathSplitter.split("/test[abc/def]"), contains("/test[abc/def]"));
    }

    @Test
    void testWithDoubleLookahead() {
        assertThat(XPathSplitter.split("/test[abc/def[/gh]]"), contains("/test[abc/def[/gh]]"));
    }

    @Test
    void testCompoundWithLookahead() {
        assertThat(XPathSplitter.split("/test/other[abc/def]"), contains("/test", "other[abc/def]"));
    }
}