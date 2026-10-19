package com.exasol.projectkeeper.test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Installs Maven Wrapper files in integration-test projects.
 */
public final class MavenWrapperTestHelper {
    private MavenWrapperTestHelper() {
        // utility class
    }

    /**
     * Install an executable Maven Wrapper in a project directory.
     *
     * @param projectDirectory project directory
     */
    public static void install(final Path projectDirectory) {
        try {
            copyResource("templates/mvnw", projectDirectory.resolve("mvnw"));
            projectDirectory.resolve("mvnw").toFile().setExecutable(true);
            copyResource("templates/mvnw.cmd", projectDirectory.resolve("mvnw.cmd"));
            copyResource("templates/.mvn/wrapper/maven-wrapper.properties",
                    projectDirectory.resolve(".mvn/wrapper/maven-wrapper.properties"));
        } catch (final IOException exception) {
            throw new UncheckedIOException("Failed to install Maven Wrapper", exception);
        }
    }

    private static void copyResource(final String resource, final Path target) throws IOException {
        Files.createDirectories(target.getParent());
        try (InputStream content = ClassLoader.getSystemResourceAsStream(resource)) {
            Files.copy(content, target);
        }
    }
}
