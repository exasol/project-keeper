package com.exasol.projectkeeper.shared.config;

import java.nio.file.Path;
import java.util.*;

/**
 * Project sources, e.g. a maven project.
 * <p>
 * Use {@link Source#builder()} to create a new instance.
 */
public final class Source {
    private final Path path;
    private final SourceType type;
    // [impl->dsn~modules~1]
    private final Set<ProjectKeeperModule> modules;
    private final boolean advertise;

    /**
     * Reference to the parent pom. For maven sources only. {@code null} if not provided.
     */
    private final ParentPomRef parentPom;

    private Source(final SourceBuilder builder) {
        this.path = builder.path;
        this.type = builder.type;
        this.modules = builder.modules;
        this.advertise = builder.advertise;
        this.parentPom = builder.parentPom;
    }

    /** @return Path to the source-project root or build file. Example: {@code my-project/pom.xml} */
    public Path getPath() {
        return path;
    }

    /**
     * Check if this sources represents the root project, i.e. the path is the root of the project. A path of
     * {@code my-project/pom.xml} will return {@code false}, while a path of {@code pom.xml} will return {@code true}.
     * 
     * @return {@code true} if this source represents the root project
     */
    public boolean isRoot() {
        return this.getPath().getParent() == null;
    }

    /** @return Type if the source-project Example: {@code MAVEN} */
    public SourceType getType() {
        return type;
    }

    /** @return List with project-keeper modules */
    public Set<ProjectKeeperModule> getModules() {
        return modules;
    }

    /** @return {@code true} if this should be advertised */
    public boolean isAdvertised() {
        return advertise;
    }

    /** @return the Maven parent POM */
    public ParentPomRef getParentPom() {
        return parentPom;
    }

    /** @return a builder for creating new {@link Source} instances */
    public static Source.SourceBuilder builder() {
        return new Source.SourceBuilder();
    }

    /** Builder for creating new {@link Source} instances */
    public static class SourceBuilder {
        private Path path;
        private SourceType type;
        private Set<ProjectKeeperModule> modules = Collections.emptySet();
        private boolean advertise = true;
        private ParentPomRef parentPom;

        private SourceBuilder() {
            // empty by intention
        }

        /**
         * @param path Path to the source-project root or build file. Example: {@code my-project/pom.xml}.
         * @return {@code this}.
         */
        public Source.SourceBuilder path(final Path path) {
            this.path = path;
            return this;
        }

        /**
         * @param type Type if the source-project Example: {@code MAVEN}.
         * @return {@code this}.
         */
        public Source.SourceBuilder type(final SourceType type) {
            this.type = type;
            return this;
        }

        /**
         * @param modules List with project-keeper modules
         * @return {@code this}.
         */
        public Source.SourceBuilder modules(final Set<ProjectKeeperModule> modules) {
            this.modules = modules;
            return this;
        }

        /**
         * @param advertise {@code true} if this should be advertised
         * @return {@code this}.
         */
        public Source.SourceBuilder advertise(final boolean advertise) {
            this.advertise = advertise;
            return this;
        }

        /**
         * @param parentPom Reference to the parent pom. For maven sources only. {@code null} if not provided.
         * @return {@code this}.
         */
        public Source.SourceBuilder parentPom(final ParentPomRef parentPom) {
            this.parentPom = parentPom;
            return this;
        }

        /** @return a new instance */
        public Source build() {
            return new Source(this);
        }
    }

    @Override
    public String toString() {
        return "Source [path=" + path + ", type=" + type + ", modules=" + modules + ", advertise=" + advertise
                + ", parentPom=" + parentPom + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, type, modules, advertise, parentPom);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Source other = (Source) obj;
        return Objects.equals(path, other.path) && type == other.type && Objects.equals(modules, other.modules)
                && advertise == other.advertise && Objects.equals(parentPom, other.parentPom);
    }
}
