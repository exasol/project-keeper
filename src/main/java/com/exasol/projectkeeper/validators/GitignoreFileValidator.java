package com.exasol.projectkeeper.validators;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.*;

/**
 * This is a {@link Validator} for the README.md.
 */
// [impl->dsn~gitignore-validator~1]
public class GitignoreFileValidator extends AbstractFileContentValidator {
    private static final String NL = System.lineSeparator();
    private static final List<String> REQUIRED_LINES = List.of(".DS_Store", "*.swp", "local", ".dbeaver*", "**/*.log",
            ".directory", "venv/", ".idea", "*.iml", "target", ".cache", "dependency-reduced-pom.xml", ".project",
            ".classpath", "pom.xml.versionsBackup", "~*", "*.lock", "*.bak", "*.orig", "*.old", "*.md.html");

    /**
     * Create a new instance of {@link GitignoreFileValidator}.
     * 
     * @param projectDirectory project's root directory
     * @param excludedFiles    matcher for excluded files
     */
    public GitignoreFileValidator(final Path projectDirectory, final ExcludedFilesMatcher excludedFiles) {
        super(projectDirectory, Path.of(".gitignore"), excludedFiles);
    }

    @Override
    protected List<ValidationFinding> validateContent(final String content) {
        final List<ValidationFinding> findings = new ArrayList<>();
        final Set<String> entries = findEntries(content);
        final List<String> missingEntries = new ArrayList<>();
        for (final String requiredEntry : REQUIRED_LINES) {
            if (!entries.contains(requiredEntry)) {
                missingEntries.add(requiredEntry);
            }
        }
        if (!missingEntries.isEmpty()) {
            findings.add(ValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-76").message("Invalid content of .gitignore.")
                            .mitigation("Please add the following required entries: {{list of missing entries}}",
                                    missingEntries)
                            .toString())
                    .andFix(getAddMissingEntriesFix(content, missingEntries)).build());
        }
        return findings;
    }

    private Set<String> findEntries(final String content) {
        final Set<String> entries = new HashSet<>();
        for (final String line : content.split("\n")) {
            entries.add(line.trim());
        }
        return entries;
    }

    private ValidationFinding.Fix getAddMissingEntriesFix(final String content, final List<String> missingEntries) {
        return log -> {
            final String newContent = content + NL + String.join(NL, missingEntries);
            try {
                Files.writeString(getAbsoluteFilePath(), newContent);
            } catch (final IOException exception) {
                throw new UncheckedIOException(
                        ExaError.messageBuilder("E-PK-77").message("Failed to write fixed .gitignore file.").toString(),
                        exception);
            }
        };
    }

    @Override
    protected String getTemplate() {
        return String.join(NL, REQUIRED_LINES);
    }
}
