package com.exasol.projectkeeper;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.shared.mavenprojectcrawler.MavenProjectCrawlResult;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.ResponseCoder;
import com.exasol.projectkeeper.sources.analyze.generic.MavenProcessBuilder;
import com.exasol.projectkeeper.sources.analyze.generic.SimpleProcess;

/**
 * Runs the maven plugin goal on the current repository and returns the parsed result.
 */
public class JavaProjectCrawlerRunner {
    private final Path mvnRepositoryOverride;
    private final String ownVersion;

    /**
     * Create a new instance of {@link JavaProjectCrawlerRunner}.
     *
     * @param mvnRepositoryOverride Maven repository override. This is useful for running integration tests. Use
     *                              {@code null} for default.
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
        final MavenProcessBuilder builder = buildMavenCommand(pomFiles);
        final SimpleProcess process = builder.startSimpleProcess();
        process.waitUntilFinished(Duration.ofSeconds(90));
        return new ResponseCoder().decodeResponse(process.getOutputStreamContent());
    }

    private MavenProcessBuilder buildMavenCommand(final Path... pomFiles) {
        final MavenProcessBuilder builder = MavenProcessBuilder.create()
                .addArguments(
                        "-Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn",
                        "com.exasol:project-keeper-java-project-crawler:" + this.ownVersion + ":" + "crawl",
                        "-DprojectsToCrawl=" + getProjectList(pomFiles),
                        /*
                         * We need to disable the model cache here since it caches the parent poms with {revision} as
                         * version and then runs into trouble since the cache is different when reading the old pom (for
                         * comparing dependencies).
                         */
                        "-Dmaven.defaultProjectBuilder.disableGlobalModelCache=true")
                .workingDir(null);

        if (this.mvnRepositoryOverride != null) {
            builder.addArgument("-Dmaven.repo.local=" + this.mvnRepositoryOverride);
        }
        return builder;
    }

    private String getProjectList(final Path... pomFiles) {
        return Arrays.stream(pomFiles).map(this::formatPath).collect(Collectors.joining(";"));
    }

    private String formatPath(final Path pomFile) {
        return pomFile.toAbsolutePath().toString()
                // we use / instead of \ here as a fix for https://github.com/eclipse-ee4j/yasson/issues/540
                .replace(FileSystems.getDefault().getSeparator(), "/");
    }
}
