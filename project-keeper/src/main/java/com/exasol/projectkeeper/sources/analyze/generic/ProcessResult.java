package com.exasol.projectkeeper.sources.analyze.generic;

/**
 * Result of executing a process.
 * 
 * @param outputStreamContent output stream content
 * @param errorStreamContent  error stream content
 */
public record ProcessResult(String outputStreamContent, String errorStreamContent) {

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
