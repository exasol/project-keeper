package com.exasol.projectkeeper.validators;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * This is a {@link com.exasol.projectkeeper.Validator} for the LICENSE file.
 * <p>
 * We implemented this validator in Java and not just as a file in the template resources, since it requires the current
 * year as parameter.
 * </p>
 */
//[impl->dsn~license-file-validator~1]
public class LicenseFileValidator extends AbstractFileContentValidator {
    /**
     * Create a new instance of {@link LicenseFileValidator}.
     *
     * @param projectDirectory project's root directory
     */
    public LicenseFileValidator(final Path projectDirectory) {
        super(projectDirectory, Path.of("LICENSE"));
    }

    @Override
    protected List<ValidationFinding> validateContent(final String content) {
        return Collections.emptyList();
    }

    @Override
    protected String getTemplate() {
        final Calendar calendar = Calendar.getInstance();
        return getTemplateFromResources().replace("!!!YEAR!!!", String.valueOf(calendar.get(Calendar.YEAR)));
    }

    private String getTemplateFromResources() {
        try (final InputStream templateStream = getClass().getClassLoader().getResourceAsStream("LICENSE-template")) {
            return new String(templateStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (final IOException | NullPointerException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-68")
                    .message("Failed to open LICENSE file template.").ticketMitigation().toString(), exception);
        }
    }
}
