package com.exasol.projectkeeper;

import java.util.Objects;

import org.apache.maven.plugin.logging.Log;

/**
 * This class represents an error that was found during validation.
 */
public class ValidationFinding {
    private final String message;
    private final Fix fix;

    /**
     * Create a new instance of {@link ValidationFinding}.
     *
     * @param message error message
     * @param fix     function that fixes the error.
     */
    private ValidationFinding(final String message, final Fix fix) {
        this.message = message;
        this.fix = fix;
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
        return Objects.requireNonNullElseGet(this.fix, () -> log -> {
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

    @FunctionalInterface
    public interface Fix {
        /**
         * Fix the error described in {@link ValidationFinding}.
         * 
         * @param log Logger for log messages
         */
        void fixError(Log log);
    }

    /**
     * Get a {@link Builder} for {@link ValidationFinding}.
     * 
     * @param message finding message
     * @return builder
     */
    public static Builder withMessage(final String message) {
        return new Builder(message);
    }

    /**
     * Builder for {@link ValidationFinding}.
     */
    public static class Builder {
        private final String message;
        private Fix fix;

        public Builder(final String message) {
            this.message = message;
        }

        /**
         * Add a fix to the finding (optional).
         * <p>
         * You can only add one fix. IF you call this method multiple times the fix will be overwritten.
         * </p>
         * 
         * @param fix function that fixes the finding
         * @return self for fluent programming
         */
        public Builder andFix(final Fix fix) {
            this.fix = fix;
            return this;
        }

        public ValidationFinding build() {
            return new ValidationFinding(this.message, this.fix);
        }
    }
}
