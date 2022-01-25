package com.exasol.projectkeeper.validators;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.exasol.errorreporting.ExaError;

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
     * List all project-versions by scanning the doc/changes/ folder.
     * 
     * @return list of project versions
     */
    public List<String> collectVersions() {
        try (final Stream<Path> filesStream = Files.walk(this.projectDirectory.resolve(Path.of("doc", "changes")))) {
            return filesStream.map(path -> path.getFileName().toString())
                    .filter(fileName -> fileName.startsWith("changes_"))
                    .map(fileName -> fileName.replace("changes_", "").replace(".md", "")).collect(Collectors.toList());
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-66")
                    .message("Failed to collect versions from changes files.").toString(), exception);
        }
    }
}
