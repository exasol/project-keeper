package com.exasol.projectkeeper;

/**
 * This class groups information about a repository.
 * 
 * @param repoName    Reference to a parent pom or {@code null}.
 * @param licenseName Name of the project's license.
 */
public record RepoInfo(String repoName, String licenseName) {

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

}
