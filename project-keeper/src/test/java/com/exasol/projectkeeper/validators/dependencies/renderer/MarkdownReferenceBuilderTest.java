package com.exasol.projectkeeper.validators.dependencies.renderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.Test;

class MarkdownReferenceBuilderTest {
    @Test
    void testReferenceDeclarations() {
        final MarkdownReferenceBuilder referenceBuilder = new MarkdownReferenceBuilder();
        referenceBuilder.getReferenceForUrl("Exasol Website", "https://exasol.com");
        assertThat(referenceBuilder.getReferences(), equalTo("[0]: https://exasol.com" + System.lineSeparator()));
    }

    @Test
    void testGetSameReferenceForSameUrl() {
        final MarkdownReferenceBuilder referenceBuilder = new MarkdownReferenceBuilder();
        final String reference1 = referenceBuilder.getReferenceForUrl("Exasol Website", "https://exasol.com");
        final String reference2 = referenceBuilder.getReferenceForUrl("Other Description", "https://exasol.com");
        assertThat(reference1, equalTo(reference2));
    }

    @Test
    void testGetSameReferenceForSameNameDifferentUrl() {
        final MarkdownReferenceBuilder referenceBuilder = new MarkdownReferenceBuilder();
        final String reference1 = referenceBuilder.getReferenceForUrl("Exasol Website", "https://exasol.com");
        final String reference2 = referenceBuilder.getReferenceForUrl("Exasol Website", "https://exasol.de");
        assertThat(reference1, not(equalTo(reference2)));
    }
}