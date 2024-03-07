package com.exasol.projectkeeper.validators.changesfile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest(name = "Line ''{0}'' contains issue {1} with description ''{2}''")
    @CsvSource(nullValues = "NULL", value = { //
            "'', NULL, NULL", //
            "some text, NULL, NULL", //
            "#1, NULL, NULL", //
            "#1: ignored, NULL, NULL", //
            "* #1, 1, NULL", //
            "'* #1 ', 1, NULL", //
            "- #1, 1, NULL", //
            "- #4321, 4321, NULL", //
            "-#4321, 4321, NULL", //
            "' - #4321 ', 4321, NULL", //
            "- #4321:, 4321, NULL", //
            "* #1: Fixed a bug, 1, Fixed a bug", //
            "\t*\t#1\t:\tFixed a\tbug\t, 1, Fixed a\tbug", //
            "*#1: Fixed a bug, 1, Fixed a bug", //
            "* #1: Fixed a #?!'$%&§ bug, 1, Fixed a #?!'$%&§ bug", //
            "* # 1: Fixed a bug, NULL, NULL", //
            "* #invalid: Fixed a bug, NULL, NULL", //
            "* #123: Fixed a bug, 123, Fixed a bug", //
            "* #1:Fixed a bug, 1, Fixed a bug", //
            "* #1 :Fixed a bug, 1, Fixed a bug", //
            "* #1 Fixed a bug, 1, Fixed a bug", //
            "- #1: Fixed a bug, 1, Fixed a bug", //
            "' * #1: Fixed a bug', 1, Fixed a bug", //
            "'* #1: Fixed a bug ', 1, Fixed a bug", //
    })
    void getFixedIssues(final String line, final Integer expectedIssueNumber, final String expectedIssueText) {
        final List<FixedIssue> issues = ChangesFileSection.builder("## Heading").addLine(line).build() //
                .getFixedIssues().toList();
        if (expectedIssueNumber == null) {
            assertThat(issues, emptyIterable());
        } else {
            assertAll(() -> assertThat(issues, hasSize(1)),
                    () -> assertThat(issues, contains(new FixedIssue(expectedIssueNumber, expectedIssueText))));
        }
    }
}
