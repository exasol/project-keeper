package com.exasol.projectkeeper.shared.mavenprojectcrawler;

import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrawledMavenProject {
    private DependencyChangeReport dependencyChangeReport;
    private ProjectDependencies projectDependencies;
    private String projectVersion;
}
