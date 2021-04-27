package com.exasol.projectkeeper;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.maven.plugin.MojoExecutionException;
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
    public void execute() throws MojoExecutionException, MojoFailureException {
        final var hadFindingsWithFix = new AtomicBoolean(false);
        final var hadFindingsWithoutFix = new AtomicBoolean(false);
        getValidators().forEach(validator -> validator.validate(finding -> {
            getLog().error(finding.getMessage());
            if (finding.hasFix()) {
                hadFindingsWithFix.set(true);
            } else {
                hadFindingsWithoutFix.set(true);
            }
        }));
        if (hadFindingsWithoutFix.get() && hadFindingsWithFix.get()) {
            throw new MojoFailureException(ExaError.messageBuilder("E-PK-24").message(INVALID_STRUCTURE_MESSAGE)
                    .mitigation(
                            "You can automatically fix some of the issues by running mvn project-keeper:fix but some also need to be fixed manually.")
                    .toString());
        } else if (hadFindingsWithFix.get()) {
            throw new MojoFailureException(ExaError.messageBuilder("E-PK-6").message(INVALID_STRUCTURE_MESSAGE)
                    .mitigation("You can automatically fix them by running mvn project-keeper:fix").toString());

        } else if (hadFindingsWithoutFix.get()) {
            throw new MojoFailureException(ExaError.messageBuilder("E-PK-25").message(INVALID_STRUCTURE_MESSAGE)
                    .mitigation("Please fix it manually.").toString());
        }
    }
}
