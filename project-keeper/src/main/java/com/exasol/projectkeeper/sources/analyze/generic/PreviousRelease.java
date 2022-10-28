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

    private final GitService git;
    private Path projectDir;
    private String version;
    private Path file;

    /**
     * Constructor for usage in tests.
     *
     * @param git {@link GitService} enabling to mock access to Git repository
     */
    public PreviousRelease(final GitService git) {
        this.git = git;
    }

    /**
     * @param projectDir root folder of the current project
     * @return this for fluent programming
     */
    public PreviousRelease projectDir(final Path projectDir) {
        this.projectDir = projectDir;
        return this;
    }

    /**
     * @param version current version of the current project
     * @return this for fluent programming
     */
    public PreviousRelease currentVersion(final String version) {
        this.version = version;
        return this;
    }

    /**
     * @param relative relative path of a file in the Git repository of the project.
     * @return this for fluent programming
     */
    public PreviousRelease file(final Path relative) {
        this.file = relative;
        return this;
    }

    /**
     * @return content of the specified file in the previous version of the project or {@code Optional.empty()} if there
     *         is no previous version or the file does not exist in the previous version.
     */
    public Optional<String> getContent() {
        try (final GitRepository repo = this.git.getRepository(this.projectDir)) {
            return repo.findLatestReleaseCommit(this.version) //
                    .map(tag -> getContent(repo, tag));
        }
    }

    private String getContent(final GitRepository repo, final TaggedCommit tag) {
        try {
            return repo.getFileFromCommit(this.file, tag.getCommit());
        } catch (final FileNotFoundException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-134")
                    .message("File {{module file}} does not exist at tag {{tag}}", //
                            this.file, tag.getTag())
                    .toString(), exception);
        }
    }
}
