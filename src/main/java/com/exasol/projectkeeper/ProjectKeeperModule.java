package com.exasol.projectkeeper;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Enum of supported modules.
 */
public enum ProjectKeeperModule {
    DEFAULT("default"), MAVEN_CENTRAL("maven_central"), JAR_ARTIFACT("jar_artifact"),
    INTEGRATION_TESTS("integration_tests");

    private final String moduleName;

    ProjectKeeperModule(final String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * Get {@link ProjectKeeperModule} by its name.
     * 
     * @param moduleName module name to search for.
     * @return {@link ProjectKeeperModule}
     * @throws IllegalArgumentException if nothing was found
     */
    public static ProjectKeeperModule getModuleByName(final String moduleName) {
        return Arrays.stream(ProjectKeeperModule.values()).filter(module -> module.moduleName.equals(moduleName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("E-PK-4 Unknown module: '" + moduleName + "'. "
                        + "Please update your <modules> configuration in the pom.file to only use supported modules: "
                        + Arrays.stream(ProjectKeeperModule.values()).map(ProjectKeeperModule::toString)
                                .collect(Collectors.joining(", "))
                        + "."));
    }

    @Override
    public String toString() {
        return this.moduleName;
    }
}
