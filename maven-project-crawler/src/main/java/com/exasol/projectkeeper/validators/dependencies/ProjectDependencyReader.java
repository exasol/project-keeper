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
                .map(dependency -> getLicense(dependency, project)) //
                .collect(Collectors.toList());
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
                .filter(this::isExplicitPlugin) //
                .map(this::convertPluginToDependency);
    }

    /**
     * Check if the given plugin is an explicit or implicit plugin.
     * 
     * <ul>
     * <li>Direct plugin (e.g. {@code org.apache.maven.plugins:maven-failsafe-plugin}) are explicitly added to the build
     * in a pom or parent pom.
     * <ul>
     * <li>Source model ID is {@code com.exasol:project-keeper-shared-test-setup-generated-parent:$&#123;revision&#125;}
     * or {@code com.exasol:project-keeper-cli:$&#123;revision&#125;}</li></li>
     * <li>Source location is {@code /path/to/project-keeper/project-keeper-cli/pom.xml} or
     * {@code /path/to/project-keeper/project-keeper-maven-plugin/pk_generated_parent.pom}</li>
     * </ul>
     * <li>Indirect plugins (e.g. {@code org.apache.maven.plugins:maven-clean-plugin}) are implicitly added to the build
     * a Maven lifecycle.
     * <ul>
     * <li>Source model ID is {@code org.apache.maven:maven-core:3.8.7:default-lifecycle-bindings}</li>
     * <li>Source location is {@code null}</li>
     * </ul>
     * </li>
     * </ul>
     * The Maven API allows distinguishing both types via Source model ID and Source location. We decided to only use
     * the source location as this requires only a simple not null check.
     * 
     * @param plugin the plugin to check
     * @return {@code true} if the plugin is explicitly added to the build
     */
    // [impl -> dsn~dependency.md-file-validator-excludes-implicit-plugins~1]
    private boolean isExplicitPlugin(final Plugin plugin) {
        final String location = plugin.getLocation("").getSource().getLocation();
        return location != null;
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
