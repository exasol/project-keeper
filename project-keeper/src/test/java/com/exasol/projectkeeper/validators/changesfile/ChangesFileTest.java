package com.exasol.projectkeeper.validators.changesfile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.validators.changesfile.ChangesFile.Builder;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class ChangesFileTest {

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(ChangesFile.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(ChangesFile.class).verify();
    }

    @Test
    void getPathForVersion() {
        assertThat(ChangesFile.getPathForVersion("1.2.3"), equalTo(Path.of("doc/changes/changes_1.2.3.md")));
    }

    @Test
    void toBuilderCreatesCopy() {
        final ChangesFile changesFile = builder().build();
        final ChangesFile copy = changesFile.toBuilder().build();
        assertThat(copy, equalTo(changesFile));
        assertThat(changesFile.equals(copy), is(true));
    }

    private Builder builder() {
        return ChangesFile.builder().projectName("name").projectVersion("1.2.3").releaseDate("2023-??-??")
                .codeName("my code name")
                .summary(ChangesFileSection.builder("## Summary").addLine("summary content").build())
                .dependencyChangeSection(ChangesFileSection.builder("## Dependency Updates")
                        .addLine("dependency update content").build())
                .addSection(ChangesFileSection.builder("section 1").build());
    }

    @Test
    void getParsedReleaseDateValid() {
        assertThat(builder().releaseDate("2024-01-29").build().getParsedReleaseDate().get(),
                equalTo(LocalDate.of(2024, 1, 29)));
    }

    @Test
    void getParsedReleaseDateInvalid() {
        assertThat(builder().releaseDate("invalid").build().getParsedReleaseDate().isPresent(), is(false));
    }

    @Test
    void getFixedIssuesNoSummarySection() {
        final ChangesFile changesFile = ChangesFile.builder().build();
        assertThat(changesFile.getFixedIssues(), hasSize(0));
    }

    @Test
    void getFixedIssues() {
        final ChangesFile changesFile = ChangesFile.builder()
                .dependencyChangeSection(
                        ChangesFileSection.builder("## Dependency Updates").addLine("- #123: Ignored").build())
                .summary(ChangesFileSection.builder("## Summary").addLine("* #42: Fixed in summary section").build())
                .addSection(
                        ChangesFileSection.builder("## Feature").addLine("* #1337: Fixed in feature section").build())
                .addSection(ChangesFileSection.builder("## Bugfixes").addLine("* #17: Fixed in bugfix section").build())
                .build();

        assertAll(() -> assertThat(changesFile.getFixedIssues(), hasSize(3)),
                () -> assertThat(changesFile.getFixedIssues(),
                        contains(new FixedIssue(42, "Fixed in summary section"),
                                new FixedIssue(1337, "Fixed in feature section"),
                                new FixedIssue(17, "Fixed in bugfix section"))));
    }
}
