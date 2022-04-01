package com.exasol.projectkeeper;

import lombok.Data;

/**
 * This class groups information about a repository.
 */
@Data
public class RepoInfo {
    /** Reference to a parent pom or {@code null}. */
    private final String repoName;
    /** Name of the project's license. */
    private final String licenseName;
}
