package com.exasol.projectkeeper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.exasol.projectkeeper.validators.ChangelogValidator;
import com.exasol.projectkeeper.validators.files.ProjectFilesValidator;
import com.exasol.projectkeeper.validators.pom.PomFileValidator;

/**
 * Abstract basis for Mojos in this project.
 */
public abstract class AbstractProjectKeeperMojo extends AbstractMojo {

    @Parameter(property = "modules")
    private List<String> modules;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

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
        final Set<ProjectKeeperModule> enabledModules = getEnabledModules();
        return List.of(new ProjectFilesValidator(enabledModules, this.project.getBasedir()),
                new PomFileValidator(enabledModules, this.project.getModel().getPomFile()),
                new ChangelogValidator(this.project));
    }
}
