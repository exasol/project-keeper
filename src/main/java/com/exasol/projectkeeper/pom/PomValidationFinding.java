package com.exasol.projectkeeper.pom;

/**
 * This class represents an error that was found during the POM file validation.
 */
public class PomValidationFinding {
    private final String message;
    private final Fix fix;

    /**
     * Create a new instance of {@link PomValidationFinding}.
     *
     * @param message error message
     * @param fix     function that fixes the error.
     */
    public PomValidationFinding(final String message, final Fix fix) {
        this.message = message;
        this.fix = fix;
    }

    /**
     * Get the error message.
     * 
     * @return @error message
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
        return this.fix;
    }

    @FunctionalInterface
    public interface Fix {
        /**
         * Fix the error described in {@link PomValidationFinding}.
         */
        void fixError();
    }
}
