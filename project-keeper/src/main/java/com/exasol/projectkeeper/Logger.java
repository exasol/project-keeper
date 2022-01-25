package com.exasol.projectkeeper;

/**
 * Interface for loggers that consume project-keeper logs.
 */
public interface Logger {

    /**
     * Log an info message.
     * 
     * @param message message to log
     */
    public void info(String message);

    /**
     * Log a warning.
     * 
     * @param message message to log
     */
    public void warn(String message);

    /**
     * Log an error.
     * 
     * @param message message to log
     */
    public void error(String message);
}
