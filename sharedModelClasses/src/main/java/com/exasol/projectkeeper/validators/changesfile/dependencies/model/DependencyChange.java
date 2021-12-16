package com.exasol.projectkeeper.validators.changesfile.dependencies.model;

/**
 * Interface for classes that represent dependency changes (add; update or remove).
 */
public interface DependencyChange {
    public String getGroupId();

    public String getArtifactId();

    public String getVersion();

    public void accept(DependencyChangeVisitor visitor);
}
