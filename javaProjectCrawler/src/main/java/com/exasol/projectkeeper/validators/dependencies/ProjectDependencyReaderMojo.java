package com.exasol.projectkeeper.validators.dependencies;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.repository.RepositorySystem;

import com.exasol.projectkeeper.ResponseEncoder;
import com.exasol.projectkeeper.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.pom.MavenModelFromRepositoryReader;

/**
 * Maven mojo that prints a report {@link ProjectDependencies} to stdandard out.
 */
@Mojo(name = "getProjectDependencies")
public class ProjectDependencyReaderMojo extends AbstractMojo {

    @Component
    RepositorySystem repositorySystem;
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;
    @Component
    private ProjectBuilder mavenProjectBuilder;
    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession session;

    @Override
    public void execute() {
        final MavenModelFromRepositoryReader modelFromRepositoryReader = new MavenModelFromRepositoryReader(
                mavenProjectBuilder, session, repositorySystem);
        final ProjectDependencies dependencies = new ProjectDependencyReader(modelFromRepositoryReader)
                .readDependencies(project);
        final String response = dependencies.toJson();
        new ResponseEncoder().printResponse(response);
    }
}
