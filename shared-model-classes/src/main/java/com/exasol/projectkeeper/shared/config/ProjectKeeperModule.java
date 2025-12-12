package com.exasol.projectkeeper.shared.config;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;

/**
 * Enum of supported modules.
 */
public enum ProjectKeeperModule {
    /**
     * Default module.
     */
    DEFAULT,
    /**
     * Module for project that are released on maven-central.
     */
    MAVEN_CENTRAL,
    /**
     * Module for projects that are released as a fat-jar.
     */
    JAR_ARTIFACT,
    /**
     * Module for projects with integration tests.
     */
    INTEGRATION_TESTS,
    /**
     * Module for projects where code coverage should be extracted from external JVMs.
     */
    UDF_COVERAGE,
    /**
     * Module for projects that use Project Lombok.
     */
    LOMBOK,

    /** Module for java projects that build a native-image (executable). */
    NATIVE_IMAGE;

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
                    ExaError.messageBuilder("E-PK-SMC-81").message("Unknown module: {{module name}}. "
                            + "Please update your modules configuration in the .project-keeper.yml to use one of the supported modules: {{supported modules}}")
                            .parameter("module name", moduleName).parameter("supported modules", supportedModules)
                            .toString(),
                    exception);
        }
    }
}
