package com.exasol.projectkeeper.validators.changesfile.dependencies;

import com.exasol.projectkeeper.shared.dependencychanges.*;

/**
 * This class renders dependency changes in the format of the Exasol changes file.
 */
class DependencyChangeRenderer {

    /**
     * Render a dependency change in the format of the Exasol changes file.
     * 
     * @param dependencyChange dependency change to render
     * @return rendered string
     */
    public String render(final DependencyChange dependencyChange) {
        final var visitor = new RenderVisitor();
        dependencyChange.accept(visitor);
        return visitor.getRendered();
    }

    private static class RenderVisitor implements DependencyChangeVisitor {
        private String rendered;

        @Override
        public void visit(final NewDependency addedDependency) {
            this.rendered = "* Added " + renderDependencyChange(addedDependency);
        }

        @Override
        public void visit(final RemovedDependency removedDependency) {
            this.rendered = "* Removed " + renderDependencyChange(removedDependency);
        }

        @Override
        public void visit(final UpdatedDependency updatedDependency) {
            this.rendered = "* Updated " + renderDependencyChange(updatedDependency) + " to `"
                    + updatedDependency.getNewVersion() + "`";
        }

        private String renderDependencyChange(final DependencyChange addedDependency) {
            return "`" + addedDependency.getGroupId() + ":" + addedDependency.getArtifactId() + ":"
                    + addedDependency.getVersion() + "`";
        }

        /**
         * Get the rendered result.
         *
         * @return rendered result
         */
        public String getRendered() {
            return this.rendered;
        }
    }
}
