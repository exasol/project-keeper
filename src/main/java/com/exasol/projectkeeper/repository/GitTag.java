package com.exasol.projectkeeper.repository;

import org.eclipse.jgit.revwalk.RevTag;

/**
 * This class represents a git tag. Currently it's a wrapper around jgit's {@link RevTag}. It's purpose is to abstract
 * over the used git library.
 */
public class GitTag {
    private final String tagName;

    /**
     * Create a new instance of {@link GitTag}.
     * 
     * @param tagName name of the tag
     */
    GitTag(final String tagName) {
        this.tagName = tagName;
    }

    /**
     * Get the name of this tag.
     * 
     * @return name of this tag
     */
    public String getName() {
        return this.tagName;
    }
}
