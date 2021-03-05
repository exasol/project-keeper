package com.exasol.projectkeeper.validators.changelog;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.exasol.errorreporting.ExaError;

/**
 * This class reads and writes a {@link ChangelogFile} from disk.
 */
public class ChangelogIO {
    private static final Pattern HEADLINE_PATTERN = Pattern.compile("\\s*##\\s.*");
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * Read a {@link ChangelogFile} from disk.
     * 
     * @param file file to read
     * @return read {@link ChangelogFile}
     */
    public ChangelogFile read(final Path file) {
        try (final BufferedReader fileReader = new BufferedReader(new FileReader(file.toFile()))) {
            String sectionHeader = null;
            String line;
            final ChangelogFile.Builder builder = ChangelogFile.builder();
            final List<String> lineBuffer = new ArrayList<>();
            while ((line = fileReader.readLine()) != null) {
                if (HEADLINE_PATTERN.matcher(line).matches()) {
                    makeSection(sectionHeader, builder, lineBuffer);
                    sectionHeader = line;
                }
                lineBuffer.add(line);
            }
            makeSection(sectionHeader, builder, lineBuffer);
            return builder.build();
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-39")
                    .message("Failed to read changelog file {{file}}.").parameter("file", file.toString()).toString(),
                    exception);
        }
    }

    private void makeSection(final String sectionHeader, final ChangelogFile.Builder builder,
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
     * Write a {@link ChangelogFile} back to disk.
     * 
     * @param changelog       {@link ChangelogFile} to write
     * @param destinationFile file to write to
     */
    public void write(final ChangelogFile changelog, final Path destinationFile) {
        try (final BufferedWriter fileWriter = new BufferedWriter(new FileWriter(destinationFile.toFile()))) {
            writeSection(fileWriter, changelog.getHeader());
            for (final ChangelogSection section : changelog.getSections()) {
                writeSection(fileWriter, section.getContent());
            }
            fileWriter.flush();
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-41").message("Failed to write changes file {{file name}}.")
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
