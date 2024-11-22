package com.exasol.projectkeeper.sources;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.shared.config.Source;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.sources.analyze.generic.RepoNameReader;

/**
 * {@link AnalyzedSource} for non-maven source.
 * <p>
 * Use {@link AnalyzedSourceImpl#builder()} to create a new instance.
 */
public final class AnalyzedSourceImpl implements AnalyzedSource {

    /**
     * Get the project name.
     * 
     * @param projectDir root folder of the project
     * @param source     source of main or sub module within the current project
     * @return name of the repository hosting the current project.
     */
    public static String projectName(final Path projectDir, final Source source) {
        if (source.isRoot()) {
            return RepoNameReader.getRepoName(projectDir);
        } else {
            return RepoNameReader.getRepoName(source.getPath().getParent());
        }
    }

    private final Path path;
    private final Set<ProjectKeeperModule> modules;
    private final boolean advertise;
    private final String moduleName;
    private final String projectName;
    private final String version;
    private final DependencyChangeReport dependencyChanges;
    private final ProjectDependencies dependencies;
    private final boolean isRootProject;

    private AnalyzedSourceImpl(final AnalyzedSourceImplBuilder builder) {
        this.path = builder.path;
        this.modules = builder.modules;
        this.advertise = builder.advertise;
        this.moduleName = builder.moduleName;
        this.projectName = builder.projectName;
        this.version = builder.version;
        this.dependencyChanges = builder.dependencyChanges;
        this.dependencies = builder.dependencies;
        this.isRootProject = builder.isRootProject;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public Set<ProjectKeeperModule> getModules() {
        return modules;
    }

    @Override
    public boolean isAdvertised() {
        return advertise;
    }

    /**
     * Get the module name.
     * 
     * @return module name
     */
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public String getProjectName() {
        return projectName;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public DependencyChangeReport getDependencyChanges() {
        return dependencyChanges;
    }

    @Override
    public ProjectDependencies getDependencies() {
        return dependencies;
    }

    /**
     * Check if this is the root project.
     * 
     * @return {@code true} if this is the main project in the repo, i.e. if build script (e.g. go.mod) lies directly in
     *         repo
     */
    public boolean isRootProject() {
        return isRootProject;
    }

    /**
     * Create a new builder.
     * 
     * @return a new builder for {@link AnalyzedSourceImpl}
     */
    public static AnalyzedSourceImpl.AnalyzedSourceImplBuilder builder() {
        return new AnalyzedSourceImpl.AnalyzedSourceImplBuilder();
    }

    /**
     * Builder for {@link AnalyzedSourceImpl}.
     */
    public static class AnalyzedSourceImplBuilder {
        private Path path;
        private Set<ProjectKeeperModule> modules;
        private boolean advertise;
        private String moduleName;
        private String projectName;
        private String version;
        private DependencyChangeReport dependencyChanges;
        private ProjectDependencies dependencies;
        private boolean isRootProject;

        private AnalyzedSourceImplBuilder() {
            // empty by intention
        }

        /**
         * Set source path.
         * 
         * @param path source path
         * @return {@code this}.
         */
        public AnalyzedSourceImpl.AnalyzedSourceImplBuilder path(final Path path) {
            this.path = path;
            return this;
        }

        /**
         * Set PK modules.
         * 
         * @param modules enabled modules
         * @return {@code this}.
         */
        public AnalyzedSourceImpl.AnalyzedSourceImplBuilder modules(final Set<ProjectKeeperModule> modules) {
            this.modules = modules;
            return this;
        }

        /**
         * Set if this should be advertised.
         * 
         * @param advertise {@code true} if this source should be advertised
         * @return {@code this}.
         */
        public AnalyzedSourceImpl.AnalyzedSourceImplBuilder advertise(final boolean advertise) {
            this.advertise = advertise;
            return this;
        }

        /**
         * Set module name.
         * 
         * @param moduleName module name
         * @return {@code this}.
         */
        public AnalyzedSourceImpl.AnalyzedSourceImplBuilder moduleName(final String moduleName) {
            this.moduleName = moduleName;
            return this;
        }

        /**
         * Set project name.
         * 
         * @param projectName project name
         * @return {@code this}.
         */
        public AnalyzedSourceImpl.AnalyzedSourceImplBuilder projectName(final String projectName) {
            this.projectName = projectName;
            return this;
        }

        /**
         * Set project version.
         * 
         * @param version version
         * @return {@code this}.
         */
        public AnalyzedSourceImpl.AnalyzedSourceImplBuilder version(final String version) {
            this.version = version;
            return this;
        }

        /**
         * Set dependency changes.
         * 
         * @param dependencyChanges dependency changes
         * @return {@code this}.
         */
        public AnalyzedSourceImpl.AnalyzedSourceImplBuilder dependencyChanges(
                final DependencyChangeReport dependencyChanges) {
            this.dependencyChanges = dependencyChanges;
            return this;
        }

        /**
         * Set current dependencies.
         * 
         * @param dependencies current dependencies
         * @return {@code this}.
         */
        public AnalyzedSourceImpl.AnalyzedSourceImplBuilder dependencies(final ProjectDependencies dependencies) {
            this.dependencies = dependencies;
            return this;
        }

        /**
         * Se tif this is the root project.
         * 
         * @param isRootProject {@code true} if this is the main project in the repo, i.e. if build script (e.g. go.mod)
         *                      lies directly in repo
         * @return {@code this}.
         */
        public AnalyzedSourceImpl.AnalyzedSourceImplBuilder isRootProject(final boolean isRootProject) {
            this.isRootProject = isRootProject;
            return this;
        }

        /**
         * Build a new instance.
         * 
         * @return a new instance
         */
        public AnalyzedSourceImpl build() {
            return new AnalyzedSourceImpl(this);
        }
    }

    @Override
    public String toString() {
        return "AnalyzedSourceImpl [path=" + path + ", modules=" + modules + ", advertise=" + advertise
                + ", moduleName=" + moduleName + ", projectName=" + projectName + ", version=" + version
                + ", dependencyChanges=" + dependencyChanges + ", dependencies=" + dependencies + ", isRootProject="
                + isRootProject + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, modules, advertise, moduleName, projectName, version, dependencyChanges, dependencies,
                isRootProject);
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
        final AnalyzedSourceImpl other = (AnalyzedSourceImpl) obj;
        return Objects.equals(path, other.path) && Objects.equals(modules, other.modules)
                && advertise == other.advertise && Objects.equals(moduleName, other.moduleName)
                && Objects.equals(projectName, other.projectName) && Objects.equals(version, other.version)
                && Objects.equals(dependencyChanges, other.dependencyChanges)
                && Objects.equals(dependencies, other.dependencies) && isRootProject == other.isRootProject;
    }
}
