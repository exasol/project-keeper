package com.exasol.projectkeeper.validators.changelog;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.validators.AbstractFileContentValidator;
import com.exasol.projectkeeper.validators.VersionLister;

//[impl->dsn~verify-changelog-file~1]
public class ChangelogFileValidator extends AbstractFileContentValidator {
    private final Path projectDirectory;

    /**
     * Create a new instance of {@link ChangelogFileValidator}.
     * 
     * @param projectDirectory project's root directory
     */
    public ChangelogFileValidator(final Path projectDirectory) {
        super(projectDirectory, Path.of("doc/changes/changelog.md"));
        this.projectDirectory = projectDirectory;
    }

    @Override
    protected List<ValidationFinding> validateContent(final String content) {
        final String expectedContent = getTemplate();
        if (!content.trim().equals(expectedContent.trim())) {
            return List.of(ValidationFinding.withMessage(ExaError.messageBuilder("E-PK-69")
                    .message("The changelog.md file has an outdated content. Expected content: {{expected}}",
                            expectedContent)
                    .toString()).andFix(getCreateFileFix()).build());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    protected String getTemplate() {
        final List<String> versions = new VersionLister(this.projectDirectory).listVersions();
        return new ChangelogFileGenerator().generate(versions);
    }
}
