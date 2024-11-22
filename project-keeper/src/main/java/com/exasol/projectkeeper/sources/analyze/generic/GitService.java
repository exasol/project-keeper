package com.exasol.projectkeeper.sources.analyze.generic;

import java.nio.file.Path;

import com.exasol.projectkeeper.shared.repository.GitRepository;

/**
 * Separate class to enable mocking access to Git repository.
 */
public class GitService {
    /** Create a new instance */
    public GitService() {
        // Empty constructor required by javadoc
    }

    /**
     * Get a Git repository.
     * 
     * @param folder root folder of the current project
     * @return {@link GitRepository}
     */
    public GitRepository getRepository(final Path folder) {
        return GitRepository.open(folder);
    }
}
