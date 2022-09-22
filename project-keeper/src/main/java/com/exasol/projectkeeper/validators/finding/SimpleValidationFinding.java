package com.exasol.projectkeeper.validators.finding;

import static java.util.function.Predicate.not;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.ProjectKeeper;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * This class represents an error that was found during validation.
 */
@EqualsAndHashCode
@ToString
public class SimpleValidationFinding implements ValidationFinding {

    /**
     * Returns mandatory findings.
     *
     * @param findings list of finding to be filtered
     * @return mandatory findings
     */
    public static List<SimpleValidationFinding> blockers(final List<SimpleValidationFinding> findings) {
        return findings.stream() //
                .filter(not(SimpleValidationFinding::isOptional)) //
                .collect(Collectors.toList());
    }

    private final String message;
    @Accessors(fluent = true)
    private final boolean isOptional;
    private final Fix fix;

    /**
     * Create a new instance of {@link SimpleValidationFinding}.
     *
     * @param message    error message
     * @param isOptional whether fixing this finding is mandatory or finding can be accepted (for a while)
     * @param fix        function that fixes the error.
     */
    public SimpleValidationFinding(final String message, final boolean isOptional, final Fix fix) {
        this.message = message;
        this.isOptional = isOptional;
        this.fix = fix;
    }

    /**
     * Get a {@link Builder} for {@link SimpleValidationFinding}.
     *
     * @param message finding message
     * @return builder
     */
    public static Builder withMessage(final String message) {
        return new Builder(message);
    }

    /**
     * Get the error message.
     *
     * @return error message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Get the function that fixes the error.
     *
     * @return function that fixes the error
     */
    public Fix getFix() {
        return Objects.requireNonNullElseGet(this.fix, () -> (final Logger log) -> {
        });
    }

    /**
     * Get if this class has an attached fix.
     *
     * @return {@code true} if this class has an attached fix
     */
    public boolean hasFix() {
        return this.fix != null;
    }

    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }

    /**
     * Functional interface for a method that fixes the finding.
     */
    @FunctionalInterface
    public interface Fix {
        /**
         * Fix the error described in {@link SimpleValidationFinding}.
         *
         * @param log logger
         */
        void fixError(Logger log);
    }

    /**
     * @return whether the current finding is optional, i.e. can be left unfixed
     */
    public boolean isOptional() {
        return this.isOptional;
    }

    /**
     * Builder for {@link SimpleValidationFinding}.
     */
    public static class Builder {
        private final String message;
        private boolean isOptional = false;
        private Fix fix;

        private Builder(final String message) {
            this.message = message;
        }

        /**
         * Add an optional fix to the finding.
         * <p>
         * You can only add one fix. If you call this method multiple times the fix will be overwritten.
         * </p>
         *
         * @param fix function that fixes the finding
         * @return self for fluent programming
         */
        public Builder andFix(final Fix fix) {
            this.fix = fix;
            return this;
        }

        /**
         * Mark the finding as optional or mandatory. By default each finding is mandatory.
         *
         * <p>
         * About the semantics when PK interprets wheter a validation was successful see
         * <ul>
         * <li>{@link ProjectKeeper#verify}</li>
         * <li>{@link ProjectKeeper#fix}</li>
         * <li>{@link SimpleValidationFinding#blockers}</li>
         * </ul>
         *
         * @param value whether this finding is optional or mandatory.
         * @return self for fluent programming
         */
        public Builder optional(final boolean value) {
            this.isOptional = value;
            return this;
        }

        /**
         * Build the {@link SimpleValidationFinding}.
         *
         * @return built {@link SimpleValidationFinding}
         */
        public SimpleValidationFinding build() {
            return new SimpleValidationFinding(this.message, this.isOptional, this.fix);
        }
    }
}
