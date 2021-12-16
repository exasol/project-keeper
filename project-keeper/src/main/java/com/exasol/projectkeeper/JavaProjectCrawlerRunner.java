package com.exasol.projectkeeper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.validators.changesfile.dependencies.model.DependencyChangeReport;

public class JavaProjectCrawlerRunner {
    private static final String RESPONSE_START_TOKEN = "###SerializedResponseStart###";
    private static final String RESPONSE_END_TOKEN = "###SerializedResponseEnd###";
    private static final Logger LOGGER = Logger.getLogger(JavaProjectCrawlerRunner.class.getName());
    private final Path pomFile;

    public JavaProjectCrawlerRunner(Path pomFile) {
        this.pomFile = pomFile;
    }

    public DependencyChangeReport getDependencyChanges() {
        final String json = runCrawlerPlugin("getDependencyUpdates");
        return DependencyChangeReport.fromJson(json);
    }

    public ProjectDependencies getDependencies() {
        final String json = runCrawlerPlugin("getProjectDependencies");
        return ProjectDependencies.fromJson(json);
    }

    private String runCrawlerPlugin(String goal) {
        try {
            Runtime rt = Runtime.getRuntime();
            String[] commands = { "mvn", "project-keeper-java-project-crawler:" + goal, "--file", pomFile.toString() };
            Process proc = rt.exec(commands);
            final int exitCode = proc.waitFor();
            if (exitCode != 0) {
                try (InputStream inputStream = proc.getErrorStream()) {
                    final String errorMessage = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    LOGGER.log(Level.SEVERE, errorMessage);
                    throw new IllegalStateException(ExaError.messageBuilder("E-PK-78").message(
                            "Failed to run project-keeper-java-project-crawler maven plugin Exit code was {{exit code}}.",
                            exitCode).toString());
                }
            }
            try (InputStream inputStream = proc.getInputStream()) {
                final String output = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                final int startIndex = output.indexOf(RESPONSE_START_TOKEN);
                final int responseStartIndex = startIndex + RESPONSE_START_TOKEN.length() + 1;
                final int endIndex = output.indexOf(RESPONSE_END_TOKEN);
                if (startIndex == -1 || endIndex == -1 || responseStartIndex > endIndex) {
                    throw new IllegalStateException(ExaError.messageBuilder("F-PK-79")
                            .message("Invalid response from crawler plugin.").ticketMitigation().toString());
                }
                return output.substring(responseStartIndex, endIndex);
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("", exception);// todo
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("", exception);// todo
        }
    }
}
