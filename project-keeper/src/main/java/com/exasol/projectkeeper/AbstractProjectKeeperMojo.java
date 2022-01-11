package com.exasol.projectkeeper;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.repository.GitRepository;
import com.exasol.projectkeeper.validators.*;
import com.exasol.projectkeeper.validators.changelog.ChangelogFileValidator;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileValidator;
import com.exasol.projectkeeper.validators.dependencies.DependenciesValidator;
import com.exasol.projectkeeper.validators.files.ProjectFilesValidator;
import com.exasol.projectkeeper.validators.pom.PomFileValidator;

/**
 * Abstract basis for Mojos in this project.
 */
public abstract class AbstractProjectKeeperMojo extends AbstractMojo {

    @Parameter(property = "modules")
    // [impl->dsn~modules~1]
    private List<String> modules;

    @Parameter(property = "project-keeper.skip", defaultValue = "false")
    private String skip;

    // [impl->dsn~excluding-files~1]
    @Parameter(property = "excludedFiles")
    private List<String> excludedFiles;

    // [impl->dsn~exclduding-mvn-plugins~1]
    @Parameter(property = "excludedPlugins")
    private Set<String> excludedPlugins;

    @Parameter(property = "linkReplacements")
    private List<String> linkReplacements;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession session;

    /**
     * Get a list of enabled modules.
     * 
     * <p>
     * Configure using {@code <modules></modules> } in the pom file.
     * </p>
     * 
     * @return list of enabled modules.
     */
    // [impl->dsn~modules~1]
    public Set<ProjectKeeperModule> getEnabledModules() {
        return Stream.concat(//
                Stream.of(ProjectKeeperModule.DEFAULT), //
                this.modules.stream().map(ProjectKeeperModule::getModuleByName)//
        ).collect(Collectors.toSet());
    }

    /**
     * Check if the project-keeper is enabled.
     * 
     * @return {@code true} if the plugin is enabled, else {@code false}
     */
    protected boolean isEnabled() {
        if ("true".equals(this.skip)) {
            getLog().info("Skipping project-keeper.");
            return false;
        } else if ("false".equals(this.skip)) {
            return true;
        } else {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-75")
                    .message("Invalid value {{value}} for property 'project-keeper.skip'.", this.skip)
                    .mitigation("Please set the property to 'true' or 'false'.").toString());
        }
    }

    /**
     * Get a list of {@link Validator}s.
     * 
     * @return list of {@link Validator}s
     */
    protected List<Validator> getValidators() {
        final Path projectDir = this.project.getBasedir().toPath();
        final Path mvnRepo = Path.of(this.session.getLocalRepository().getBasedir());
        final String ownVersion = getClass().getPackage().getImplementationVersion();
        final GitRepository gitRepository = new GitRepository(projectDir);
        final var brokenLinkReplacer = new BrokenLinkReplacer(this.linkReplacements);
        final Set<ProjectKeeperModule> enabledModules = getEnabledModules();
        final var excludedFilesMatcher = new ExcludedFilesMatcher(this.excludedFiles);
        final var pomFile = this.project.getModel().getPomFile();
        return List.of(new ProjectFilesValidator(enabledModules, projectDir.toFile(), excludedFilesMatcher),
                new ReadmeFileValidator(projectDir, this.project.getName(), this.project.getArtifactId(),
                        gitRepository.getRepoNameFromRemote().orElse(this.project.getArtifactId()), enabledModules,
                        excludedFilesMatcher),
                new LicenseFileValidator(projectDir, excludedFilesMatcher),
                new PomFileValidator(enabledModules, this.excludedPlugins, pomFile),
                new ChangesFileValidator(this.project.getVersion(), this.project.getName(), projectDir,
                        excludedFilesMatcher, mvnRepo, ownVersion),
                new ChangelogFileValidator(projectDir, excludedFilesMatcher),
                new DependenciesValidator(pomFile, projectDir, brokenLinkReplacer, mvnRepo, ownVersion),
                new DeletedFilesValidator(projectDir, excludedFilesMatcher),
                new GitignoreFileValidator(projectDir, excludedFilesMatcher));
    }
}
