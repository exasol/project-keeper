package com.exasol.projectkeeper.dependencyupdate;

import java.nio.file.Path;

import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile.Builder;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileIO;

/**
 * This class updates the the changesfile (e.g. {@code doc/changes/changes_1.2.0.md} for a given version, adding
 * information about fixed vulnerabilities in dependencies.
 */
class ChangesFileUpdater {
    private final ChangesFileIO changesFileIO;
    private final Path projectDir;
    private final VulnerabilityInfoProvider vulnerabilityInfoProvider;

    ChangesFileUpdater(final VulnerabilityInfoProvider vulnerabilityInfoProvider, final ChangesFileIO changesFileIO,
            final Path projectDir) {
        this.vulnerabilityInfoProvider = vulnerabilityInfoProvider;
        this.changesFileIO = changesFileIO;
        this.projectDir = projectDir;
    }

    void updateChanges(final String version) {
        final Path changesFilePath = getChangesFilePath(version);
        final ChangesFile changesFile = changesFileIO.read(changesFilePath);
        final ChangesFile updatedChanges = update(changesFile);
        changesFileIO.write(updatedChanges, changesFilePath);
    }

    private ChangesFile update(final ChangesFile changesFile) {
        final Builder builder = changesFile.toBuilder();
        // Changes file will be updated in the next PR
        return builder.build();
    }

    private Path getChangesFilePath(final String version) {
        return projectDir.resolve(ChangesFile.getPathForVersion(version));
    }
}
