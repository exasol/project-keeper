package com.exasol.projectkeeper.github;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;

/**
 * This class provides output parameters for a GitHub workflow step by appending the parameters to a file. See <a href=
 * "https://docs.github.com/en/actions/using-workflows/workflow-commands-for-github-actions#setting-an-output-parameter">GitHub
 * workflow documentation</a> for details.
 */
// [impl->dsn~verify-modes.output-parameters~1]
class FileContentProvider implements WorkflowOutput {
    private static final Logger LOG = Logger.getLogger(FileContentProvider.class.getName());
    private final KeyValuePairFormatter formatter = new KeyValuePairFormatter();
    private final Writer writer;
    private final Path outputPath;

    public FileContentProvider(final Writer writer, final Path outputPath) {
        this.writer = writer;
        this.outputPath = outputPath;
    }

    static WorkflowOutput create(final Path outputPath) {
        LOG.info(() -> "Creating new file output publisher for path '" + outputPath + "'");
        try {
            return new FileContentProvider(Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND), outputPath);
        } catch (final IOException exception) {
            throw new UncheckedIOException(ExaError.messageBuilder("E-PK-CORE-188")
                    .message("Failed to open {{output file}} for appending: {{error message}}", outputPath,
                            exception.getMessage())
                    .toString(), exception);
        }
    }

    @Override
    public void publish(final String key, final String value) {
        final String content = formatter.format(key, value);
        LOG.finest(() -> "Publishing content '" + content + "'");
        write(content + "\n");
    }

    private void write(final String content) {
        try {
            writer.write(content);
        } catch (final IOException exception) {
            throw new UncheckedIOException(ExaError.messageBuilder("E-PK-CORE-189")
                    .message("Failed to write content {{content}} to file {{output file}}: {{error message}}", content,
                            outputPath, exception.getMessage())
                    .toString(), exception);
        }
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (final IOException exception) {
            throw new UncheckedIOException(ExaError.messageBuilder("E-PK-CORE-187")
                    .message("Failed to close file {{output file}} after writing: {{error message}}", outputPath,
                            exception.getMessage())
                    .toString(), exception);
        }
    }
}
