package com.exasol.projectkeeper;

import java.util.stream.Collectors;

import org.apache.maven.plugin.MojoFailureException;
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
public class ProjectKeeperVerifyMojo extends AbstractProjectKeeperMojo {

    private static final String INVALID_STRUCTURE_MESSAGE = "This projects structure does not conform with the template.";

    @Override
    public void execute() throws MojoFailureException {
        final var findings = getValidators().stream().flatMap(validator -> validator.validate().stream())
                .collect(Collectors.toList());
        final var log = getLog();
        findings.forEach(finding -> log.error(finding.getMessage()));
        final var hadFindingsWithFix = findings.stream().anyMatch(ValidationFinding::hasFix);
        final var hadFindingsWithoutFix = findings.stream().anyMatch(finding -> !finding.hasFix());
        printValidationFailedMessageIfRequired(hadFindingsWithFix, hadFindingsWithoutFix);
    }

    private void printValidationFailedMessageIfRequired(final boolean hadFindingsWithFix,
            final boolean hadFindingsWithoutFix) throws MojoFailureException {
        if (hadFindingsWithoutFix && hadFindingsWithFix) {
            throw new MojoFailureException(ExaError.messageBuilder("E-PK-24").message(INVALID_STRUCTURE_MESSAGE)
                    .mitigation(
                            "You can automatically fix some of the issues by running mvn project-keeper:fix but some also need to be fixed manually.")
                    .toString());
        } else if (hadFindingsWithFix) {
            throw new MojoFailureException(ExaError.messageBuilder("E-PK-6").message(INVALID_STRUCTURE_MESSAGE)
                    .mitigation("You can automatically fix them by running mvn project-keeper:fix").toString());

        } else if (hadFindingsWithoutFix) {
            throw new MojoFailureException(ExaError.messageBuilder("E-PK-25").message(INVALID_STRUCTURE_MESSAGE)
                    .mitigation("Please fix it manually.").toString());
        }
    }
}
