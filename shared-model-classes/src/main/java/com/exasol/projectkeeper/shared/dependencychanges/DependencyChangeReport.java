package com.exasol.projectkeeper.shared.dependencychanges;

import java.util.*;
import java.util.function.Function;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;

/**
 * This class represents a report of changed dependencies.
 */
public class DependencyChangeReport {

    /**
     * @return builder for {@link DependencyChangeReport}
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Dependency changes in various scopes */
    private Map<Type, List<DependencyChange>> changes = new EnumMap<>(Type.class);

    /**
     * Default constructor for a new instance of {@link DependencyChangeReport}.
     */
    public DependencyChangeReport() {
        for (final Type type : Type.values()) {
            this.changes.put(type, new ArrayList<>());
        }
    }

    /**
     * @param type {@link Type} of dependencies to get changes for
     * @return list of changes for the specified type
     */
    public List<DependencyChange> getChanges(final Type type) {
        return this.changes.get(type);
    }

    /**
     * @return map containing list changes for each {@link Type} of dependencies
     */
    // only for serialization and deserialization
    public Map<Type, List<DependencyChange>> getChanges() {
        return this.changes;
    }

    /**
     * @param changes map containing list changes for each {@link Type} of dependencies
     */
    // only for serialization and deserialization
    public void setChanges(final Map<Type, List<DependencyChange>> changes) {
        this.changes = changes;
    }

    // old ordering: compile, runtime, test, plugin

    /**
     * Builder for new instances of class{@link DependencyChangeReport}
     */
    public static final class Builder {
        private final DependencyChangeReport report = new DependencyChangeReport();

        /**
         * Adds a list of changes for the specified {@link Type} of the changed dependencies.
         *
         * @param type    {@link Type} of the changed dependencies.
         * @param changes list of changes
         * @return this for fluent programming
         */
        public Builder typed(final Type type, final List<DependencyChange> changes) {
            this.report.changes.put(type, changes);
            return this;
        }

        /**
         * Adds a list of changes each with an individual {@link Type} of the changed dependency.
         *
         * @param changes      list of changes
         * @param typeDetector type detector to identify the {@link Type} of the dependency for each change
         * @return this for fluent programming
         */
        public Builder mixed(final List<DependencyChange> changes,
                final Function<DependencyChange, Type> typeDetector) {
            for (final DependencyChange c : changes) {
                this.report.changes.get(typeDetector.apply(c)).add(c);
            }
            return this;
        }

        /**
         * @return new instance of {@link DependencyChangeReport}.
         */
        public DependencyChangeReport build() {
            return this.report;
        }
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
        final DependencyChangeReport other = (DependencyChangeReport) obj;
        return Objects.equals(this.changes, other.changes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.changes);
    }
}
