package com.exasol.projectkeeper.validators.changesfile.dependencies;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.maven.model.Dependency;

import com.exasol.projectkeeper.shared.dependencychanges.*;

/**
 * This class compares two lists of maven dependencies.
 */
class MavenDependencyComparator {

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
        findOutWhatHappenedToDependenciesOfPreviousRelease(report, oldDependenciesMap, newDependenciesMap);
        checkIfThisReleaseHasNewDependencies(report, oldDependenciesMap, newDependenciesMap);
        return report;
    }

    private void checkIfThisReleaseHasNewDependencies(final List<DependencyChange> report,
            final Map<String, Dependency> oldDependenciesMap, final Map<String, Dependency> newDependenciesMap) {
        newDependenciesMap.forEach((key, newDependency) -> {
            if (!oldDependenciesMap.containsKey(key)) {
                addAddedDependency(report, newDependency);
            }
        });
    }

    private void findOutWhatHappenedToDependenciesOfPreviousRelease(final List<DependencyChange> report,
            final Map<String, Dependency> oldDependenciesMap, final Map<String, Dependency> newDependenciesMap) {
        oldDependenciesMap.forEach((key, oldDependency) -> {
            if (newDependenciesMap.containsKey(key)) {
                addUpdatedDependencyIfVersionChanged(report, newDependenciesMap.get(key), oldDependency);
            } else {
                addRemovedDependency(report, oldDependency);
            }
        });
    }

    private void addAddedDependency(final List<DependencyChange> report, final Dependency newDependency) {
        report.add(new NewDependency(newDependency.getGroupId(), newDependency.getArtifactId(),
                newDependency.getVersion()));
    }

    private void addRemovedDependency(final List<DependencyChange> report, final Dependency oldDependency) {
        report.add(new RemovedDependency(oldDependency.getGroupId(), oldDependency.getArtifactId(),
                oldDependency.getVersion()));
    }

    private void addUpdatedDependencyIfVersionChanged(final List<DependencyChange> report,
            final Dependency newDependency, final Dependency oldDependency) {
        final String newDependencyVersion = newDependency.getVersion();
        if (newDependencyVersion != null && !newDependencyVersion.equals(oldDependency.getVersion())) {
            report.add(new UpdatedDependency(oldDependency.getGroupId(), oldDependency.getArtifactId(),
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
