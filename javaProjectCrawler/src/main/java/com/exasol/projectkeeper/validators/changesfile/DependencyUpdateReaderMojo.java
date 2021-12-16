package com.exasol.projectkeeper.validators.changesfile;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.repository.RepositorySystem;

import com.exasol.projectkeeper.ResponseEncoder;
import com.exasol.projectkeeper.pom.DefaultMavenProjectFromFileReader;
import com.exasol.projectkeeper.pom.MavenProjectFromFileReader;
import com.exasol.projectkeeper.validators.changesfile.dependencies.model.DependencyChangeReport;

@Mojo(name = "getDependencyUpdates")
public class DependencyUpdateReaderMojo extends AbstractMojo {

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
        final MavenProjectFromFileReader mavenProjectReader = new DefaultMavenProjectFromFileReader(
                this.mavenProjectBuilder, session);
        final DependencyChangeReport report = new DependencyUpdateReader(mavenProjectReader,
                project.getBasedir().toPath(), project.getModel()).getDependencyUpdates();
        final String response = report.toJson();
        new ResponseEncoder().printResponse(response);
    }
}
