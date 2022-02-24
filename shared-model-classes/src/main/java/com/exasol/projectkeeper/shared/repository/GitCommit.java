package com.exasol.projectkeeper.shared.repository;

import org.eclipse.jgit.revwalk.RevCommit;

/**
 * This class represents a git commit. Currently it's a wrapper around jgit's {@link RevCommit}. It's purpose is to
 * abstract over the used git library.
 */
public class GitCommit {
    private final RevCommit commit;

    /**
     * Create a new instance of {@link GitCommit}.
     * 
     * @param commit jgit's {@link RevCommit} to wrap
     */
    GitCommit(final RevCommit commit) {
        this.commit = commit;
    }

    /**
     * Get the jgit's {@link RevCommit}
     * 
     * @return jgit's {@link RevCommit}
     */
    RevCommit getCommit() {
        return this.commit;
    }
}
