package com.exasol.projectkeeper.validators;

import java.io.IOException;
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

    private static List<ValidationFinding> findings(final Fix fix, final String message) {
        final Builder builder = SimpleValidationFinding.withMessage(message).optional(true);
        if (fix != null) {
            builder.andFix(fix);
        }
        return List.of(builder.build());
    }

    // instance fields and methods

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
            final Version current = parseVersion(this.currentVersion,
                    new ValidationException(ExaError.messageBuilder("W-PK-CORE-152")
                            .message("Could not validate version of project-keeper.") //
                            .message(" Unsupported format of own version {{version}}.", this.currentVersion) //
                            .toString()));
            final Version latest = getLatestVersion(this.mavenRepository);
            if (current.isGreaterOrEqualThan(latest)) {
                return Collections.emptyList();
            }

            final Fix fix = (this.updater == null ? null : this.updater.accept(latest.toString()));
            return findings(fix, ExaError.messageBuilder("W-PK-CORE-153") //
                    .message("Project-keeper version {{current}} is outdated.", current) //
                    .mitigation("Please update project-keeper to latest version {{latest}}.", latest) //
                    .toString());
        } catch (final ValidationException e) {
            return e.getFindings();
        }
    }

    private Version parseVersion(final String version, final ValidationException exception) throws ValidationException {
        try {
            return Version.of(version);
        } catch (final UnsupportedVersionFormatException e) {
            throw exception;
        }
    }

    private Version getLatestVersion(final MavenRepository repo) throws ValidationException {
        try {
            final String versionString = repo.getLatestVersion();
            return parseVersion(versionString, new ValidationException(ExaError.messageBuilder("W-PK-CORE-154") //
                    .message("Could not detect latest available version of project-keeper.") //
                    .message(" Unsupported format of latest version from Maven repository: {{version}}.", versionString) //
                    .toString()));
        } catch (final IOException | JsonContentException e) {
            throw new ValidationException(ExaError.messageBuilder("W-PK-CORE-155") //
                    .message("Could not detect latest available version of project-keeper.") //
                    .message(" {{message|uq}}.", e.getMessage()) //
                    .mitigation("Please check network connection and response from {{url}}", repo.getUrl()) //
                    .toString());
        }
    }

    private static class ValidationException extends Exception {
        private static final long serialVersionUID = 1L;
        @Getter
        private final List<ValidationFinding> findings;

        public ValidationException(final String message) {
            this.findings = OwnVersionValidator.findings(null, message);
        }
    }

    @FunctionalInterface
    public interface Updater {
        Fix accept(String value);
    }
}
