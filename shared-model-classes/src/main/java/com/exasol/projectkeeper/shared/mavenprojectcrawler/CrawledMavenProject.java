package com.exasol.projectkeeper.shared.mavenprojectcrawler;

import java.util.Objects;

import javax.annotation.processing.Generated;

import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;

/**
 * Wrapper for the crawl result of maven-project-crawler for a single project.
 */
public final class CrawledMavenProject {
    private DependencyChangeReport dependencyChangeReport;
    private ProjectDependencies projectDependencies;
    private String projectVersion;
    private String javaVersion;

    /** Default constructor required for JSON serialization. */
    public CrawledMavenProject() {
        this(null, null, null, null);
    }

    /**
     * Create a new instance.
     * 
     * @param dependencyChangeReport dependency change report
     * @param projectDependencies    dependencies
     * @param projectVersion         project version
     * @param javaVersion            Java version from the {@code java.version} property or {@code null} if property is
     *                               not defined
     */
    public CrawledMavenProject(final DependencyChangeReport dependencyChangeReport,
            final ProjectDependencies projectDependencies, final String projectVersion, final String javaVersion) {
        this.dependencyChangeReport = dependencyChangeReport;
        this.projectDependencies = projectDependencies;
        this.projectVersion = projectVersion;
        this.javaVersion = javaVersion;
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

    /** @return project version */
    public String getProjectVersion() {
        return projectVersion;
    }

    /**
     * @return Java version from the {@code java.version} property or {@code null} if property is not defined
     */
    public String getJavaVersion() {
        return javaVersion;
    }

    /** @param projectVersion version */
    public void setProjectVersion(final String projectVersion) {
        this.projectVersion = projectVersion;
    }

    /**
     * @param javaVersion Java version from the {@code java.version} property or {@code null} if property is not defined
     */
    public void setJavaVersion(final String javaVersion) {
        this.javaVersion = javaVersion;
    }

    @Override
    public String toString() {
        return "CrawledMavenProject [dependencyChangeReport=" + dependencyChangeReport + ", projectDependencies="
                + projectDependencies + ", projectVersion=" + projectVersion + ", javaVersion=" + javaVersion + "]";
    }

    @Override
    @Generated("vscode")
    public int hashCode() {
        return Objects.hash(dependencyChangeReport, projectDependencies, projectVersion, javaVersion);
    }

    @Override
    @Generated("vscode")
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
                && Objects.equals(projectVersion, other.projectVersion)
                && Objects.equals(javaVersion, other.javaVersion);
    }
}
