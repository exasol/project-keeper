package com.exasol.projectkeeper.validators.changelog.dependencies;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.maven.model.Dependency;

/**
 * This class compares two lists of maven dependencies.
 */
public class MavenDependencyComparator {

    /**
     * Compare two lists of maven dependencies.
     * 
     * @param oldDependencies old list of dependencies
     * @param newDependencies new list of dependencies
     * @return list of dependency changes
     */
    public List<DependencyChange> compare(final List<Dependency> oldDependencies,
            final List<Dependency> newDependencies) {
        final List<DependencyChange> report = new ArrayList<>();
        final Map<String, Dependency> oldDependenciesMap = createMapWithDependencyKey(oldDependencies);
        final Map<String, Dependency> newDependenciesMap = createMapWithDependencyKey(newDependencies);
        oldDependenciesMap.forEach((key, oldDependency) -> {
            if (newDependenciesMap.containsKey(key)) {
                addDependencyUpdateIfVersionChanged(report, newDependenciesMap.get(key), oldDependency);
            } else {
                addDependencyRemove(report, oldDependency);
            }
        });
        newDependenciesMap.forEach((key, newDependency) -> {
            if (!oldDependenciesMap.containsKey(key)) {
                addDependencyAdd(report, newDependency);
            }
        });
        return report;
    }

    private void addDependencyAdd(final List<DependencyChange> report, final Dependency newDependency) {
        report.add(new DependencyAdd(newDependency.getGroupId(), newDependency.getArtifactId(),
                newDependency.getVersion()));
    }

    private void addDependencyRemove(final List<DependencyChange> report, final Dependency oldDependency) {
        report.add(new DependencyRemove(oldDependency.getGroupId(), oldDependency.getArtifactId(),
                oldDependency.getVersion()));
    }

    private void addDependencyUpdateIfVersionChanged(final List<DependencyChange> report,
            final Dependency newDependency, final Dependency oldDependency) {
        final String newDependencyVersion = newDependency.getVersion();
        if (newDependencyVersion != null && !newDependencyVersion.equals(oldDependency.getVersion())) {
            report.add(new DependencyUpdate(oldDependency.getGroupId(), oldDependency.getArtifactId(),
                    oldDependency.getVersion(), newDependencyVersion));
        }
    }

    private Map<String, Dependency> createMapWithDependencyKey(final List<Dependency> dependencies) {
        return dependencies.stream().collect(Collectors.toMap(this::buildDependencyKey, each -> each));
    }

    private String buildDependencyKey(final Dependency each) {
        return each.getGroupId() + each.getArtifactId();
    }
}
