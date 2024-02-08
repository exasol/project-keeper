package com.exasol.projectkeeper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.repository.RepositorySystem;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.pom.*;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.*;
import com.exasol.projectkeeper.validators.changesfile.DependencyUpdateReader;
import com.exasol.projectkeeper.validators.dependencies.ProjectDependencyReader;

/**
 * Maven mojo that analyzes a maven project and prints the report to stdout.
 */
@Mojo(name = "crawl", requiresProject = false)
public class MavenProjectCrawlerMojo extends AbstractMojo {
    private static final String PROPERTY_PROJECTS_TO_CRAWL = "projectsToCrawl";
    @Component
    RepositorySystem repositorySystem;

    @Parameter(property = PROPERTY_PROJECTS_TO_CRAWL, required = true)
    private String projectsToCrawl;

    @Component
    private ProjectBuilder mavenProjectBuilder;
    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession session;

    // [impl -> dsn~eclipse-prefs-java-version~1]
    @Override
    public void execute() {
        if (this.projectsToCrawl == null || this.projectsToCrawl.isBlank()) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-MPC-64")
                    .message("Property {{property name}} is not defined or empty.", PROPERTY_PROJECTS_TO_CRAWL)
                    .mitigation("Specify property with least one pom file.").toString());
        }
        final MavenProjectFromFileReader mavenProjectReader = new DefaultMavenProjectFromFileReader(
                this.mavenProjectBuilder, this.session);
        final MavenModelFromRepositoryReader modelFromRepositoryReader = new MavenModelFromRepositoryReader(
                this.mavenProjectBuilder, this.session, this.repositorySystem);
        final Map<String, CrawledMavenProject> crawledProjects = new HashMap<>();
        final String[] paths = this.projectsToCrawl.split(";");
        for (final String path : paths) {
            final MavenProject project = readProject(mavenProjectReader, path);
            final DependencyChangeReport dependencyChangeReport = new DependencyUpdateReader(mavenProjectReader,
                    project.getBasedir().toPath(), project.getModel()).readDependencyChanges();
            final ProjectDependencies dependencies = new ProjectDependencyReader(modelFromRepositoryReader, project)
                    .readDependencies();
            final String javaVersion = project.getProperties().getProperty("java.version", null);
            final CrawledMavenProject crawledMavenProject = new CrawledMavenProject(dependencyChangeReport,
                    dependencies, project.getVersion(), javaVersion);
            crawledProjects.put(path, crawledMavenProject);
        }
        final MavenProjectCrawlResult report = new MavenProjectCrawlResult(crawledProjects);
        final String response = report.toJson();
        new ResponseCoder().printResponse(response);
    }

    private MavenProject readProject(final MavenProjectFromFileReader mavenProjectReader, final String pomPath) {
        try {
            return mavenProjectReader.readProject(new File(pomPath));
        } catch (final MavenProjectFromFileReader.ReadFailedException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-MPC-1")
                    .message("Failed to crawl project {{path}}.", pomPath).toString(), exception);
        }
    }
}
