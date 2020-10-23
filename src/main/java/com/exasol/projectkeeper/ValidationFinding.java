package com.exasol.projectkeeper;

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
    public ValidationFinding(final String message, final Fix fix) {
        this.message = message;
        this.fix = fix;
    }

    /**
     * Create a new instance of {@link ValidationFinding} without a fix.
     *
     * @param message error message
     */
    public ValidationFinding(final String message) {
        this.message = message;
        this.fix = null;
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
        if (this.fix == null) {
            return (log) -> {
            };
        } else {
            return this.fix;
        }
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
         * @param log
         */
        void fixError(Log log);
    }
}
