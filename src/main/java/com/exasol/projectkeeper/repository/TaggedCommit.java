package com.exasol.projectkeeper.repository;

import org.eclipse.jgit.revwalk.RevCommit;

/**
 * This class represents a git commit that is tagged by an git tag.
 */
public class TaggedCommit {
    private final GitCommit commit;
    private final String tag;

    /**
     * Create a new instance of {@link TaggedCommit}.
     * 
     * @param commit git commit
     * @param tag    git tag
     */
    public TaggedCommit(final RevCommit commit, final String tag) {
        this.commit = new GitCommit(commit);
        this.tag = tag;
    }

    /**
     * Get the git tag.
     * 
     * @return git tag
     */
    public String getTag() {
        return this.tag;
    }

    /**
     * Get the git commit.
     * 
     * @return git commit
     */
    public GitCommit getCommit() {
        return this.commit;
    }
}
