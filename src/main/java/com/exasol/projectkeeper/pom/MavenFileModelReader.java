package com.exasol.projectkeeper.pom;

import java.io.File;

import org.apache.maven.model.Model;

/**
 * Interfaces for classes that read a maven {@link Model} from a pom.xml file.
 */
public interface MavenFileModelReader {
    /**
     * Read a maven model.
     *
     * @param pomFile pom.xml file to read
     * @return read model
     * @throws ReadFailedException if reading failed
     */
    Model readModel(File pomFile) throws ReadFailedException;

    /**
     * Exception that is thrown if the reading failed.
     */
    class ReadFailedException extends Exception {
        /**
         * Create a new instance of {@link ReadFailedException}.
         * 
         * @param message message
         * @param cause   cause
         */
        public ReadFailedException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
