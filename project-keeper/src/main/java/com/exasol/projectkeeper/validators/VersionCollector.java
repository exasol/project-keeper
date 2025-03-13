package com.exasol.projectkeeper.validators;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileName;

/**
 * This class list all project-versions by scanning the doc/changes/ folder.
 */
public class VersionCollector {
    private final Path projectDirectory;

    /**
     * Create a new instance of {@link VersionCollector}.
     *
     * @param projectDirectory projects root directory.
     */
    public VersionCollector(final Path projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    /**
     * List all changes files in folder doc/changes/.
     *
     * @return list of changes files
     */
    public List<ChangesFileName> collectChangesFiles() {
        try (final Stream<Path> filesStream = Files.walk(this.projectDirectory.resolve(Path.of("doc", "changes")))) {
            return filesStream //
                    .map(ChangesFileName::from) //
                    .flatMap(Optional::stream) //
                    .sorted(Comparator.reverseOrder()) //
                    .toList();
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-66")
                            .message("Failed to collect versions from files in folder doc/changes/.").toString(),
                    exception);
        }
    }
}
