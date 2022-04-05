package com.exasol.projectkeeper.shared.dependencies;

import java.util.List;

import lombok.*;

/**
 * Maven dependency.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDependency {
    /** Dependency name */
    private String name;
    /** Website url */
    private String websiteUrl;
    /** License */
    private List<License> licenses;
    /** Dependency Type */
    private Type type;

    /**
     * Type of a {@link ProjectDependency}.
     */
    public enum Type {
        /** Compile dependency. */
        COMPILE,
        /** Runtime dependency */
        RUNTIME,
        /** Test dependency */
        TEST,
        /** Plugin */
        PLUGIN
    }
}
