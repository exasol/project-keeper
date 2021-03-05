package com.exasol.projectkeeper.validators.changelog;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ChangelogIOTest {

    @TempDir
    Path tempDir;

    @Test
    void testParsing() throws IOException {
        final Path changesFile = loadExampleFileToTempDir();
        final ChangelogFile changelogFile = new ChangelogIO().read(changesFile);
        final List<String> headlines = changelogFile.getSections().stream().map(ChangelogSection::getHeadline)
                .collect(Collectors.toList());
        assertThat(changelogFile.getHeadline(), equalTo("# My Project 0.1.0, released 1980-01-01"));
        assertThat(headlines, contains("## Summary", "## Features", "## Bug Fixes", "## Documentation",
                "## Refactoring", "## Dependency Updates"));
    }

    @Test
    void testWriting() throws IOException {
        final ChangelogFile changelogFile = ChangelogFile.builder().setHeader(List.of("# MyChanges"))
                .addSection(List.of("## My Subsection")).build();
        final Path testFile = this.tempDir.resolve("myFile.md");
        new ChangelogIO().write(changelogFile, testFile);
        assertThat(Files.readString(testFile),
                equalTo("# MyChanges" + System.lineSeparator() + "## My Subsection" + System.lineSeparator()));
    }

    @Test
    void testReadAndWrite() throws IOException {
        final Path changesFile = loadExampleFileToTempDir();
        final ChangelogIO changelogIO = new ChangelogIO();
        final ChangelogFile changelogFile = changelogIO.read(changesFile);
        final Path testFile = this.tempDir.resolve("result.md");
        changelogIO.write(changelogFile, testFile);
        assertThat(Files.readString(testFile), equalTo(Files.readString(changesFile)));
    }

    private Path loadExampleFileToTempDir() throws IOException {
        final Path changesFile = this.tempDir.resolve("changed_0.1.0.md");
        try (final InputStream exampleFileStream = getClass().getClassLoader()
                .getResourceAsStream("changelogExample1.md")) {
            Files.copy(Objects.requireNonNull(exampleFileStream), changesFile, StandardCopyOption.REPLACE_EXISTING);
        }
        return changesFile;
    }
}