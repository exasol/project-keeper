package com.exasol.projectkeeper.shared.config;

import java.util.Objects;

/**
 * Reference to a parent pom of a maven source.
 */
public final class ParentPomRef {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String relativePath;

    /**
     * Create a new instance.
     * 
     * @param groupId      Maven group ID of the parent POM
     * @param artifactId   Maven artifact ID of the parent POM
     * @param version      Maven version of the parent POM
     * @param relativePath relative path to the parent POM file
     */
    public ParentPomRef(final String groupId, final String artifactId, final String version,
            final String relativePath) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.relativePath = relativePath;
    }

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

    @Override
    public String toString() {
        return "ParentPomRef [groupId=" + groupId + ", artifactId=" + artifactId + ", version=" + version
                + ", relativePath=" + relativePath + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version, relativePath);
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
        final ParentPomRef other = (ParentPomRef) obj;
        return Objects.equals(groupId, other.groupId) && Objects.equals(artifactId, other.artifactId)
                && Objects.equals(version, other.version) && Objects.equals(relativePath, other.relativePath);
    }
}
