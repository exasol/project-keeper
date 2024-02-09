package com.exasol.projectkeeper.sources.analyze.generic;

import java.util.Objects;

/**
 * Result of executing a process.
 */
public final class ProcessResult {
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

    @Override
    public int hashCode() {
        return Objects.hash(outputStreamContent, errorStreamContent);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProcessResult other = (ProcessResult) obj;
        return Objects.equals(outputStreamContent, other.outputStreamContent)
                && Objects.equals(errorStreamContent, other.errorStreamContent);
    }

    @Override
    public String toString() {
        return "ProcessResult [outputStreamContent=" + outputStreamContent + ", errorStreamContent="
                + errorStreamContent + "]";
    }
}
