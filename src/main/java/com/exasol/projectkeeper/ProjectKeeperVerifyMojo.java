package com.exasol.projectkeeper;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.exasol.errorreporting.ExaError;

/**
 * Entry point for the verify goal.
 * <p>
 * Run using {@code mvn project-keeper:verify}
 * </p>
 */
@Mojo(name = "verify", defaultPhase = LifecyclePhase.PACKAGE)
// [impl->dsn~mvn-verify-goal~1]
public class ProjectKeeperVerifyMojo extends AbstractProjectKeeperMojo {

    private static final String INVALID_STRUCTURE_MESSAGE = "This projects structure does not conform with the template.";

    @Override
    public void execute() throws MojoFailureException {
        if (isEnabled()) {
            final List<ValidationFinding> findings = getValidators().stream()
                    .flatMap(validator -> validator.validate().stream()).collect(Collectors.toList());
            final Log log = getLog();
            findings.forEach(finding -> log.error(finding.getMessage()));
            final boolean hasFindingsWithFix = findings.stream().anyMatch(ValidationFinding::hasFix);
            final boolean hasFindingsWithoutFix = findings.stream().anyMatch(finding -> !finding.hasFix());
            failIfValidationFailed(hasFindingsWithFix, hasFindingsWithoutFix);
        }
    }

    private void failIfValidationFailed(final boolean hasFindingsWithFix, final boolean hasFindingsWithoutFix)
            throws MojoFailureException {
        if (hasFindingsWithoutFix && hasFindingsWithFix) {
            throw new MojoFailureException(ExaError.messageBuilder("E-PK-24").message(INVALID_STRUCTURE_MESSAGE)
                    .mitigation(
                            "You can automatically fix some of the issues by running mvn project-keeper:fix but some also need to be fixed manually.")
                    .toString());
        } else if (hasFindingsWithFix) {
            throw new MojoFailureException(ExaError.messageBuilder("E-PK-6").message(INVALID_STRUCTURE_MESSAGE)
                    .mitigation("Run mvn project-keeper:fix to fix the issues automatically.").toString());

        } else if (hasFindingsWithoutFix) {
            throw new MojoFailureException(ExaError.messageBuilder("E-PK-25").message(INVALID_STRUCTURE_MESSAGE)
                    .mitigation("Please fix it manually.").toString());
        }
    }
}
