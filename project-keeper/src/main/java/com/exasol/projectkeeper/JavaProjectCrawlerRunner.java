package com.exasol.projectkeeper;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.MavenProjectCrawlResult;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.ResponseCoder;
import com.exasol.projectkeeper.sources.analyze.generic.*;

/**
 * Runs the maven plugin goal on the current repository and returns the parsed result.
 * <p>
 * The crawler times out after {@link #DEFAULT_TIMEOUT}. Users can change this with system property
 * {@value #TIMEOUT_PROPERTY} (in seconds).
 */
public class JavaProjectCrawlerRunner {
    static final String TIMEOUT_PROPERTY = "project-keeper.crawlerTimeout";
    static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(120);
    private final Path projectDirectory;
    private final Path mvnRepositoryOverride;
    private final String ownVersion;

    /**
     * Create a new instance of {@link JavaProjectCrawlerRunner}.
     *
     * @param projectDirectory      the project directory
     * @param mvnRepositoryOverride Maven repository override. This is useful for running integration tests. Use
     *                              {@code null} for default.
     * @param ownVersion            project-keeper version
     */
    public JavaProjectCrawlerRunner(final Path projectDirectory, final Path mvnRepositoryOverride, final String ownVersion) {
        this.projectDirectory = projectDirectory;
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
        final ProcessResult result = new CommandExecutor().execute(builder.buildCommand());
        return new ResponseCoder().decodeResponse(result.getOutputStreamContent());
    }

    private MavenProcessBuilder buildMavenCommand(final Path... pomFiles) {
        final MavenProcessBuilder builder = MavenProcessBuilder.create(this.projectDirectory)
                .addArguments(
                        "--no-transfer-progress",
                        "com.exasol:project-keeper-java-project-crawler:" + this.ownVersion + ":" + "crawl",
                        "-DprojectsToCrawl=" + getProjectList(pomFiles),
                        /*
                         * We need to disable the model cache here since it caches the parent poms with {revision} as
                         * version and then runs into trouble since the cache is different when reading the old pom (for
                         * comparing dependencies).
                         */
                        "-Dmaven.defaultProjectBuilder.disableGlobalModelCache=true")
                .workingDir(this.projectDirectory)
                .timeout(readTimeout());

        if (this.mvnRepositoryOverride != null) {
            builder.addArgument("-Dmaven.repo.local=" + this.mvnRepositoryOverride);
        }
        return builder;
    }

    static Duration readTimeout() {
        final String value = System.getProperty(TIMEOUT_PROPERTY);
        if ((value == null) || value.isBlank()) {
            return DEFAULT_TIMEOUT;
        }
        if (!value.trim().matches("[1-9]\\d{0,8}")) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-216")
                    .message("Invalid value {{value}} for system property {{property}}.", value, TIMEOUT_PROPERTY)
                    .mitigation("Specify the crawler timeout as a positive number of seconds, e.g. {{example}}.",
                            "-D" + TIMEOUT_PROPERTY + "=180")
                    .toString());
        }
        return Duration.ofSeconds(Long.parseLong(value.trim()));
    }

    private static String getProjectList(final Path... pomFiles) {
        return Arrays.stream(pomFiles)
                .map(Path::toAbsolutePath)
                .map(Path::toString)
                .collect(Collectors.joining(";"));
    }
}
