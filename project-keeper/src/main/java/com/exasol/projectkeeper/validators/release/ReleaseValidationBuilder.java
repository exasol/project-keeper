package com.exasol.projectkeeper.validators.release;

import java.nio.file.Path;
import java.util.List;

import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileIO;

public class ReleaseValidationBuilder {

    private final String projectVersion;
    private final Path projectDirectory;
    private final ChangesFileIO changesFileIO;

    public ReleaseValidationBuilder(final String projectVersion, final Path projectDirectory) {
        this(projectVersion, projectDirectory, new ChangesFileIO());
    }

    ReleaseValidationBuilder(final String projectVersion, final Path projectDirectory,
            final ChangesFileIO changesFileIO) {
        this.projectVersion = projectVersion;
        this.projectDirectory = projectDirectory;
        this.changesFileIO = changesFileIO;

    }

    public List<Validator> validators() {
        final Path changesFilePath = projectDirectory.resolve(ChangesFile.getPathForVersion(projectVersion));
        final ChangesFile changesFile = changesFileIO.read(changesFilePath);
        return List.of(new ChangesFileReleaseValidator(changesFilePath, changesFile));
    }
}
