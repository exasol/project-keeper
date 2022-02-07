package com.exasol.projectkeeper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.OsCheck.OSType;
import com.exasol.projectkeeper.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.stream.AsyncStreamReader;
import com.exasol.projectkeeper.stream.CollectingConsumer;
import com.exasol.projectkeeper.validators.changesfile.dependencies.model.DependencyChangeReport;

/**
 * Runs the maven plugin goal on the current repository and returns the parsed result.
 */
public class JavaProjectCrawlerRunner {
    private static final String RESPONSE_START_TOKEN = "###SerializedResponseStart###";
    private static final String RESPONSE_END_TOKEN = "###SerializedResponseEnd###";
    private static final Logger LOGGER = Logger.getLogger(JavaProjectCrawlerRunner.class.getName());
    private static final Duration STREAM_READING_TIMEOUT = Duration.ofSeconds(1);
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
            final List<String> commandParts = new ArrayList<>(List.of(getMavenExecutable(), "--batch-mode",
                    "-Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn",
                    "com.exasol:project-keeper-java-project-crawler:" + this.ownVersion + ":" + goal, "--file",
                    pomFile.toString()));
            if (this.mvnRepositoryOverride != null) {
                commandParts.add("-Dmaven.repo.local=" + this.mvnRepositoryOverride);
            }

            LOGGER.fine(() -> "Executing command " + commandParts);
            final Process proc = new ProcessBuilder(commandParts).redirectErrorStream(true).start();

            CollectingConsumer streamConsumer = new AsyncStreamReader().startCollectingConsumer(proc.getInputStream());

            if (!proc.waitFor(90, TimeUnit.SECONDS)) {
                String output = streamConsumer.getContent(STREAM_READING_TIMEOUT);
                throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-81")
                        .message("Timeout while executing command {{executed command|uq}}. Output was {{output}}",
                                commandParts, output)
                        .toString());
            }
            final int exitCode = proc.exitValue();
            final String output = streamConsumer.getContent(STREAM_READING_TIMEOUT);
            if (exitCode != 0) {
                throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-78")
                        .message("Failed to run command {{executed command|uq}}, exit code was {{exit code}}.",
                                commandParts, exitCode)
                        .toString());
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
}
