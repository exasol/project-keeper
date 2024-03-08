package com.exasol.projectkeeper.sources;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.validators.pom.PomFileGenerator;

/**
 * {@link AnalyzedSource} for maven source.
 * <p>
 * Use {@link AnalyzedMavenSource#builder()} to create a new instance.
 */
public final class AnalyzedMavenSource implements AnalyzedSource {
    private final Path path;
    private final Set<ProjectKeeperModule> modules;
    private final boolean advertise;
    private final String artifactId;
    private final String projectName;
    private final String version;
    private final String releaseArtifactName;
    private final String javaVersion;
    private final DependencyChangeReport dependencyChanges;
    private final ProjectDependencies dependencies;
    private final boolean isRootProject;

    private AnalyzedMavenSource(final AnalyzedMavenSourceBuilder builder) {
        this.path = builder.path;
        this.modules = builder.modules;
        this.advertise = builder.advertise;
        this.artifactId = builder.artifactId;
        this.projectName = builder.projectName;
        this.version = builder.version;
        this.releaseArtifactName = builder.releaseArtifactName;
        this.javaVersion = builder.javaVersion;
        this.dependencyChanges = builder.dependencyChanges;
        this.dependencies = builder.dependencies;
        this.isRootProject = builder.isRootProject;
    }

    /** @return new builder for an {@link AnalyzedMavenSource}. */
    public static AnalyzedMavenSource.AnalyzedMavenSourceBuilder builder() {
        return new AnalyzedMavenSource.AnalyzedMavenSourceBuilder();
    }

    /** @return source path */
    @Override
    public Path getPath() {
        return this.path;
    }

    /** @return modules */
    @Override
    public Set<ProjectKeeperModule> getModules() {
        return this.modules;
    }

    /** @return advertise this source or not */
    @Override
    public boolean isAdvertised() {
        return this.advertise;
    }

    /** @return Maven artifact ID */
    public String getArtifactId() {
        return this.artifactId;
    }

    /** @return project name */
    @Override
    public String getProjectName() {
        return this.projectName;
    }

    /** @return Project version */
    @Override
    public String getVersion() {
        return this.version;
    }

    /** @return artifact file name or {@code null} if no artifact is built */
    public String getReleaseArtifactName() {
        return this.releaseArtifactName;
    }

    /**
     * @return Java version used for compiling and testing the project. Read from the {@code properties/java.version}
     *         element of the {@code pom.xml} file. Defaults to {@link PomFileGenerator#DEFAULT_JAVA_VERSION}.
     */
    public String getJavaVersion() {
        return this.javaVersion;
    }

    /** @return dependency changes */
    @Override
    public DependencyChangeReport getDependencyChanges() {
        return this.dependencyChanges;
    }

    /** @return dependencies */
    @Override
    public ProjectDependencies getDependencies() {
        return this.dependencies;
    }

    /**
     * @return {@code true} if this is the main maven project in the repo (if pom lies directly in repo)
     */
    public boolean isRootProject() {
        return this.isRootProject;
    }

    /**
     * Builder for {@link AnalyzedMavenSource} instances.
     */
    public static class AnalyzedMavenSourceBuilder {
        private Path path;
        private Set<ProjectKeeperModule> modules;
        private boolean advertise;
        private String artifactId;
        private String projectName;
        private String version;
        private String releaseArtifactName;
        private String javaVersion = PomFileGenerator.DEFAULT_JAVA_VERSION;
        private DependencyChangeReport dependencyChanges;
        private ProjectDependencies dependencies;
        private boolean isRootProject;

        private AnalyzedMavenSourceBuilder() {
            // empty by intention
        }

        /**
         * @param path source path
         * @return {@code this}.
         */
        public AnalyzedMavenSource.AnalyzedMavenSourceBuilder path(final Path path) {
            this.path = path;
            return this;
        }

        /**
         * @param modules modules
         * @return {@code this}.
         */
        public AnalyzedMavenSource.AnalyzedMavenSourceBuilder modules(final Set<ProjectKeeperModule> modules) {
            this.modules = modules;
            return this;
        }

        /**
         * @param advertise advertise this source or not
         * @return {@code this}.
         */
        public AnalyzedMavenSource.AnalyzedMavenSourceBuilder advertise(final boolean advertise) {
            this.advertise = advertise;
            return this;
        }

