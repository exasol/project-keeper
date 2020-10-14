package com.exasol.projectkeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Abstract basis for Mojos in this project.
 */
public abstract class AbstractProjectKeeperMojo extends AbstractMojo {

    @Parameter(property = "modules")
    private List<String> modules;

    /**
     * Get a list of enabled modules.
     * 
     * <p>
     * Configure using {@code <modules></modules> } in the pom file.
     * </p>
     * 
     * @return list of enabled modules.
     */
    public List<ProjectKeeperModule> getEnabledModules() {
        final ArrayList<ProjectKeeperModule> enabledModules = this.modules.stream()
                .map(ProjectKeeperModule::getModuleByName).collect(Collectors.toCollection(ArrayList::new));
        if (!enabledModules.contains(ProjectKeeperModule.DEFAULT)) {
            enabledModules.add(ProjectKeeperModule.DEFAULT);
        }
        return enabledModules;
    }
}
