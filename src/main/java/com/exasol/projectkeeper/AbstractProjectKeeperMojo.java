package com.exasol.projectkeeper;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.repository.RepositorySystem;

import com.exasol.projectkeeper.pom.*;
import com.exasol.projectkeeper.validators.DeletedFilesValidator;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileValidator;
import com.exasol.projectkeeper.validators.dependencies.DependenciesValidator;
import com.exasol.projectkeeper.validators.files.ProjectFilesValidator;
import com.exasol.projectkeeper.validators.pom.PomFileValidator;

/**
 * Abstract basis for Mojos in this project.
 */
public abstract class AbstractProjectKeeperMojo extends AbstractMojo {

    @Parameter(property = "modules")
    private List<String> modules;

    @Parameter(property = "excludeFiles")
    private List<String> excludedFiles;

    @Parameter(property = "linkReplacements")
    private List<String> linkReplacements;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Component
    private ProjectBuilder mavenProjectBuilder;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession session;

    @Component
    RepositorySystem repositorySystem;

    /**
     * Get a list of enabled modules.
     * 
     * <p>
     * Configure using {@code <modules></modules> } in the pom file.
     * </p>
     * 
     * @return list of enabled modules.
     */
    public Set<ProjectKeeperModule> getEnabledModules() {
        return Stream.concat(//
                Stream.of(ProjectKeeperModule.DEFAULT), //
                this.modules.stream().map(ProjectKeeperModule::getModuleByName)//
        ).collect(Collectors.toSet());
    }

    protected List<Validator> getValidators() {
        final BrokenLinkReplacer brokenLinkReplacer = new BrokenLinkReplacer(this.linkReplacements);
        final Set<ProjectKeeperModule> enabledModules = getEnabledModules();
        final ExcludedFilesMatcher excludedFilesMatcher = new ExcludedFilesMatcher(this.excludedFiles);
        final DefaultMavenFileModelReader mavenModelReader = new DefaultMavenFileModelReader(this.mavenProjectBuilder,
                this.session);
        final MavenArtifactModelReader artifactReader = new DefaultMavenArtifactModelReader(this.mavenProjectBuilder,
                this.session, this.repositorySystem);
        final File pomFile = this.project.getModel().getPomFile();
        return List.of(new ProjectFilesValidator(enabledModules, this.project.getBasedir(), excludedFilesMatcher),
                new PomFileValidator(enabledModules, pomFile),
                new ChangesFileValidator(this.project.getVersion(), this.project.getName(),
                        this.project.getBasedir().toPath(), mavenModelReader),
                new DependenciesValidator(mavenModelReader, artifactReader, pomFile, this.project.getBasedir().toPath(),
                        brokenLinkReplacer),
                new DeletedFilesValidator(this.project.getBasedir().toPath(), excludedFilesMatcher));
    }
}
