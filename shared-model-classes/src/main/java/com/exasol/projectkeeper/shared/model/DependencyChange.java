package com.exasol.projectkeeper.shared.model;

/**
 * Interface for classes that represent dependency changes (add; update or remove).
 */
public interface DependencyChange {

    /**
     * Get the artifact group id.
     * 
     * @return the artifact group id
     */
    public String getGroupId();

    /**
     * Get the artifact id.
     * 
     * @return the artifact id
     */
    public String getArtifactId();

    /**
     * Get the artifact version.
     * 
     * @return the artifact version
     */
    public String getVersion();

    /**
     * Accept a visitor.
     * 
     * @param visitor the visitor to accept
     */
    public void accept(DependencyChangeVisitor visitor);
}
