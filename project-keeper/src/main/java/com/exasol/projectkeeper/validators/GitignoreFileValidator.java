package com.exasol.projectkeeper.validators;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * This is a {@link Validator} for the {@code .gitignore} file.
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
     */
    public GitignoreFileValidator(final Path projectDirectory) {
        super(projectDirectory, Path.of(".gitignore"));
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
            findings.add(SimpleValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-CORE-76").message("Invalid content of .gitignore.")
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

    private SimpleValidationFinding.Fix getAddMissingEntriesFix(final String content,
            final List<String> missingEntries) {
        return (Logger log) -> {
            final String newContent = content + NL + String.join(NL, missingEntries);
            try {
                Files.writeString(getAbsoluteFilePath(), newContent);
            } catch (final IOException exception) {
                throw new UncheckedIOException(ExaError.messageBuilder("E-PK-CORE-77")
                        .message("Failed to write fixed .gitignore file.").toString(), exception);
            }
        };
    }

    @Override
    protected String getTemplate() {
        return String.join(NL, REQUIRED_LINES);
    }
}
