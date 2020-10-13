package com.exasol.projectkeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Abstract basis for Mojos in this project.
 */
public abstract class AbstractProjectKeeperMojo extends AbstractMojo {
    public static final String MODULE_DEFAULT = "default";
    public static final String MODULE_MAVEN_CENTRAL = "mavenCentral";
    public static final String MODULE_JAR_ARTIFACT = "jarArtifact";
    public static final String MODULE_INTEGRATION_TESTS = "integrationTests";
    public static final List<String> SUPPORTED_MODULES = List.of(MODULE_DEFAULT, MODULE_MAVEN_CENTRAL,
            MODULE_JAR_ARTIFACT, MODULE_INTEGRATION_TESTS);

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
    public List<String> getModules() {
        validateModules();
        if (!this.modules.contains(MODULE_DEFAULT)) {
            this.modules = new ArrayList<>(this.modules);
            this.modules.add(MODULE_DEFAULT);
        }
        return this.modules;
    }

    private void validateModules() {
        for (final String module : this.modules) {
            if (!SUPPORTED_MODULES.contains(module)) {
                throw new IllegalArgumentException("E-PK-4 Unknown module: '" + module + "'. "
                        + "Please update your <modules> configuration in the pom.file to only use supported modules: "
                        + String.join(", ", SUPPORTED_MODULES) + ".");
            }
        }
    }
}
