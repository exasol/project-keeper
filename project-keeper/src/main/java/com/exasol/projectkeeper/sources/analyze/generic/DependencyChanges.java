package com.exasol.projectkeeper.sources.analyze.generic;

import static java.util.Collections.emptyList;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.shared.dependencies.VersionedDependency;
import com.exasol.projectkeeper.shared.dependencychanges.*;

public class DependencyChanges {

    /**
     * @return builder for a new instance of {@link DependencyChanges}
     */
    public static DependencyChanges builder() {
        return new DependencyChanges();
    }

    private Map<String, VersionedDependency> from;
    private Map<String, VersionedDependency> to;
    private final List<DependencyChange> changes = new ArrayList<>();

    /**
     * @param dependencies previous list of dependencies
     * @return this for fluent programming
     */
    public DependencyChanges from(final Optional<List<VersionedDependency>> dependencies) {
        this.from = toMap(dependencies.orElse(emptyList()));
        return this;
    }

    /**
     * @param dependencies current list of dependencies
     * @return this for fluent programming
     */
    public DependencyChanges to(final List<VersionedDependency> dependencies) {
        this.to = toMap(dependencies);
        return this;
    }

    private Map<String, VersionedDependency> toMap(final List<VersionedDependency> dependencies) {
        return dependencies.stream().collect(Collectors.toMap(VersionedDependency::getName, Function.identity()));
    }

    /**
     * @return list of dependency changes
     */
    public List<DependencyChange> build() {
        for (final Entry<String, VersionedDependency> entry : this.to.entrySet()) {
            final String depName = entry.getKey();
            final String newVersion = entry.getValue().getVersion();
            if (this.from.containsKey(depName)) {
                final String oldVersion = this.from.get(depName).getVersion();
                if (!oldVersion.equals(newVersion)) {
                    updatedDependency(depName, oldVersion, newVersion);
                }
            } else {
                addedDependency(depName, newVersion);
            }
        }
        for (final Entry<String, VersionedDependency> entry : this.from.entrySet()) {
            final String depName = entry.getKey();
            if (!this.to.containsKey(depName)) {
                removedDependency(depName, entry.getValue().getVersion());
            }
        }
        return this.changes;
    }

    private void addedDependency(final String module, final String version) {
        this.changes.add(new NewDependency(null, module, version));
    }

    private void removedDependency(final String module, final String version) {
        this.changes.add(new RemovedDependency(null, module, version));
    }

    private void updatedDependency(final String module, final String oldVersion, final String newVersion) {
        this.changes.add(new UpdatedDependency(null, module, oldVersion, newVersion));
    }

    /**
     * Add a change to current list of dependency changes. Change can be
     * <ul>
     * <li>either a new dependency has been added,</li>
     * <li>an existing dependency has been removed,</li>
     * <li>or the version of an existing dependency has been changed, e.g. updated to a newer version.</li>
     * </ul>
     *
     * @param module   name of the dependency's module
     * @param previous module version as of the previous dependency
     * @param current  module version as of the current dependency
     * @return this for fluent programming
     */
    public DependencyChanges withChange(final String module, final Optional<String> previous,
            final Optional<String> current) {
        if (previous.isEmpty() && (current.isPresent())) {
            addedDependency(module, current.get());
        } else if ((previous.isPresent()) && (current.isEmpty())) {
            removedDependency(module, previous.get());
        } else if ((previous.isPresent()) && !previous.equals(current)) {
            updatedDependency(module, previous.get(), current.get());
        }
        return this;
    }
}
