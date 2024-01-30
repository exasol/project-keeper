package com.exasol.projectkeeper.validators.changesfile;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile.Builder;

/**
 * This class reads and writes a {@link ChangesFile} from disk.
 */
public class ChangesFileIO {
    private static final String PROJECT_NAME_PATTERN = "[\\w\\s-]+";
    private static final String VERSION_PATTERN = "\\d+\\.\\d+\\.\\d+";
    private static final String DATE_PATTERN = "\\d{4}-[\\d?]{2}-[\\d?]{2}";
    private static final Pattern FIRST_LINE_PATTERN = Pattern
            .compile("^# (" + PROJECT_NAME_PATTERN + ") (" + VERSION_PATTERN + "), released (" + DATE_PATTERN + ")$");
    private static final Pattern SECTION_HEADING_PATTERN = Pattern.compile("\\s*##\\s.*");
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * Read a {@link ChangesFile} from disk.
     * 
     * @param file file to read
     * @return read {@link ChangesFile}
     */
    public ChangesFile read(final Path file) {
        try (final var fileReader = new BufferedReader(new FileReader(file.toFile()))) {
            return read(file, fileReader);
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-39")
                    .message("Failed to read changes file {{file}}.").parameter("file", file.toString()).toString(),
                    exception);
        }
    }

    ChangesFile read(final Path file, final BufferedReader fileReader) throws IOException {
        String sectionHeader = null;
        String line;
        int lineCount = 0;
        final var builder = ChangesFile.builder();
        final List<String> lineBuffer = new ArrayList<>();
        while ((line = fileReader.readLine()) != null) {
            if (lineCount == 0) {
                parseFirstLine(file, line, builder);
            } else {
                if (SECTION_HEADING_PATTERN.matcher(line).matches()) {
                    makeSection(sectionHeader, builder, lineBuffer);
                    sectionHeader = line;
                }
                lineBuffer.add(line);
            }
            lineCount++;
        }
        makeSection(sectionHeader, builder, lineBuffer);
        return builder.build();
    }

    private void parseFirstLine(final Path filePath, final String line, final Builder builder) {
        final Matcher matcher = FIRST_LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-171")
                    .message("Changes file {{file path}} contains invalid first line {{first line}}.", filePath, line)
                    .mitigation("Update first line so that it matches regex {{expected regular expression}}",
                            FIRST_LINE_PATTERN)
                    .toString());
        }
        builder.projectName(matcher.group(1)) //
                .projectVersion(matcher.group(2)) //
                .releaseDate(matcher.group(3));
    }

    private void makeSection(final String sectionHeader, final ChangesFile.Builder builder,
            final List<String> lineBuffer) {
        if (!lineBuffer.isEmpty()) {
            if (sectionHeader == null) {
                builder.setHeader(List.copyOf(lineBuffer));
            } else {
                builder.addSection(List.copyOf(lineBuffer));
            }
            lineBuffer.clear();
        }
    }

    /**
     * Write a {@link ChangesFile} back to disk.
     * 
     * @param changesFile     {@link ChangesFile} to write
     * @param destinationFile file to write to
     */
    public void write(final ChangesFile changesFile, final Path destinationFile) {
        try (final var writer = new BufferedWriter(new FileWriter(destinationFile.toFile()))) {
            write(changesFile, writer);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-41").message("Failed to write changes file {{file name}}.")
                            .parameter("file name", destinationFile).toString(),
                    exception);
        }
    }

    void write(final ChangesFile changesFile, final Writer writer) throws IOException {
        writeFirstLine(writer, changesFile);
        writeSection(writer, changesFile.getHeaderSectionLines());
        for (final ChangesFileSection section : changesFile.getSections()) {
            writeSection(writer, section.getContent());
        }
    }

    private void writeFirstLine(final Writer writer, final ChangesFile changesFile) throws IOException {
        writer.write("# " + changesFile.getProjectName() + " " + changesFile.getProjectVersion() + ", released "
                + changesFile.getReleaseDate());
        writer.write(LINE_SEPARATOR);
    }

    private void writeSection(final Writer fileWriter, final List<String> content) throws IOException {
        for (final String line : content) {
            fileWriter.write(line);
            fileWriter.write(LINE_SEPARATOR);
        }
    }
}
