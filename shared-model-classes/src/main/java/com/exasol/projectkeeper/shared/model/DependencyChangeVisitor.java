package com.exasol.projectkeeper.shared.model;

/**
 * Visitor for the {@link DependencyChange} class structure.
 */
public interface DependencyChangeVisitor {

    /**
     * Visit {@link NewDependency}.
     * 
     * @param addedDependency added dependency
     */
    public void visit(NewDependency addedDependency);

    /**
     * Visit {@link RemovedDependency}.
     * 
     * @param removedDependency removed dependency
     */
    public void visit(RemovedDependency removedDependency);

    /**
     * Visit {@link UpdatedDependency}.
     * 
     * @param updatedDependency updated dependency
     */
    public void visit(UpdatedDependency updatedDependency);
}
