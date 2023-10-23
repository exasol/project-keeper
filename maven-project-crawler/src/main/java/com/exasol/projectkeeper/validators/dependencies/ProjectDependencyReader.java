package com.exasol.projectkeeper.validators.dependencies;

import static com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.*;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingException;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.pom.MavenModelFromRepositoryReader;
import com.exasol.projectkeeper.shared.dependencies.*;
import com.exasol.projectkeeper.shared.dependencies.License;

/**
 * This class reads all dependencies of a pom file (including the plugins) together with their license.
 */
public class ProjectDependencyReader {
    private final MavenModelFromRepositoryReader artifactModelReader;
    private final MavenProject project;

    /**
     * Create a new instance of {@link ProjectDependencyReader}.
     *
     * @param artifactModelReader maven dependency reader
     * @param project             maven project
     */
    public ProjectDependencyReader(final MavenModelFromRepositoryReader artifactModelReader,
            final MavenProject project) {
        this.artifactModelReader = artifactModelReader;
        this.project = project;
    }

    /**
     * Read the dependencies of the pom file (including plugins).
     *
     * @return list of dependencies
     */
    public ProjectDependencies readDependencies() {
        final List<ProjectDependency> dependencies = getDependenciesIncludingPlugins()
                .map(dependency -> getLicense(dependency, project)).collect(Collectors.toList());
        return new ProjectDependencies(dependencies);
    }

    private Stream<Dependency> getDependenciesIncludingPlugins() {
        return Stream.concat(getDependencies(), getPluginDependencies());
    }

    private Stream<Dependency> getDependencies() {
        return project.getModel().getDependencies().stream();
    }

    private Stream<Dependency> getPluginDependencies() {
        return project.getModel().getBuild().getPlugins().stream() //
                .filter(this::isDirectPlugin) //
                .map(this::convertPluginToDependency);
    }

    private boolean isDirectPlugin(final Plugin plugin) {
        System.err.println("--- plugin: " + plugin.getClass().getName() + ": " + plugin);
        return true;
    }

    private Dependency convertPluginToDependency(final Plugin plugin) {
        final var dependency = new Dependency();
        dependency.setGroupId(plugin.getGroupId());
        dependency.setArtifactId(plugin.getArtifactId());
        dependency.setVersion(plugin.getVersion());
        dependency.setScope("plugin");
        return dependency;
    }

    private ProjectDependency getLicense(final Dependency dependency, final MavenProject project) {
        try {
            final List<ArtifactRepository> repos = project.getRemoteArtifactRepositories();
            final var dependenciesPom = this.artifactModelReader.readModel(dependency.getArtifactId(),
                    dependency.getGroupId(), dependency.getVersion(), repos);
            final List<License> licenses = dependenciesPom.getLicenses().stream()
                    .map(license -> new License(license.getName(), license.getUrl())).collect(Collectors.toList());
            return ProjectDependency.builder() //
                    .type(mapScopeToDependencyType(dependency.getScope())) //
                    .name(getDependencyName(dependenciesPom)) //
                    .websiteUrl(dependenciesPom.getUrl()) //
                    .licenses(licenses) //
                    .build();
        } catch (final ProjectBuildingException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-MPC-49")
                    .message("Failed to get license information for dependency {{groupId}}:{{artifactId}}.",
                            dependency.getGroupId(), dependency.getArtifactId())
                    .toString(), exception);
        }
    }

    private String getDependencyName(final Model dependenciesPom) {
        if ((dependenciesPom.getName() == null) || dependenciesPom.getName().isBlank()) {
            return dependenciesPom.getArtifactId();
        } else {
            return dependenciesPom.getName();
        }
    }

    private BaseDependency.Type mapScopeToDependencyType(final String scope) {
        if (scope == null) {
            return COMPILE;
        } else {
            switch (scope) {
            case "compile":
            case "provided":
            case "system":
            case "import":
                return COMPILE;
            case "test":
                return TEST;
            case "runtime":
                return RUNTIME;
            case "plugin":
                return PLUGIN;
            default:
                throw new IllegalStateException(ExaError.messageBuilder("F-PK-MPC-54")
                        .message("Unimplemented dependency scope {{scope}}.", scope).ticketMitigation().toString());
            }
        }
    }
}
