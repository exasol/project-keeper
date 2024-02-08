package com.exasol.projectkeeper.validators.pom;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.exasol.errorreporting.ExaError;

/**
 * This class reads and writes POM files using the Maven POM {@link Model} class.
 */
public class PomFileIO {

    private static final MavenXpp3Writer WRITER = new MavenXpp3Writer();
    private static final MavenXpp3Reader READER = new MavenXpp3Reader();

    /**
     * Read the given file and parse it as a POM file.
     * 
     * @param path path to read
     * @return parsed POM model
     */
    public Model readPom(final Path path) {
        try {
            return READER.read(Files.newBufferedReader(path));
        } catch (IOException | XmlPullParserException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-172")
                    .message("Failed to read pom {{pom file path}}", path).toString(), exception);
        }
    }

    /**
     * Write a POM model to a file
     * 
     * @param model the POM model to write
     * @param path  the path to write
     */
    public void writePom(final Model model, final Path path) {
        try {
            WRITER.write(Files.newOutputStream(path), model);
        } catch (final IOException exception) {
            throw new UncheckedIOException(ExaError.messageBuilder("E-PK-CORE-173")
                    .message("Failed to write pom {{pom file path}}", path).toString(), exception);
        }
    }
}
