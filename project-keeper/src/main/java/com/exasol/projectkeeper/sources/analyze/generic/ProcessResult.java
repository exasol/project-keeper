package com.exasol.projectkeeper.sources.analyze.generic;

/**
 * Result of executing a process.
 */
public class ProcessResult {
    private final String outputStreamContent;
    private final String errorStreamContent;

    /**
     * Create a new process result.
     * 
     * @param outputStreamContent output stream content
     * @param errorStreamContent  error stream content
     */
    public ProcessResult(final String outputStreamContent, final String errorStreamContent) {
        this.outputStreamContent = outputStreamContent;
        this.errorStreamContent = errorStreamContent;
    }

    /**
     * Content of the output stream.
     * 
     * @return output stream
     */
    public String getOutputStreamContent() {
        return outputStreamContent;
    }

    /**
     * Content of the error stream.
     * 
     * @return error stream
     */
    public String getErrorStreamContent() {
        return errorStreamContent;
    }
}
