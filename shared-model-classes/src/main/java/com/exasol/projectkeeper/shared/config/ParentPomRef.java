package com.exasol.projectkeeper.shared.config;

/**
 * Reference to a parent pom of a maven source.
 * 
 * @param groupId      Maven group ID of the parent POM
 * @param artifactId   Maven artifact ID of the parent POM
 * @param version      Maven version of the parent POM
 * @param relativePath relative path to the parent POM file
 */
public record ParentPomRef(String groupId, String artifactId, String version, String relativePath) {

    /**
     * Get group ID.
     * 
     * @return Maven group ID of the parent POM
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Get artifact ID.
     * 
     * @return Maven artifact ID of the parent POM
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Get version.
     * 
     * @return Maven version of the parent POM
     */
    public String getVersion() {
        return version;
    }

    /**
     * Get relative path.
     * 
     * @return relative path to the parent POM file
     */
    public String getRelativePath() {
        return relativePath;
    }
}
