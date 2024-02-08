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
        assertThat(changesFile.getSummarySection().get().getContent(), contains("", "My summary", ""));
        assertThat(headings, contains("## Features", "## Bug Fixes", "## Documentation", "## Refactoring"));
        assertThat(changesFile.getDependencyChangeSection().get().getHeading(), equalTo("## Dependency Updates"));
        assertThat(changesFile.getDependencyChangeSection().get().getContent(), hasSize(18));
    }

    @Test
    void testWriting() throws IOException {
        final ChangesFile changesFile = ChangesFile.builder().projectName("project").projectVersion("1.2.3")
                .releaseDate("2023-??-??").codeName("my code name")
                .summary(ChangesFileSection.builder("## Summary").addLine("my summary content").build())
                .addSection(ChangesFileSection.builder("# MyChanges").build())
                .addSection(ChangesFileSection.builder("## My Subsection").addLine("content").build()).build();
        final Path testFile = this.tempDir.resolve("myFile.md");
        new ChangesFileIO().write(changesFile, testFile);
        assertThat(Files.readString(testFile), equalTo(
                "# project 1.2.3, released 2023-??-??\n\nCode name: my code name\n\n## Summary\nmy summary content\n# MyChanges\n## My Subsection\ncontent\n"));
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
        final ChangesFile changesFile = readFromString(
                "# Project Name 1.2.3, released 2024-??-??\nCode name: my code name\n## Summary\n\n");
        assertThat(changesFile.getProjectName(), equalTo("Project Name"));
        assertThat(changesFile.getProjectVersion().toString(), equalTo("1.2.3"));
        assertThat(changesFile.getReleaseDate(), equalTo("2024-??-??"));
        assertThat(changesFile.getCodeName(), equalTo("my code name"));
        assertThat(changesFile.getParsedReleaseDate().isPresent(), is(false));
        assertWriteRead(changesFile);
    }

    @Test
    void testReadFirstLineWithValidReleaseDate() throws IOException {
        final ChangesFile changesFile = readFromString("# Project Name 1.2.3, released 2024-01-29\n## Summary\n\n");
        assertThat(changesFile.getProjectName(), equalTo("Project Name"));
        assertThat(changesFile.getProjectVersion().toString(), equalTo("1.2.3"));
        assertThat(changesFile.getReleaseDate(), equalTo("2024-01-29"));
        assertThat(changesFile.getParsedReleaseDate().get(), equalTo(LocalDate.parse("2024-01-29")));
        assertWriteRead(changesFile);
    }

    @Test
    void testReadMissingSummary() throws IOException {
        final ChangesFile changesFile = readFromString("# Project Name 1.2.3, released 2024-01-29");
        assertThat(changesFile.getSummarySection().isEmpty(), is(true));
    }

    @Test
    void testReadEmptySummary() throws IOException {
        final ChangesFile changesFile = readFromString("# Project Name 1.2.3, released 2024-01-29\n## Summary");
        final ChangesFileSection summary = changesFile.getSummarySection().get();
        assertThat(summary.getHeading(), equalTo("## Summary"));
        assertThat(summary.getContent(), emptyIterable());
    }

    @Test
    void testReadSummary() throws IOException {
        final ChangesFile changesFile = readFromString(
                "# Project Name 1.2.3, released 2024-01-29\n## Summary\nmy\ncontent\n");
        final ChangesFileSection summary = changesFile.getSummarySection().get();
        assertThat(summary.getHeading(), equalTo("## Summary"));
        assertThat(summary.getContent(), contains("my", "content"));
        assertWriteRead(changesFile);
    }

    @Test
    void testReadNoDependencySection() throws IOException {
        final ChangesFile changesFile = readFromString("# Project Name 1.2.3, released 2024-01-29\n## Summary");
        assertThat(changesFile.getDependencyChangeSection().isEmpty(), is(true));
    }

    @Test
    void testReadDependencySection() throws IOException {
        final ChangesFile changesFile = readFromString(
                "# Project Name 1.2.3, released 2024-01-29\n## Summary\n## Dependency Updates\nmy\ncontent");
        assertThat(changesFile.getDependencyChangeSection().isEmpty(), is(false));
        assertThat(changesFile.getDependencyChangeSection().get().getHeading(), equalTo("## Dependency Updates"));
        assertThat(changesFile.getDependencyChangeSection().get().getContent(), contains("my", "content"));
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
            return new String(exampleFileStream.readAllBytes(), StandardCharsets.UTF_8).replace("\r\n", "\n");
        }
    }

    private InputStream getExampleFileStream() {
        return getClass().getClassLoader().getResourceAsStream("changesFileExample1.md");
    }

    private void assertWriteRead(final ChangesFile changesFile) throws IOException {
        final String content = writeToString(changesFile);
        final ChangesFile readChangesFile = readFromString(content);
        assertThat(readChangesFile.toString(), equalTo(changesFile.toString()));
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
