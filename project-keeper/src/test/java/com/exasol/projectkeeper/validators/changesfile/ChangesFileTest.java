package com.exasol.projectkeeper.validators.changesfile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.validators.changesfile.ChangesFile.Builder;

import nl.jqno.equalsverifier.EqualsVerifier;

class ChangesFileTest {

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(ChangesFile.class).verify();
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
                .addSection(List.of("section 1")).setHeader(List.of("header 1"));
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
}
