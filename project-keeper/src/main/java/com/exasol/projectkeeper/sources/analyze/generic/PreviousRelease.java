package com.exasol.projectkeeper.sources.analyze.generic;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Optional;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.repository.GitRepository;
import com.exasol.projectkeeper.shared.repository.TaggedCommit;

/**
 * For a project in the specified local folder {@code projectDir} with the specified current version and backed by a Git
 * repository this class enables to get the content of files in the previous version.
 */
public class PreviousRelease {

    /**
     * @param projectDir     root folder of the current project
     * @param currentVersion current version of the current project
     */
    public static PreviousRelease from(final Path projectDir, final String currentVersion) {
        return new PreviousRelease(projectDir, currentVersion);
    }

    private final Path projectDir;
    private final String currentVersion;

    PreviousRelease(final Path projectDir, final String currentVersion) {
        this.projectDir = projectDir;
        this.currentVersion = currentVersion;
    }

    /**
     * @param relative relative path of a file in the Git repository of the project.
     * @return content of the specified file in the previous version of the project or {@code Optional.empty()} if there
     *         is no previous version.
     */
    public Optional<String> fileContent(final Path relative) {
        try (final GitRepository repo = GitRepository.open(this.projectDir)) {
            return repo.findLatestReleaseCommit(this.currentVersion).map(tag -> getContent(repo, relative, tag));
        }
    }

    private String getContent(final GitRepository repo, final Path relative, final TaggedCommit tag) {
        try {
            return repo.getFileFromCommit(relative, tag.getCommit());
        } catch (final FileNotFoundException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-134")
                    .message("File {{module file}} does not exist at tag {{tag}}", relative, tag.getTag()).toString(),
                    exception);
        }
    }
}
