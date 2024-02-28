package com.exasol.projectkeeper.validators.changesfile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class ChangesFileSectionTest {

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(ChangesFileSection.class).verify();
    }

    @Test
    void toStringWithoutBody() {
        assertThat(ChangesFileSection.builder("## Heading").build(), hasToString("## Heading\n"));
    }

    @Test
    void toStringWithSingleLineBody() {
        assertThat(ChangesFileSection.builder("## Heading").addLine("content line").build(),
                hasToString("## Heading\ncontent line"));
    }

    @Test
    void toStringWithLineContainingNewline() {
        assertThat(ChangesFileSection.builder("## Heading").addLine("content\nline").build(),
                hasToString("## Heading\ncontent\nline"));
    }

    @Test
    void toStringEmptyLineRenderedAsNewline() {
        assertThat(ChangesFileSection.builder("## Heading").addLine("").addLine("content line").build(),
                hasToString("## Heading\n\ncontent line"));
    }

    @Test
    void toStringWithMultiLineBody() {
        assertThat(ChangesFileSection.builder("## Heading").addLine("content line 1").addLine("content line 2").build(),
                hasToString("## Heading\ncontent line 1\ncontent line 2"));
    }

    @Test
    void toStringCollapsesTwoNewlines() {
        assertThat(ChangesFileSection.builder("## Heading").addLine("").addLine("").addLine("content line").build(),
                hasToString("## Heading\n\ncontent line"));
    }

    @Test
    void toStringCollapsesThreeNewlines() {
        assertThat(ChangesFileSection.builder("## Heading").addLine("").addLine("").addLine("").addLine("content line")
                .build(), hasToString("## Heading\n\ncontent line"));
    }
}
