package com.exasol.projectkeeper.validators.finding;

import java.util.Objects;

import com.exasol.projectkeeper.Logger;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This class represents an error that was found during validation.
 */
@EqualsAndHashCode
@ToString
public class SimpleValidationFinding implements ValidationFinding {
    private final String message;
    private final Fix fix;

    /**
     * Create a new instance of {@link SimpleValidationFinding}.
     *
     * @param message error message
     * @param fix     function that fixes the error.
     */
    private SimpleValidationFinding(final String message, final Fix fix) {
        this.message = message;
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
        return Objects.requireNonNullElseGet(this.fix, () -> (Logger log) -> {
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
     * Builder for {@link SimpleValidationFinding}.
     */
    public static class Builder {
        private final String message;
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
         * Build the {@link SimpleValidationFinding}.
         *
         * @return built {@link SimpleValidationFinding}
         */
        public SimpleValidationFinding build() {
            return new SimpleValidationFinding(this.message, this.fix);
        }
    }
}
