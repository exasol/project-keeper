package com.exasol.projectkeeper.shared.mavenprojectcrawler;

import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;

import lombok.*;

/**
 * Wrapper for the crawl result of maven-project-crawler for a single project.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrawledMavenProject {
    private DependencyChangeReport dependencyChangeReport;
    private ProjectDependencies projectDependencies;
    private String projectVersion;
}
