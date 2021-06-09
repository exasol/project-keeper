package com.exasol.projectkeeper.validators.dependencies;

import static com.exasol.projectkeeper.validators.dependencies.ProjectDependency.Type.*;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.maven.model.*;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingException;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.pom.MavenModelFromRepositoryReader;
import com.exasol.projectkeeper.pom.MavenProjectFromFileReader;

/**
 * This class reads all dependencies of a pom file (including the plugins) together with their license.
 */
public class ProjectDependencyReader {
    private final MavenProjectFromFileReader fileModelReader;
    private final MavenModelFromRepositoryReader artifactModelReader;

    /**
     * Create a new instance of {@link ProjectDependencyReader}.
     * 
     * @param fileModelReader     pom file parser
     * @param artifactModelReader maven dependency reader
     */
    public ProjectDependencyReader(final MavenProjectFromFileReader fileModelReader,
            final MavenModelFromRepositoryReader artifactModelReader) {
        this.fileModelReader = fileModelReader;
        this.artifactModelReader = artifactModelReader;
    }

    /**
     * Read the dependencies of the pom file (including plugins).
     * 
     * @param pomFile pom file to read
     * @return list of dependencies
     */
    public List<ProjectDependency> readDependencies(final File pomFile) {
        final var project = readProjectFromPomFile(pomFile);
        return getDependenciesIncludingPlugins(project.getModel()).map(dependency -> getLicense(dependency, project))
                .collect(Collectors.toList());
    }

    private Stream<Dependency> getDependenciesIncludingPlugins(final Model model) {
        return Stream.concat(model.getDependencies().stream(),
                model.getBuild().getPlugins().stream().map(this::convertPluginToDependency));
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
            final var dependenciesPom = this.artifactModelReader.readModel(dependency.getArtifactId(),
                    dependency.getGroupId(), dependency.getVersion(), project.getRemoteArtifactRepositories());
            final List<License> licenses = dependenciesPom.getLicenses().stream()
                    .map(license -> new License(license.getName(), license.getUrl())).collect(Collectors.toList());
            return new ProjectDependency(getDependencyName(dependenciesPom), dependenciesPom.getUrl(), licenses,
                    mapScopeToDependencyType(dependency.getScope()));
        } catch (final ProjectBuildingException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-49")
                    .message("Failed to get license information for dependency {{groupId}}:{{artifactId}}.",
                            dependency.getGroupId(), dependency.getArtifactId())
                    .toString(), exception);
        }
    }

    private String getDependencyName(final Model dependenciesPom) {
        if (dependenciesPom.getName() == null || dependenciesPom.getName().isBlank()) {
            return dependenciesPom.getArtifactId();
        } else {
            return dependenciesPom.getName();
        }
    }

    private ProjectDependency.Type mapScopeToDependencyType(final String scope) {
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
                throw new IllegalStateException(ExaError.messageBuilder("F-PK-54")
                        .message("Unimplemented dependency scope {{scope}}.", scope).ticketMitigation().toString());
            }
        }
    }

    private MavenProject readProjectFromPomFile(final File pomFile) {
        try {
            return this.fileModelReader.readProject(pomFile);
        } catch (final MavenProjectFromFileReader.ReadFailedException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-48").message("Failed to parse pom.xml file.").toString(), exception);
        }
    }
}
