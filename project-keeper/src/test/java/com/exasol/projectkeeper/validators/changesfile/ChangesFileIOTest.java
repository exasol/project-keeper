package com.exasol.projectkeeper.validators.changesfile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
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
        assertThat(changesFile.getProjectName(), equalTo("My Project"));
        assertThat(changesFile.getProjectVersion().toString(), equalTo("0.1.0"));
        assertThat(changesFile.getReleaseDate(), equalTo("1980-01-01"));
        assertThat(changesFile.getParsedReleaseDate().get(), equalTo(LocalDate.parse("1980-01-01")));
        assertThat(headings, contains("## Summary", "## Features", "## Bug Fixes", "## Documentation", "## Refactoring",
                "## Dependency Updates"));
    }

    @Test
    void testWriting() throws IOException {
        final ChangesFile changesFile = ChangesFile.builder().projectName("project").projectVersion("1.2.3")
                .releaseDate("2023-??-??").setHeader(List.of("# MyChanges")).addSection(List.of("## My Subsection"))
                .build();
        final Path testFile = this.tempDir.resolve("myFile.md");
        new ChangesFileIO().write(changesFile, testFile);
        assertThat(Files.readString(testFile), equalTo("# project 1.2.3, released 2023-??-??" + System.lineSeparator() + //
                "# MyChanges" + System.lineSeparator() + //
                "## My Subsection" + System.lineSeparator()));
    }

    @Test
    void testReadAndWrite() throws IOException {
        final String content = readExampleFile();
        assertReadWrite(content);
    }

    @Test
    void testReadInvalidFirstLineFails() {
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> readFromString("# invalid first line"));
        assertThat(exception.getMessage(), startsWith(
                "E-PK-CORE-171: Changes file 'dummy-file' contains invalid first line '# invalid first line'. Update first line so that it matches regex"));
    }

    @Test
    void testReadFirstLineWithDummyReleaseDate() throws IOException {
        final ChangesFile changesFile = readFromString("# Project Name 1.2.3, released 2024-??-??");
        assertThat(changesFile.getProjectName(), equalTo("Project Name"));
        assertThat(changesFile.getProjectVersion().toString(), equalTo("1.2.3"));
        assertThat(changesFile.getReleaseDate(), equalTo("2024-??-??"));
        assertThat(changesFile.getParsedReleaseDate().isPresent(), is(false));
        assertWriteRead(changesFile);
    }

    @Test
    void testReadFirstLineWithValidReleaseDate() throws IOException {
        final ChangesFile changesFile = readFromString("# Project Name 1.2.3, released 2024-01-29");
        assertThat(changesFile.getProjectName(), equalTo("Project Name"));
        assertThat(changesFile.getProjectVersion().toString(), equalTo("1.2.3"));
        assertThat(changesFile.getReleaseDate(), equalTo("2024-01-29"));
        assertThat(changesFile.getParsedReleaseDate().get(), equalTo(LocalDate.parse("2024-01-29")));
        assertWriteRead(changesFile);
    }

    private Path loadExampleFileToTempDir() throws IOException {
        final Path changesFile = this.tempDir.resolve("changed_0.1.0.md");
        try (final InputStream exampleFileStream = getExampleFileStream()) {
            Files.copy(Objects.requireNonNull(exampleFileStream), changesFile, StandardCopyOption.REPLACE_EXISTING);
        }
        return changesFile;
    }

    private String readExampleFile() throws IOException {
        try (final InputStream exampleFileStream = getExampleFileStream()) {
            return new String(exampleFileStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private InputStream getExampleFileStream() {
        return getClass().getClassLoader().getResourceAsStream("changesFileExample1.md");
    }

    private void assertWriteRead(final ChangesFile changesFile) throws IOException {
        final String content = writeToString(changesFile);
        final ChangesFile readChangesFile = readFromString(content);
        assertThat(readChangesFile, equalTo(changesFile));
    }

    private void assertReadWrite(final String content) throws IOException {
        final ChangesFile changesFile = readFromString(content);
        final String writtenContent = writeToString(changesFile);
        assertThat(writtenContent, equalTo(content));
    }

    private String writeToString(final ChangesFile changesFile) throws IOException {
        final StringWriter stringWriter = new StringWriter();
        new ChangesFileIO().write(changesFile, stringWriter);
        return stringWriter.toString();
    }

    private ChangesFile readFromString(final String content) throws IOException {
        return new ChangesFileIO().read(Path.of("dummy-file"), new BufferedReader(new StringReader(content)));
    }
}
