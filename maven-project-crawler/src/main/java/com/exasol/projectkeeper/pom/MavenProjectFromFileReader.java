package com.exasol.projectkeeper.pom;

import java.io.File;

import org.apache.maven.project.MavenProject;

/**
 * Interfaces for classes that read a {@link MavenProject} from a pom.xml file.
 */
public interface MavenProjectFromFileReader {
    /**
     * Read a {@link MavenProject} from pom.xml file.
     *
     * @param pomFile pom.xml file to read
     * @return read model
     * @throws ReadFailedException if reading failed
     */
    MavenProject readProject(File pomFile) throws ReadFailedException;

    /**
     * Exception that is thrown if the reading failed.
     */
    class ReadFailedException extends Exception {
        private static final long serialVersionUID = 394058627645835163L;

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
