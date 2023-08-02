package com.exasol.projectkeeper.shared.mavenprojectcrawler;

import java.util.Objects;

import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;

/**
 * Wrapper for the crawl result of maven-project-crawler for a single project.
 */
public final class CrawledMavenProject {
    private DependencyChangeReport dependencyChangeReport;
    private ProjectDependencies projectDependencies;
    private String projectVersion;

    /** Default constructor required for JSON serialization. */
    public CrawledMavenProject() {
        this(null, null, null);
    }

    /**
     * Create a new instance.
     * 
     * @param dependencyChangeReport dependency change report
     * @param projectDependencies    dependencies
     * @param projectVersion         version
     */
    public CrawledMavenProject(final DependencyChangeReport dependencyChangeReport,
            final ProjectDependencies projectDependencies, final String projectVersion) {
        this.dependencyChangeReport = dependencyChangeReport;
        this.projectDependencies = projectDependencies;
        this.projectVersion = projectVersion;
    }

    /** @return dependency change report */
    public DependencyChangeReport getDependencyChangeReport() {
        return dependencyChangeReport;
    }

    /** @param dependencyChangeReport dependency change report */
    public void setDependencyChangeReport(final DependencyChangeReport dependencyChangeReport) {
        this.dependencyChangeReport = dependencyChangeReport;
    }

    /** @return dependencies */
    public ProjectDependencies getProjectDependencies() {
        return projectDependencies;
    }

    /** @param projectDependencies dependencies */
    public void setProjectDependencies(final ProjectDependencies projectDependencies) {
        this.projectDependencies = projectDependencies;
    }

    /** @return version */
    public String getProjectVersion() {
        return projectVersion;
    }

    /** @param projectVersion version */
    public void setProjectVersion(final String projectVersion) {
        this.projectVersion = projectVersion;
    }

    @Override
    public String toString() {
        return "CrawledMavenProject [dependencyChangeReport=" + dependencyChangeReport + ", projectDependencies="
                + projectDependencies + ", projectVersion=" + projectVersion + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(dependencyChangeReport, projectDependencies, projectVersion);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CrawledMavenProject other = (CrawledMavenProject) obj;
        return Objects.equals(dependencyChangeReport, other.dependencyChangeReport)
                && Objects.equals(projectDependencies, other.projectDependencies)
                && Objects.equals(projectVersion, other.projectVersion);
    }
}
