package com.exasol.projectkeeper;

import java.util.Objects;

/**
 * This class groups information about a repository.
 */
public final class RepoInfo {
    /** Reference to a parent pom or {@code null}. */
    private final String repoName;
    /** Name of the project's license. */
    private final String licenseName;

    /**
     * Create a new {@link RepoInfo}.
     * 
     * @param repoName    repository name
     * @param licenseName license name
     */
    public RepoInfo(final String repoName, final String licenseName) {
        this.repoName = repoName;
        this.licenseName = licenseName;
    }

    /**
     * Get the repository name.
     * 
     * @return repository name
     */
    public String getRepoName() {
        return repoName;
    }

    /**
     * Get the license name.
     * 
     * @return license name
     */
    public String getLicenseName() {
        return licenseName;
    }

    @Override
    public String toString() {
        return "RepoInfo [repoName=" + repoName + ", licenseName=" + licenseName + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(repoName, licenseName);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RepoInfo other = (RepoInfo) obj;
        return Objects.equals(repoName, other.repoName) && Objects.equals(licenseName, other.licenseName);
    }
}
