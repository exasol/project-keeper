package com.exasol.projectkeeper.validators.dependencies.renderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MarkdownReferenceBuilderTest {
    @Test
    void testReferenceDeclarations() {
        final MarkdownReferenceBuilder referenceBuilder = new MarkdownReferenceBuilder();
        referenceBuilder.getReferenceForUrl("https://exasol.com");
        assertThat(referenceBuilder.getReferences(), equalTo("[0]: https://exasol.com" + System.lineSeparator()));
    }

    @ParameterizedTest
    @ValueSource(strings = { "http://maven.apache.org", "https://maven.apache.org" })
    void testReferencesToMAvenApacheOrg(final String url) {
        assumeTrue(Workarounds.ALTERNATIVING_DEPENDENCIES.isActive());
        final MarkdownReferenceBuilder referenceBuilder = new MarkdownReferenceBuilder();
        referenceBuilder.getReferenceForUrl(url);
        assertThat(referenceBuilder.getReferences(), equalTo("[0]: https://maven.apache.org" + System.lineSeparator()));
    }

    @Test
    void testGetSameReferenceForSameUrl() {
        final MarkdownReferenceBuilder referenceBuilder = new MarkdownReferenceBuilder();
        final String reference1 = referenceBuilder.getReferenceForUrl("https://exasol.com");
        final String reference2 = referenceBuilder.getReferenceForUrl("https://exasol.com");
        assertThat(reference1, equalTo(reference2));
    }

    @Test
    void testGetSameReferenceForSameNameDifferentUrl() {
        final MarkdownReferenceBuilder referenceBuilder = new MarkdownReferenceBuilder();
        final String reference1 = referenceBuilder.getReferenceForUrl("https://exasol.com");
        final String reference2 = referenceBuilder.getReferenceForUrl("https://exasol.de");
        assertThat(reference1, not(equalTo(reference2)));
    }
}