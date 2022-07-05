package com.exasol.projectkeeper.validators;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.mavenrepo.MavenRepository;
import com.exasol.projectkeeper.mavenrepo.MavenRepository.JsonContentException;
import com.exasol.projectkeeper.mavenrepo.Version;
import com.exasol.projectkeeper.mavenrepo.Version.UnsupportedVersionFormatException;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding.Builder;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding.Fix;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

import lombok.Getter;

//[impl->dsn~verify-own-version~1]
//[impl->dsn~self-update~1]
public class OwnVersionValidator implements Validator {

    public static OwnVersionValidator forMavenPlugin(final String currentVersion, final Updater updater) {
        return new OwnVersionValidator(currentVersion, MavenRepository.mavenPlugin(), updater);
    }

    // fix is only possible if using PK from maven pom
    public static OwnVersionValidator forCli(final String currentVersion) {
        return new OwnVersionValidator(currentVersion, MavenRepository.cli(), null);
    }

    private final String currentVersion;
    @Getter
    private final MavenRepository mavenRepository;
    @Getter
    private final Updater updater;

    OwnVersionValidator(final String currentVersion, final MavenRepository repo, final Updater updater) {
        this.currentVersion = currentVersion;
        this.mavenRepository = repo;
        this.updater = updater;
    }

    @Override
    public List<ValidationFinding> validate() {
        try {
            final Version current = parseVersion("own version", this.currentVersion);
            final Version latest = parseVersion("latest version from Maven repository",
                    getLatestVersion(this.mavenRepository));
            if (current.isLessThan(latest)) {
                final Builder builder = SimpleValidationFinding.withMessage(ExaError.messageBuilder("W-PK-CORE-151") //
                        .message("Project-keeper version {{current}} is outdated.", current) //
                        .mitigation("Please update project-keeper to latest version {{latest}}.", latest) //
                        .toString()) //
                        .optional(true);
                if (this.updater != null) {
                    builder.andFix(this.updater.accept(latest.toString()));
                }
                return List.of(builder.build());
            }
            return Collections.emptyList();
        } catch (final ValidationException e) {
            return List.of(SimpleValidationFinding.withMessage(ExaError.messageBuilder("W-PK-CORE-152") //
                    .message("Could not detect latest available version of project-keeper due to {{message|uq}}.",
                            e.getMessage())
                    .toString()) //
                    .optional(true) //
                    .build());
        }
    }

    private Version parseVersion(final String context, final String version) throws ValidationException {
        try {
            return Version.of(version);
        } catch (final UnsupportedVersionFormatException e) {
            throw new ValidationException(MessageFormat.format("Unsupported format of {0}: ''{1}''", context, version));
        }
    }

    private String getLatestVersion(final MavenRepository repo) throws ValidationException {
        try {
            return repo.getLatestVersion();
        } catch (final IOException | JsonContentException e) {
            throw new ValidationException(ExaError.messageBuilder(e.getMessage() + ".") //
                    .mitigation("Please check network connection and response from {{url}}", repo.getUrl()) //
                    .toString());
        }
    }

    private static class ValidationException extends Exception {
        private static final long serialVersionUID = 1L;

        public ValidationException(final String message) {
            super(message);
        }
    }

    @FunctionalInterface
    public interface Updater {
        Fix accept(String value);
    }
}
