package com.exasol.projectkeeper.shared.dependencychanges;

/**
 * This class represents an added dependency.
 * 
 * @param groupId    the group ID of the added dependency. May be null if the package system does not use group IDs
 *                   (e.g. for Golang)
 * @param artifactId the artifact ID of the added dependency
 * @param version    the version of the added dependency
 */
public record NewDependency(String groupId, String artifactId, String version) implements DependencyChange {

    /** @return group id */
    @Override
    public String getGroupId() {
        return groupId;
    }

    /** @return artifact id */
    @Override
    public String getArtifactId() {
        return artifactId;
    }

    /** @return version */
    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void accept(final DependencyChangeVisitor visitor) {
        visitor.visit(this);
    }
}
