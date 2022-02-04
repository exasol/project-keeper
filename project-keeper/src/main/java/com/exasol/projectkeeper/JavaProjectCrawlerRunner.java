package com.exasol.projectkeeper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.OsCheck.OSType;
import com.exasol.projectkeeper.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.validators.changesfile.dependencies.model.DependencyChangeReport;

/**
 * Runs the maven plugin goal on the current repository and returns the parsed result.
 */
public class JavaProjectCrawlerRunner {
    private static final String RESPONSE_START_TOKEN = "###SerializedResponseStart###";
    private static final String RESPONSE_END_TOKEN = "###SerializedResponseEnd###";
    private static final Logger LOGGER = Logger.getLogger(JavaProjectCrawlerRunner.class.getName());
    private final Path mvnRepositoryOverride;
    private final String ownVersion;

    /**
     * Create a new instance of {@link JavaProjectCrawlerRunner}.
     *
     * @param mvnRepositoryOverride maven repository override. USe {@code null} for default
     * @param ownVersion            project-keeper version
     */
    public JavaProjectCrawlerRunner(final Path mvnRepositoryOverride, final String ownVersion) {
        this.mvnRepositoryOverride = mvnRepositoryOverride;
        this.ownVersion = ownVersion;
    }

    /**
     * Get a {@link DependencyChangeReport} for the project.
     * 
     * @param pomFile path to the pom file to analyze.
     * @return the {@link DependencyChangeReport}
     */
    public DependencyChangeReport getDependencyChanges(final Path pomFile) {
        final String json = runCrawlerPlugin(pomFile, "getDependencyUpdates");
        return DependencyChangeReport.fromJson(json);
    }

    /**
     * Get the {@link ProjectDependencies} for the project.
     * 
     * @param pomFile path to the pom file to analyze.
     * @return the {@link ProjectDependencies}
     */
    public ProjectDependencies getDependencies(final Path pomFile) {
        final String json = runCrawlerPlugin(pomFile, "getProjectDependencies");
        return ProjectDependencies.fromJson(json);
    }

    private String runCrawlerPlugin(final Path pomFile, final String goal) {
        try {
            final Runtime rt = Runtime.getRuntime();
            final List<String> commandParts = new ArrayList<>(List.of(getMavenExecutable(), "--batch-mode",
                    "-Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn",
                    "com.exasol:project-keeper-java-project-crawler:" + this.ownVersion + ":" + goal, "--file",
                    pomFile.toString()));
            if (this.mvnRepositoryOverride != null) {
                commandParts.add("-Dmaven.repo.local=" + this.mvnRepositoryOverride);
            }

            LOGGER.fine(() -> "Executing command " + commandParts);
            final Process proc = rt.exec(commandParts.toArray(String[]::new));
            if (!proc.waitFor(90, TimeUnit.SECONDS)) {
                final String output = readFromStream(proc.getInputStream());
                final String errors = readFromStream(proc.getErrorStream());
                throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-81").message(
                        "Timeout while executing command {{executed command|uq}}.\nStandard output: {{standard output|uq}}\nStandard error: {{error output|uq}}",
                        commandParts, output, errors).toString());
            }
            final int exitCode = proc.exitValue();
            final String output = readFromStream(proc.getInputStream());
            final String errors = readFromStream(proc.getErrorStream());
            if (exitCode != 0) {
                LOGGER.log(Level.SEVERE, output);
                throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-78").message(
                        "Failed to run command {{executed command|uq}}, exit code was {{exit code}}.\nStandard output: {{standard output|uq}}\nStandard error: {{error output|uq}}",
                        commandParts, exitCode, output, errors).toString());
            }
            final int startIndex = output.indexOf(RESPONSE_START_TOKEN);
            final int responseStartIndex = startIndex + RESPONSE_START_TOKEN.length() + 1;
            final int endIndex = output.indexOf(RESPONSE_END_TOKEN);
            if (startIndex == -1 || endIndex == -1 || responseStartIndex > endIndex) {
                throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-79")
                        .message("Invalid response from crawler plugin: {{output}}", output).ticketMitigation()
                        .toString());
            }
            return output.substring(responseStartIndex, endIndex);
        } catch (final IOException exception) {
            throw new UncheckedIOException(getRunFailedMessage(), exception);
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(getRunFailedMessage(), exception);
        }
    }

    private String getMavenExecutable() {
        OSType osType = new OsCheck().getOperatingSystemType();
        if (osType == OSType.WINDOWS) {
            return "mvn.cmd";
        } else {
            return "mvn";
        }
    }

    private String getRunFailedMessage() {
        return ExaError.messageBuilder("E-PK-CORE-80").message("Failed to run project-keeper-java-project-crawler.")
                .toString();
    }

    private String readFromStream(final InputStream stream) throws IOException {
        try (final InputStream inputStream = stream) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
