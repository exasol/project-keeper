package com.exasol.projectkeeper.validators.dependencies;

import static com.exasol.projectkeeper.validators.dependencies.Dependency.Scope.*;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.maven.model.*;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.ProjectBuildingException;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.pom.MavenArtifactModelReader;
import com.exasol.projectkeeper.pom.MavenFileModelReader;
import com.exasol.projectkeeper.validators.dependencies.Dependency.Scope;

/**
 * This class reads all dependencies of a pom file (including the plugins) together with their license.
 */
public class MavenDependencyReader {
    private final MavenFileModelReader fileModelReader;
    private final MavenArtifactModelReader artifactModelReader;

    /**
     * Create a new instance of {@link MavenDependencyReader}.
     * 
     * @param fileModelReader     pom file parser
     * @param artifactModelReader maven dependency reader
     */
    public MavenDependencyReader(final MavenFileModelReader fileModelReader,
            final MavenArtifactModelReader artifactModelReader) {
        this.fileModelReader = fileModelReader;
        this.artifactModelReader = artifactModelReader;
    }

    /**
     * Read the dependencies of the pom file (including plugins).
     * 
     * @param pomFile pom file to read
     * @return list of dependencies
     */
    public List<com.exasol.projectkeeper.validators.dependencies.Dependency> readDependencies(final File pomFile) {
        final Model model = parsePomFile(pomFile);
        return getDependenciesIncludingPlugins(model).map(this::getDependenciesLicense).collect(Collectors.toList());
    }

    private Stream<Dependency> getDependenciesIncludingPlugins(final Model model) {
        return Stream.concat(model.getDependencies().stream(),
                model.getBuild().getPlugins().stream().map(this::convertPluginToDependency));
    }

    private Dependency convertPluginToDependency(final Plugin plugin) {
        final Dependency dependency = new Dependency();
        dependency.setGroupId(plugin.getGroupId());
        dependency.setArtifactId(plugin.getArtifactId());
        dependency.setVersion(plugin.getVersion());
        dependency.setScope("plugin");
        return dependency;
    }

    private com.exasol.projectkeeper.validators.dependencies.Dependency getDependenciesLicense(
            final Dependency dependency) {
        try {
            final Model dependenciesPom = this.artifactModelReader.readModel(dependency.getArtifactId(),
                    dependency.getGroupId(), dependency.getVersion());
            final List<License> licenses = dependenciesPom.getLicenses().stream()
                    .map(license -> new License(license.getName(), license.getUrl())).collect(Collectors.toList());
            return new com.exasol.projectkeeper.validators.dependencies.Dependency(getName(dependenciesPom),
                    dependenciesPom.getUrl(), licenses, mapScope(dependency.getScope()));
        } catch (final ProjectBuildingException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-49")
                    .message("Failed to get license information for dependency {{groupId}}:{{artifactId}}.",
                            dependency.getGroupId(), dependency.getArtifactId())
                    .toString(), exception);
        }
    }

    private String getName(final Model dependenciesPom) {
        if (dependenciesPom.getName() == null || dependenciesPom.getName().isBlank()) {
            return dependenciesPom.getArtifactId();
        } else {
            return dependenciesPom.getName();
        }
    }

    private Scope mapScope(final String scope) {
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
                return Scope.RUNTIME;
            case "plugin":
                return PLUGIN;
            default:
                throw new IllegalStateException(ExaError.messageBuilder("F-PK-54")
                        .message("Unimplemented dependency scope {{scope}}.", scope).ticketMitigation().toString());
            }
        }
    }

    private Model parsePomFile(final File pomFile) {
        try {
            return this.fileModelReader.readModel(pomFile);
        } catch (final MavenFileModelReader.ReadFailedException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-48").message("Failed to parse pom.xml file.").toString(), exception);
        }
    }
}
