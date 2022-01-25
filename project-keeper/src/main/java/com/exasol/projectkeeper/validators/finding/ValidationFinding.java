package com.exasol.projectkeeper.validators.finding;

/**
 * Interface for validation findings.
 */
public interface ValidationFinding {

    /**
     * Accept a visitor.
     * 
     * @param visitor visitor to accept.
     */
    public void accept(ValidationFinding.Visitor visitor);

    /**
     * Visitor for {@link ValidationFinding}.
     */
    public interface Visitor {
        /**
         * Visit.
         *
         * @param finding the finding
         */
        void visit(SimpleValidationFinding finding);

        /**
         * Visit.
         *
         * @param finding the finding
         */
        void visit(ValidationFindingGroup finding);
    }
}
