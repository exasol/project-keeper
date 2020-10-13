package com.exasol.projectkeeper;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Enum of supported modules.
 */
public enum Module {
    DEFAULT("default"), MAVEN_CENTRAL("mavenCentral"), JAR_ARTIFACT("jarArtifact"),
    INTEGRATION_TESTS("integrationTests");

    private final String moduleName;

    Module(final String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * Get {@link Module} by its name.
     * 
     * @param moduleName module name to search for.
     * @return {@link Module}
     * @throws IllegalArgumentException if nothing was found
     */
    public static Module getModuleByName(final String moduleName) {
        return Arrays.stream(Module.values()).filter(module -> module.moduleName.equals(moduleName)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("E-PK-4 Unknown module: '" + moduleName + "'. "
                        + "Please update your <modules> configuration in the pom.file to only use supported modules: "
                        + Arrays.stream(Module.values()).map(Module::toString).collect(Collectors.joining(", "))
                        + "."));
    }

    @Override
    public String toString() {
        return this.moduleName;
    }
}
