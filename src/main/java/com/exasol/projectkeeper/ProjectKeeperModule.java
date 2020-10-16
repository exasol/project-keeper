package com.exasol.projectkeeper;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Enum of supported modules.
 */
public enum ProjectKeeperModule {
    DEFAULT, MAVEN_CENTRAL, JAR_ARTIFACT, INTEGRATION_TESTS;

    /**
     * Get {@link ProjectKeeperModule} by its name.
     * 
     * @param moduleName module name to search for.
     * @return {@link ProjectKeeperModule}
     * @throws IllegalArgumentException if nothing was found
     */
    public static ProjectKeeperModule getModuleByName(final String moduleName) {
        try {
            return valueOf(moduleName.toUpperCase());
        } catch (final IllegalArgumentException exception) {
            throw new IllegalArgumentException("E-PK-4 Unknown module: '" + moduleName + "'. "
                    + "Please update your <modules> configuration in the pom.file to only use supported modules: "
                    + Arrays.stream(ProjectKeeperModule.values()).map(ProjectKeeperModule::name)
                            .map(String::toLowerCase).collect(Collectors.joining(", ")),
                    exception);
        }
    }
}
