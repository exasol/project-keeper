package com.exasol.projectkeeper;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;

/**
 * Enum of supported modules.
 */
public enum ProjectKeeperModule {
    DEFAULT, MAVEN_CENTRAL, JAR_ARTIFACT, INTEGRATION_TESTS, UDF_COVERAGE;

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
            final String supportedModules = Arrays.stream(ProjectKeeperModule.values()).map(ProjectKeeperModule::name)
                    .map(String::toLowerCase).collect(Collectors.joining(", "));
            throw new IllegalArgumentException(
                    ExaError.messageBuilder("E-PK-4").message("Unknown module: {{module name}}. "
                            + "Please update your <modules> configuration in the pom.file to use one of the supported modules: {{supported modules}}")
                            .parameter("module name", moduleName).parameter("supported modules", supportedModules)
                            .toString(),
                    exception);
        }
    }
}
