package com.exasol.projectkeeper.validators.changesfile.dependencies;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.maven.model.*;

/**
 * Reader for {@link DependencyChangeReportReader}.
 */
public class DependencyChangeReportReader {

    private final MavenDependencyComparator dependencyComparator;

    /**
     * Create a new instance of {@link DependencyChangeReportReader}.
     */
    public DependencyChangeReportReader() {
        this.dependencyComparator = new MavenDependencyComparator();
    }

    /**
     * Read a dependency change report by comparing two maven models.
     * 
     * @param oldModel previous releases maven model
     * @param newModel current maven model
     * @return read model
     */
    public DependencyChangeReport read(final Model oldModel, final Model newModel) {
        return new DependencyChangeReport(//
                sort(this.compareDependencies(oldModel, newModel, "compile")),
                sort(this.compareDependencies(oldModel, newModel, "runtime")),
                sort(this.compareDependencies(oldModel, newModel, "test")),
                sort(this.comparePluginDependencies(oldModel, newModel)));
    }

    private List<DependencyChange> compareDependencies(final Model oldModel, final Model newModel, final String scope) {
        final List<Dependency> oldDependencies = filterDependenciesByScope(oldModel, scope);
        final List<Dependency> newDependencies = filterDependenciesByScope(newModel, scope);
        return this.dependencyComparator.compare(oldDependencies, newDependencies);
    }

    private List<DependencyChange> comparePluginDependencies(final Model oldModel, final Model newModel) {
        final List<Dependency> oldPluginDependencies = convertPluginsToDependencies(oldModel);
        final List<Dependency> newPluginDependencies = convertPluginsToDependencies(newModel);
        return this.dependencyComparator.compare(oldPluginDependencies, newPluginDependencies);
    }

    private List<Dependency> filterDependenciesByScope(final Model model, final String scope) {
        return model.getDependencies().stream()
                .filter(dependency -> (scope.equals("compile")
                        && (dependency.getScope() == null || dependency.getScope().equals("")))
                        || scope.equals(dependency.getScope()))
                .collect(Collectors.toList());
    }

    private List<Dependency> convertPluginsToDependencies(final Model oldModel) {
        return oldModel.getBuild().getPlugins().stream().map(this::convertPluginToDependency)
                .collect(Collectors.toList());
    }

    private Dependency convertPluginToDependency(final Plugin plugin) {
        final Dependency dependency = new Dependency();
        dependency.setGroupId(plugin.getGroupId());
        dependency.setArtifactId(plugin.getArtifactId());
        dependency.setVersion(plugin.getVersion());
        return dependency;
    }

    private List<DependencyChange> sort(final List<DependencyChange> dependencyChanges) {
        final List<DependencyChange> result = new ArrayList<>(dependencyChanges);
        result.sort(new AlphabeticalDependencyComparator());
        return result;
    }

    private static class AlphabeticalDependencyComparator implements Comparator<DependencyChange> {
        @Override
        public int compare(final DependencyChange a, final DependencyChange b) {
            return getComparableString(a).compareTo(getComparableString(b));
        }

        private String getComparableString(final DependencyChange dependencyChange) {
            return dependencyChange.getGroupId() + dependencyChange.getArtifactId() + dependencyChange.getVersion();
        }
    }
}
