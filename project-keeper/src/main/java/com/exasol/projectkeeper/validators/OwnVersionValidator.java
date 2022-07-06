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

/**
 * Validates if PK itself is up-to-date and performs self-update of called from a pom file.
 */
//[impl->dsn~verify-own-version~1]
//[impl->dsn~self-update~1]
public class OwnVersionValidator implements Validator {

    /**
     * @param currentVersion current version of PK in order to validate if there is an update available.
     * @param updater        instance of {@link Updater} in order to accept the latest version and to perform a
     *                       self-update by replacing the version of PK maven plugin in the user's pom file.
     * @return instance of {@link OwnVersionValidator} with the ability to perform a self-update
     */
    public static OwnVersionValidator forMavenPlugin(final String currentVersion, final Updater updater) {
        return new OwnVersionValidator(currentVersion, MavenRepository.mavenPlugin(), updater);
    }

    /**
     * @param currentVersion current version of PK in order to validate if there is an update available.
     * @return instance of {@link OwnVersionValidator} without the ability to perform a self-update
     */
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
            return new Version(version);
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

    /**
     * Users of {@link OwnVersionValidator} need to provide an implementation of this updater. The validator may then
     * pass the latest version to the updater. The updater in turn may then version of the project keeper plugin in the
     * user's pom file.
     */
    @FunctionalInterface
    public interface Updater {
        /**
         * By accepting the latest version retrieved from central maven repository this updater then creates and returns
         * a Fix in order to perform a self-update of PK. Note that this currently is only supported if PK is used as a
         * maven plugin.
         *
         * @param value latest version
         * @return Fix performing a self-update of PK.
         */
        Fix accept(String value);
    }
}
