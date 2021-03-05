package com.exasol.projectkeeper.validators.changelog.dependencies;

/**
 * Visitor for the {@link DependencyChange} class structure.
 */
public interface DependencyChangeVisitor {

    /**
     * Visit {@link DependencyAdd}.
     * 
     * @param addedDependency added dependency
     */
    public void visit(DependencyAdd addedDependency);

    /**
     * Visit {@link DependencyRemove}.
     * 
     * @param removedDependency removed dependency
     */
    public void visit(DependencyRemove removedDependency);

    /**
     * Visit {@link DependencyUpdate}.
     * 
     * @param updatedDependency updated dependency
     */
    public void visit(DependencyUpdate updatedDependency);
}
