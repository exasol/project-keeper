package com.exasol.projectkeeper.validators.changesfile;

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

class ChangesFileIOTest {

    @TempDir
    Path tempDir;

    @Test
    void testParsing() throws IOException {
        final Path changesFilePath = loadExampleFileToTempDir();
        final ChangesFile changesFile = new ChangesFileIO().read(changesFilePath);
        final List<String> headings = changesFile.getSections().stream().map(ChangesFileSection::getHeading)
                .collect(Collectors.toList());
        assertThat(changesFile.getHeading(), equalTo("# My Project 0.1.0, released 1980-01-01"));
        assertThat(headings, contains("## Summary", "## Features", "## Bug Fixes", "## Documentation", "## Refactoring",
                "## ProjectDependency Updates"));
    }

    @Test
    void testWriting() throws IOException {
        final ChangesFile changesFile = ChangesFile.builder().setHeader(List.of("# MyChanges"))
                .addSection(List.of("## My Subsection")).build();
        final Path testFile = this.tempDir.resolve("myFile.md");
        new ChangesFileIO().write(changesFile, testFile);
        assertThat(Files.readString(testFile),
                equalTo("# MyChanges" + System.lineSeparator() + "## My Subsection" + System.lineSeparator()));
    }

    @Test
    void testReadAndWrite() throws IOException {
        final Path changesFilePath = loadExampleFileToTempDir();
        final ChangesFileIO changesFileIO = new ChangesFileIO();
        final ChangesFile changesFile = changesFileIO.read(changesFilePath);
        final Path testFile = this.tempDir.resolve("result.md");
        changesFileIO.write(changesFile, testFile);
        assertThat(Files.readString(testFile), equalTo(Files.readString(changesFilePath)));
    }

    private Path loadExampleFileToTempDir() throws IOException {
        final Path changesFile = this.tempDir.resolve("changed_0.1.0.md");
        try (final InputStream exampleFileStream = getClass().getClassLoader()
                .getResourceAsStream("changesFileExample1.md")) {
            Files.copy(Objects.requireNonNull(exampleFileStream), changesFile, StandardCopyOption.REPLACE_EXISTING);
        }
        return changesFile;
    }
}