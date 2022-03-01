package com.exasol.projectkeeper.validators.changesfile;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.exasol.errorreporting.ExaError;

/**
 * This class reads and writes a {@link ChangesFile} from disk.
 */
public class ChangesFileIO {
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
            String sectionHeader = null;
            String line;
            final var builder = ChangesFile.builder();
            final List<String> lineBuffer = new ArrayList<>();
            while ((line = fileReader.readLine()) != null) {
                if (SECTION_HEADING_PATTERN.matcher(line).matches()) {
                    makeSection(sectionHeader, builder, lineBuffer);
                    sectionHeader = line;
                }
                lineBuffer.add(line);
            }
            makeSection(sectionHeader, builder, lineBuffer);
            return builder.build();
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-39")
                    .message("Failed to read changes file {{file}}.").parameter("file", file.toString()).toString(),
                    exception);
        }
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
        try (final var fileWriter = new BufferedWriter(new FileWriter(destinationFile.toFile()))) {
            writeSection(fileWriter, changesFile.getHeaderSectionLines());
            for (final ChangesFileSection section : changesFile.getSections()) {
                writeSection(fileWriter, section.getContent());
            }
            fileWriter.flush();
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-41").message("Failed to write changes file {{file name}}.")
                            .parameter("file name", destinationFile).toString(),
                    exception);
        }
    }

    private void writeSection(final BufferedWriter fileWriter, final List<String> content) throws IOException {
        for (final String line : content) {
            fileWriter.write(line);
            fileWriter.write(LINE_SEPARATOR);
        }
    }
}
