package com.exasol.projectkeeper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.MavenProjectCrawlResult;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.ResponseCoder;
import com.exasol.projectkeeper.sources.analyze.generic.MavenProcessBuilder;
import com.exasol.projectkeeper.stream.AsyncStreamReader;
import com.exasol.projectkeeper.stream.CollectingConsumer;

/**
 * Runs the maven plugin goal on the current repository and returns the parsed result.
 */
public class JavaProjectCrawlerRunner {
    private static final Logger LOGGER = Logger.getLogger(JavaProjectCrawlerRunner.class.getName());
    private static final Duration STREAM_READING_TIMEOUT = Duration.ofSeconds(1);
    private final Path mvnRepositoryOverride;
    private final String ownVersion;

    /**
     * Create a new instance of {@link JavaProjectCrawlerRunner}.
     *
     * @param mvnRepositoryOverride maven repository override. Use {@code null} for default
     * @param ownVersion            project-keeper version
     */
    public JavaProjectCrawlerRunner(final Path mvnRepositoryOverride, final String ownVersion) {
        this.mvnRepositoryOverride = mvnRepositoryOverride;
        this.ownVersion = ownVersion;
    }

    /**
     * Crawl a maven project.
     *
     * @param pomFiles paths to the pom files to analyze.
     * @return the {@link MavenProjectCrawlResult}
     */
    public MavenProjectCrawlResult crawlProject(final Path... pomFiles) {
        final String json = runCrawlerPlugin(pomFiles);
        return MavenProjectCrawlResult.fromJson(json);
    }

    private String runCrawlerPlugin(final Path... pomFiles) {
        final String projectList = Arrays.stream(pomFiles).map(pomFile -> pomFile.toAbsolutePath().toString()
                // we use / instead of \ here as a fix for https://github.com/eclipse-ee4j/yasson/issues/540
                .replace(FileSystems.getDefault().getSeparator(), "/")).collect(Collectors.joining(";"));
        final List<String> commandParts = buildMavenCommand(projectList);
        LOGGER.fine(() -> "Executing command " + commandParts);
        try {
            final Process proc = new ProcessBuilder(commandParts).redirectErrorStream(true).start();

            final CollectingConsumer outputStreamConsumer = new AsyncStreamReader()
                    .startCollectingConsumer(proc.getInputStream());
            final CollectingConsumer errorStreamConsumer = new AsyncStreamReader()
                    .startCollectingConsumer(proc.getErrorStream());

            if (!proc.waitFor(90, TimeUnit.SECONDS)) {
                final String stdOutput = outputStreamConsumer.getCurrentContent();
                final String stdError = errorStreamConsumer.getCurrentContent();
                throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-81").message(
                        "Timeout while executing command {{executed command|u}}. Output: {{std output}}, error: {{std error}}",
                        commandParts, stdOutput, stdError).toString());
            }
            final int exitCode = proc.exitValue();
            final String output = outputStreamConsumer.getContent(STREAM_READING_TIMEOUT);
            if (exitCode != 0) {
                LOGGER.log(Level.SEVERE, output);
                throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-78").message(
                        "Failed to run command {{executed command|u}}, exit code was {{exit code}}. Output:\n{{output}}",
                        commandParts, exitCode, output).toString());
            }
            return new ResponseCoder().decodeResponse(output);
        } catch (final IOException exception) {
            throw new UncheckedIOException(getRunFailedMessage(), exception);
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(getRunFailedMessage(), exception);
        }
    }

    private List<String> buildMavenCommand(final String projectList) {
        final MavenProcessBuilder command = MavenProcessBuilder.create().addArguments("--batch-mode",
                "-Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn",
                "com.exasol:project-keeper-java-project-crawler:" + this.ownVersion + ":" + "crawl",
                "-DprojectsToCrawl=" + projectList,
                /*
                 * We need to disable the model cache here since it caches the parent poms with {revision} as version
                 * and then runs into trouble since the cache is different when reading the old pom (for comparing
                 * dependencies).
                 */
                "-Dmaven.defaultProjectBuilder.disableGlobalModelCache=true");

        if (this.mvnRepositoryOverride != null) {
            command.addArgument("-Dmaven.repo.local=" + this.mvnRepositoryOverride);
        }
        return command.build();
    }

    private String getRunFailedMessage() {
        return ExaError.messageBuilder("E-PK-CORE-80").message("Failed to run project-keeper-java-project-crawler.")
                .toString();
    }
}
