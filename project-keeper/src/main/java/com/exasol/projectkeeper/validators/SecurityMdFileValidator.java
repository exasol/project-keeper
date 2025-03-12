package com.exasol.projectkeeper.validators;

import static java.util.Collections.emptyList;

import java.nio.file.Path;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

// [impl -> dsn~security.md-file-validator~1]
public class SecurityMdFileValidator extends AbstractFileContentValidator {

    private static final Path PATH = Path.of("SECURITY.md");
    private final String repoName;

    public SecurityMdFileValidator(final Path projectDirectory, final String repoName) {
        super(projectDirectory, PATH);
        this.repoName = repoName;
    }

    @Override
    protected List<ValidationFinding> validateContent(final String content) {
        if (getTemplate().equals(content)) {
            return emptyList();
        }
        return List.of(SimpleValidationFinding
                .withMessage(ExaError.messageBuilder("E-PK-CORE-210")
                        .message("File {{path}} has outdated content", PATH).toString())
                .andFix(getCreateFileFix())
                .build());
    }

    @Override
    protected String getTemplate() {
        return """
                # Security

                If you believe you have found a new security vulnerability in this repository, please report it to us as follows.

                ## Reporting Security Issues

                * Please do **not** report security vulnerabilities through public GitHub issues.

                * Please create a draft security advisory on the Github page: the reporting form is under `> Security > Advisories`. The URL is https://github.com/exasol/%s/security/advisories/new.

                * If you prefer to email, please send your report to `infosec@exasol.com`.

                ## Guidelines

                * When reporting a vulnerability, please include as much information as possible, including the complete steps to reproduce the issue.

                * Avoid sending us executables.

                * Feel free to include any script you wrote and used but avoid sending us scripts that download and run binaries.

                * We will prioritise reports that show how the exploits work in realistic environments.

                * We prefer all communications to be in English.

                * We do not offer financial rewards. We are happy to acknowledge your research publicly when possible.
                """
                .formatted(repoName);
    }
}
