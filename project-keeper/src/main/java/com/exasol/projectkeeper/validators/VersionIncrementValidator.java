package com.exasol.projectkeeper.validators;

import static java.util.Collections.emptyList;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.shared.repository.GitRepository;
import com.exasol.projectkeeper.shared.repository.TaggedCommit;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;
import com.vdurmont.semver4j.Semver;

/**
 * This {@link Validator} verifies that the current version number was correctly incremented compared to the previous
 * version.
 */
public class VersionIncrementValidator implements Validator {
    private static final Logger LOG = Logger.getLogger(VersionIncrementValidator.class.getName());
    private final String projectVersion;
    private final GitRepository gitRepository;

    /**
     * Create a new instance.
     * 
     * @param projectVersion the project's version number
     * @param projectDir     the project's directory
     */
    public VersionIncrementValidator(final String projectVersion, final Path projectDir) {
        this(projectVersion, GitRepository.open(projectDir));
    }

    VersionIncrementValidator(final String projectVersion, final GitRepository gitRepository) {
        this.projectVersion = projectVersion;
        this.gitRepository = gitRepository;
    }

    // [impl->dsn~version-increment-validator~1]
    @Override
    public List<ValidationFinding> validate() {
        final Optional<TaggedCommit> latestReleaseCommit = gitRepository.findLatestReleaseCommit(null);
        if (latestReleaseCommit.isEmpty()) {
            LOG.info(() -> "No git tag exists, accepting " + this.projectVersion + " as valid.");
            return emptyList();
        }
        final String previousGitTag = latestReleaseCommit.get().getTag();
        if (isValidSuccessor(previousGitTag)) {
            return emptyList();
        }
        return List.of(SimpleValidationFinding.withMessage(ExaError.messageBuilder("E-PK-CORE-184")
                .message("Project version {{current version}} is not a valid successor of {{previous version}}.",
                        this.projectVersion, previousGitTag)
                .mitigation("Only increment one of major, minor or patch version.").toString()).build());
    }

    private boolean isValidSuccessor(final String previousGitTag) {
        final Semver current = parseVersion(this.projectVersion);
        final Semver previous = parseVersion(previousGitTag);
        return previous.nextMajor().equals(current) //
                || previous.nextMinor().equals(current) //
                || previous.nextPatch().equals(current);
    }

    private Semver parseVersion(String version) {
        if (version.startsWith("v")) {
            version = version.substring(1);
        }
        return new Semver(version);
    }
}
