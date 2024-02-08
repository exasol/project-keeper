package com.exasol.projectkeeper.validators.changesfile;

import java.io.*;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile.Builder;

/**
 * This class reads and writes a {@link ChangesFile} from disk.
 */
public class ChangesFileIO {
    private static final String CODE_NAME = "Code name:";
    private static final String PROJECT_NAME_PATTERN = "[\\w\\s-]+";
    private static final String VERSION_PATTERN = "\\d+\\.\\d+\\.\\d+";
    private static final String DATE_PATTERN = "\\d{4}-[\\d?]{2}-[\\d?]{2}";
    private static final Pattern FIRST_LINE_PATTERN = Pattern
            .compile("^# (" + PROJECT_NAME_PATTERN + ") (" + VERSION_PATTERN + "), released (" + DATE_PATTERN + ")$");
    private static final String LINE_SEPARATOR = "\n";

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
        return new Parser(file, fileReader).parse();
    }

    private static class Parser {
        final Path file;
        final BufferedReader reader;
        final Builder builder = ChangesFile.builder();
        ChangesFileSection.Builder currentSection;
        int lineCount = 0;

        Parser(final Path file, final BufferedReader reader) {
            this.file = file;
            this.reader = reader;
        }

        ChangesFile parse() throws IOException {
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line);
                lineCount++;
            }
            addSection();
            return builder.build();
        }

        private void parseLine(final String line) {
            if (lineCount == 0) {
                parseFirstLine(file, line);
                return;
            }
            if (line.startsWith(CODE_NAME)) {
                builder.codeName(line.substring(CODE_NAME.length()).trim());
                return;
            }
            if (line.startsWith("## ")) {
                addSection();
                currentSection = ChangesFileSection.builder(line);
                return;
            }
            if (currentSection != null) {
                currentSection.addLine(line);
            }
        }

        private void addSection() {
            if (currentSection == null) {
                return;
            }
            final ChangesFileSection section = currentSection.build();
            currentSection = null;
            switch (section.getHeading()) {
            case ChangesFile.SUMMARY_HEADING:
                builder.summary(section);
                break;
            case ChangesFile.DEPENDENCY_UPDATES_HEADING:
                builder.dependencyChangeSection(section);
                break;
            default:
                builder.addSection(section);
                break;
            }
        }

        private void parseFirstLine(final Path filePath, final String line) {
            final Matcher matcher = FIRST_LINE_PATTERN.matcher(line);
            if (!matcher.matches()) {
                throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-171")
                        .message("Changes file {{file path}} contains invalid first line {{first line}}.", filePath,
                                line)
                        .mitigation("Update first line so that it matches regex {{expected regular expression}}",
                                FIRST_LINE_PATTERN)
                        .toString());
            }
            builder.projectName(matcher.group(1)) //
                    .projectVersion(matcher.group(2)) //
                    .releaseDate(matcher.group(3));
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
        writeHeader(writer, changesFile);
        for (final ChangesFileSection section : changesFile.getSections()) {
            writeSection(writer, section);
        }
        final Optional<ChangesFileSection> dependencyChangeSection = changesFile.getDependencyChangeSection();
        if (dependencyChangeSection.isPresent()) {
            writer.write(dependencyChangeSection.get().toString());
            writer.write(LINE_SEPARATOR);
        }
    }

    private void writeHeader(final Writer writer, final ChangesFile changesFile) throws IOException {
        writer.write("# " + changesFile.getProjectName() + " " + changesFile.getProjectVersion() + ", released "
                + changesFile.getReleaseDate());
        writer.write(LINE_SEPARATOR);
        writer.write(LINE_SEPARATOR);
        writer.write(CODE_NAME + (changesFile.getCodeName() != null ? " " + changesFile.getCodeName() : ""));
        writer.write(LINE_SEPARATOR);
        writer.write(LINE_SEPARATOR);
        final Optional<ChangesFileSection> summarySection = changesFile.getSummarySection();
        if (summarySection.isPresent()) {
            writer.write(summarySection.get().toString());
            writer.write(LINE_SEPARATOR);
        }
    }

    private void writeSection(final Writer writer, final ChangesFileSection section) throws IOException {
        writer.write(section.getHeading());
        writer.write(LINE_SEPARATOR);
        for (final String line : section.getContent()) {
            writer.write(line);
            writer.write(LINE_SEPARATOR);
        }
    }
}
