package com.exasol.projectkeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import com.exasol.errorreporting.ExaError;

/**
 * Entry point for the fix goal.
 * <p>
 * Run using {@code mvn project-keeper:fix}
 * </p>
 */
@Mojo(name = "fix")
// [impl->dsn~mvn-fix-goal~1]
public class ProjectKeeperFixMojo extends AbstractProjectKeeperMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final var log = getLog();
        final List<ValidationFinding> unfixedFindings = new ArrayList<>();
        getValidators().forEach(validator -> validator.validate().forEach(finding -> {
            if (finding.hasFix()) {
                finding.getFix().fixError(log);
            } else {
                unfixedFindings.add(finding);
            }
        }));
        for (final ValidationFinding unfixedFinding : unfixedFindings) {
            log.warn("Could not auto-fix: " + unfixedFinding.getMessage());
        }
        if (!unfixedFindings.isEmpty()) {
            throw new MojoFailureException(ExaError.messageBuilder("E-PK-65").message(
                    "Could not fix all of the findings automatically. There are some leftovers that you need to fix by hand. (This is an error and not a warning sinc no one checks for warnings in maven builds...).")
                    .toString());
        }
    }
}
