package com.exasol.projectkeeper.validators.files;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.validators.VersionCollector;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile.Filename;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * Ensure there is no newer changes file than that for the current version of the current project.
 */
public class LatestChangesFileValidator implements Validator {
    final Path projectDirectory;
    final String projectVersion;

    /**
     * Create a new instance of {@link LatestChangesFileValidator}.
     *
     * @param projectDir     project's root directory
     * @param projectVersion version of the project to validate
     */
    public LatestChangesFileValidator(final Path projectDir, final String projectVersion) {
        this.projectDirectory = projectDir;
        this.projectVersion = projectVersion;
    }

    @Override
    public List<ValidationFinding> validate() {
        final List<ValidationFinding> empty = Collections.emptyList();
        final List<Filename> list = new VersionCollector(this.projectDirectory).collectVersions();
        if (list.isEmpty()) {
            return empty;
        }
        final Filename latest = list.get(0);
        if (latest.version().equals(this.projectVersion)) {
            return empty;
        }
        return List.of(SimpleValidationFinding.withMessage(ExaError.messageBuilder("E-PK-CORE-162")
                .message("Found newer changes file {{filename}}.", latest.filename().toString())
                .mitigation("Please remove changes file later than current version {{version}}.", this.projectVersion) //
                .toString()).build());
    }

}
