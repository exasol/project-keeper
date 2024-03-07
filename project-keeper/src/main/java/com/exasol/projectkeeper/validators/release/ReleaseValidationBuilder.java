package com.exasol.projectkeeper.validators.release;

import java.nio.file.Path;
import java.util.List;

import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileIO;

/**
 * This class creates {@link Validator}s that check if the project satisfies all preconditions for a release.
 */
public class ReleaseValidationBuilder {

    private final String projectVersion;
    private final Path projectDirectory;
    private final ChangesFileIO changesFileIO;
    private final String repoName;

    /**
     * Create a new instance.
     * 
     * @param repoName         the project's repository name within the GitHub organization {@code exasol}
     * @param projectVersion   the project's version
     * @param projectDirectory the project's directory
     */
    public ReleaseValidationBuilder(final String repoName, final String projectVersion, final Path projectDirectory) {
        this(repoName, projectVersion, projectDirectory, new ChangesFileIO());
    }

    ReleaseValidationBuilder(final String repoName, final String projectVersion, final Path projectDirectory,
            final ChangesFileIO changesFileIO) {
        this.repoName = repoName;
        this.projectVersion = projectVersion;
        this.projectDirectory = projectDirectory;
        this.changesFileIO = changesFileIO;
    }

    /**
     * Create a list of validators for checking the preconditions for a release.
     * 
     * @return validators
     */
    public List<Validator> validators() {
        final Path changesFilePath = projectDirectory.resolve(ChangesFile.getPathForVersion(projectVersion));
        final ChangesFile changesFile = changesFileIO.read(changesFilePath);
        return List.of(new ChangesFileReleaseValidator(repoName, changesFilePath, changesFile));
    }
}