        /**
         * @param artifactId Maven artifact ID
         * @return {@code this}.
         */
        public AnalyzedMavenSource.AnalyzedMavenSourceBuilder artifactId(final String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        /**
         * @param projectName project name
         * @return {@code this}.
         */
        public AnalyzedMavenSource.AnalyzedMavenSourceBuilder projectName(final String projectName) {
            this.projectName = projectName;
            return this;
        }

        /**
         * @param version Project version
         * @return {@code this}.
         */
        public AnalyzedMavenSource.AnalyzedMavenSourceBuilder version(final String version) {
            this.version = version;
            return this;
        }

        /**
         * @param releaseArtifactName Artifact file name or {@code null} if no artifact is built
         * @return {@code this}.
         */
        public AnalyzedMavenSource.AnalyzedMavenSourceBuilder releaseArtifactName(final String releaseArtifactName) {
            this.releaseArtifactName = releaseArtifactName;
            return this;
        }

        /**
         * @param javaVersion Java version used for compiling and testing the project. Read from the
         *                    {@code properties/java.version} element of the {@code pom.xml} file. Defaults to
         *                    {@link PomFileGenerator#DEFAULT_JAVA_VERSION}.
         * @return {@code this}.
         */
        public AnalyzedMavenSource.AnalyzedMavenSourceBuilder javaVersion(final String javaVersion) {
            if (javaVersion != null) {
                this.javaVersion = javaVersion;
            }
            return this;
        }

        /**
         * @param dependencyChanges dependency changes
         * @return {@code this}.
         */
        public AnalyzedMavenSource.AnalyzedMavenSourceBuilder dependencyChanges(
                final DependencyChangeReport dependencyChanges) {
            this.dependencyChanges = dependencyChanges;
            return this;
        }

        /**
         * @param dependencies dependencies
         * @return {@code this}.
         */
        public AnalyzedMavenSource.AnalyzedMavenSourceBuilder dependencies(final ProjectDependencies dependencies) {
            this.dependencies = dependencies;
            return this;
        }

        /**
         * @param isRootProject {@code true} if this is the main maven project in the repo (if pom lies directly in
         *                      repo)
         * @return {@code this}.
         */
        public AnalyzedMavenSource.AnalyzedMavenSourceBuilder isRootProject(final boolean isRootProject) {
            this.isRootProject = isRootProject;
            return this;
        }

        /** @return the new instance */
        public AnalyzedMavenSource build() {
            return new AnalyzedMavenSource(this);
        }

        @Override
        public String toString() {
            return "AnalyzedMavenSourceBuilder [path=" + path + ", modules=" + modules + ", advertise=" + advertise
                    + ", artifactId=" + artifactId + ", projectName=" + projectName + ", version=" + version
                    + ", releaseArtifactName=" + releaseArtifactName + ", javaVersion=" + javaVersion
                    + ", dependencyChanges=" + dependencyChanges + ", dependencies=" + dependencies + ", isRootProject="
                    + isRootProject + "]";
        }
    }

    @Override
    public String toString() {
        return "AnalyzedMavenSource [path=" + path + ", modules=" + modules + ", advertise=" + advertise
                + ", artifactId=" + artifactId + ", projectName=" + projectName + ", version=" + version
                + ", releaseArtifactName=" + releaseArtifactName + ", javaVersion=" + javaVersion
                + ", dependencyChanges=" + dependencyChanges + ", dependencies=" + dependencies + ", isRootProject="
                + isRootProject + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, modules, advertise, artifactId, projectName, version, releaseArtifactName,
                javaVersion, dependencyChanges, dependencies, isRootProject);
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
        final AnalyzedMavenSource other = (AnalyzedMavenSource) obj;
        return Objects.equals(path, other.path) && Objects.equals(modules, other.modules)
                && advertise == other.advertise && Objects.equals(artifactId, other.artifactId)
                && Objects.equals(projectName, other.projectName) && Objects.equals(version, other.version)
                && Objects.equals(releaseArtifactName, other.releaseArtifactName)
                && Objects.equals(javaVersion, other.javaVersion)
                && Objects.equals(dependencyChanges, other.dependencyChanges)
                && Objects.equals(dependencies, other.dependencies) && isRootProject == other.isRootProject;
    }
}
