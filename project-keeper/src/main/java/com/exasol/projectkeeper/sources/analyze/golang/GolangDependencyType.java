package com.exasol.projectkeeper.sources.analyze.golang;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChange;

class GolangDependencyType {

    static final Logger LOGGER = Logger.getLogger(GolangDependencyType.class.getName());

    private final List<ProjectDependency> dependencies;
    private final Map<String, Type> types;

    GolangDependencyType(final List<ProjectDependency> dependencies) {
        this.dependencies = dependencies;
        this.types = dependencies.stream()
                .collect(Collectors.toMap(ProjectDependency::getName, ProjectDependency::getType));
    }

    Type getType(final DependencyChange change) {
        final String moduleName = change.getArtifactId();
        if (isGolangRuntime(moduleName)) {
            return Type.COMPILE;
        }
        final Type type = this.types.get(moduleName);
        if (type != null) {
            return type;
        }
        return getTypeByPrefix(moduleName);
    }

    private boolean isGolangRuntime(final String name) {
        return name.equals(GolangServices.GOLANG_DEPENDENCY_NAME);
    }

    private Type getTypeByPrefix(final String moduleName) {
        final DependencyMatcher dependencyMatcher = DependencyMatcher.forModule(moduleName);
        return this.dependencies.stream() //
                .filter(dependencyMatcher::matches) //
                .map(ProjectDependency::getType) //
                .findFirst() //
                .orElse(Type.UNKNOWN);
    }

    static interface DependencyMatcher {
        static final Pattern PATTERN = Pattern.compile(".*/v\\d+");
        static final DependencyMatcher NEVER = dep -> false;

        static DependencyMatcher forModule(final String moduleName) {
            if (!PATTERN.matcher(moduleName).matches()) {
                return NEVER;
            }
            final String prefix = moduleName.substring(0, moduleName.lastIndexOf("/"));
            LOGGER.finest(() -> "Found prefix '" + prefix + "' for module '" + moduleName + "'.");
            return new PrefixMatcher(prefix);
        }

        /**
         * Check if the name of a given dependency matches this matcher's prefix
         *
         * @param dependency dependency to check
         * @return {@code true} if dependency matches
         */
        public boolean matches(final ProjectDependency dependency);

        static class PrefixMatcher implements DependencyMatcher {
            private final String prefix;

            PrefixMatcher(final String prefix) {
                this.prefix = prefix;
            }

            @Override
            public boolean matches(final ProjectDependency dependency) {
                return dependency.getName().startsWith(this.prefix);
            }
        }
    }
}