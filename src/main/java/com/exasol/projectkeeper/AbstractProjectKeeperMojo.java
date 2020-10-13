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
    public List<Module> getModules() {
        final ArrayList<Module> enabledModules = this.modules.stream().map(Module::getModuleByName)
                .collect(Collectors.toCollection(ArrayList::new));
        if (!enabledModules.contains(Module.DEFAULT)) {
            enabledModules.add(Module.DEFAULT);
        }
        return enabledModules;
    }
}
