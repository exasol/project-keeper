package com.exasol.projectkeeper.sources.analyze;

import static com.exasol.projectkeeper.config.ProjectKeeperConfig.SourceType.MAVEN;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.JavaProjectCrawlerRunner;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.CrawledMavenProject;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MavenSourceAnalyzer implements LanguageSpecificSourceAnalyzer {

    private final Path mvnRepositoryOverride;
    private final String ownVersion;

    @Override
    public List<AnalyzedSource> analyze(final Path projectDir, final List<ProjectKeeperConfig.Source> sources) {
        final Map<String, CrawledMavenProject> crawledMvnSources = runCrawlerForMvnSources(sources);
        return sources.stream()
                .map((final ProjectKeeperConfig.Source source) -> analyzeSource(projectDir, source, crawledMvnSources))
                .collect(Collectors.toList());
    }

    /**
     * Run the maven-project-crawler for all maven sources.
     * <p>
     * We run the crawler only once since it has a high start up time (due to mvn). Starting it for every source would
     * multiply this start up time.
     * </p>
     *
     * @param sources list of sources
     * @return crawled maven sources
     */
    private Map<String, CrawledMavenProject> runCrawlerForMvnSources(final List<ProjectKeeperConfig.Source> sources) {
        final List<Path> mvnSourcePaths = sources.stream().filter(source -> MAVEN.equals(source.getType()))
                .map(ProjectKeeperConfig.Source::getPath).collect(Collectors.toList());
        return new JavaProjectCrawlerRunner(this.mvnRepositoryOverride, this.ownVersion)
                .crawlProject(mvnSourcePaths.toArray(Path[]::new)).getCrawledProjects();
    }

    private AnalyzedSource analyzeSource(final Path projectDir, final ProjectKeeperConfig.Source source,
            final Map<String, CrawledMavenProject> crawledMvnSources) {
        if (MAVEN.equals(source.getType())) {
            final Model model = readMavenModel(source);
            final String artifactId = model.getArtifactId();
            final String rawProjectName = model.getName();
            final String projectName = ((rawProjectName == null) || rawProjectName.isBlank()) ? artifactId
                    : rawProjectName;
            final CrawledMavenProject crawledMavenProject = getCrawlResultForProject(source, crawledMvnSources);
            final boolean isRoot = projectDir.relativize(source.getPath()).equals(Path.of("pom.xml"));
            return AnalyzedMavenSource.builder().path(source.getPath()).modules(source.getModules())
                    .advertise(source.isAdvertise()).artifactId(artifactId).projectName(projectName)
                    .dependencies(crawledMavenProject.getProjectDependencies())
                    .dependencyChanges(crawledMavenProject.getDependencyChangeReport())
                    .version(crawledMavenProject.getProjectVersion()).isRootProject(isRoot).build();
        } else {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-93")
                    .message("Analyzing of {{type}} is not supported by MavenSourceAnalyzer",
                            source.getType())
                    .ticketMitigation().toString());
        }
    }

    private CrawledMavenProject getCrawlResultForProject(final ProjectKeeperConfig.Source source,
            final Map<String, CrawledMavenProject> crawlResult) {
        final String key = source.getPath().toString()
                // we use / instead of \ here as a fix for https://github.com/eclipse-ee4j/yasson/issues/540
                .replace(FileSystems.getDefault().getSeparator(), "/");
        if (!crawlResult.containsKey(key)) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-117")
                    .message("The crawl result did not contain the project {{project}}.", source.getPath())
                    .ticketMitigation().toString());
        }
        return crawlResult.get(key);
    }

    private Model readMavenModel(final ProjectKeeperConfig.Source source) {
        try (final FileInputStream fileInputStream = new FileInputStream(source.getPath().toFile())) {
            return new MavenXpp3Reader().read(fileInputStream);
        } catch (final XmlPullParserException | IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-94").message("Failed to analyze maven source.").toString(),
                    exception);
        }
    }
}
