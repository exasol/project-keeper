package com.exasol.projectkeeper.config;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Map;

/**
 * Intermediate class for reading the config. This is used by {@link ProjectKeeperConfigReader}.
 * <p>
 * SnakeYML requires this to be public.
 * </p>
 */
public class ProjectKeeperRawConfig {
    private List<Source> sources;
    private List<String> linkReplacements;
    private List<Object> excludes;
    private Object version;
    private Build build;

    /**
     * Get the sources.
     * 
     * @return sources
     */
    public List<Source> getSources() {
        return sources;
    }

    /**
     * Get link replacements.
     * 
     * @return link replacements
     */
    public List<String> getLinkReplacements() {
        return linkReplacements;
    }

    /**
     * Get the excludes.
     * <p>
     * This is either
     * <ul>
     * <li>a {@link String}, e.g.
     * {@code E-PK-CORE-17: Missing required file: '.github/workflows/project-keeper-verify.yml'}
     * <li>or a {@link Map} with key {@code regex} and a {@link String} value, e.g.
     * {@code W-PK-CORE-151: Pom file .* contains no reference to project-keeper-maven-plugin.}
     * </ul>
     * 
     * @return excludes
     */
    public List<Object> getExcludes() {
        return excludes;
    }

    /**
     * Get version.
     * <p>
     * This is either
     * <ul>
     * <li>a {@link String}, e.g. {@code 1.2.3}
     * <li>or a {@link Map} with key {@code fromSource} and a {@link String} value, e.g. {@code project-keeper/pom.xml}
     * </ul>
     * 
     * @return version
     */
    public Object getVersion() {
        return version;
    }

    /**
     * Set sources.
     * 
     * @param sources sources
     */
    public void setSources(final List<ProjectKeeperRawConfig.Source> sources) {
        this.sources = sources;
    }

    /**
     * Set link replacements.
     * 
     * @param linkReplacements link replacements
     */
    public void setLinkReplacements(final List<String> linkReplacements) {
        this.linkReplacements = linkReplacements;
    }

    /**
     * Set excludes.
     * 
     * @param excludes excludes
     */
    public void setExcludes(final List<Object> excludes) {
        this.excludes = excludes;
    }

    /**
     * Set version.
     * 
     * @param version version
     */
    public void setVersion(final Object version) {
        this.version = version;
    }

    /**
     * Get the CI build configuration.
     * 
     * @return build options
     */
    public Build getBuild() {
        return build;
    }

    /**
     * Set the CI build options.
     * 
     * @param build build configuration
     */
    public void setBuild(final Build build) {
        this.build = build;
    }

    /**
     * Intermediate class for reading the config sources, i.e. modules or sub-projects.
     * <p>
     * SnakeYML requires this class to be public.
     */
    public static class Source {
        private String path;
        private String type;
        private List<String> modules;
        private boolean advertise = true;
        private ProjectKeeperRawConfig.ParentPomRef parentPom;
        private List<String> artifacts;

        /**
         * Get the source path, e.g. {@code project-keeper/pom.xml}.
         * 
         * @return path
         */
        public String getPath() {
            return path;
        }

        /**
         * Get the source type, e.g. {@code maven}.
         * 
         * @return type
         */
        public String getType() {
            return type;
        }

        /**
         * Get the list of modules enabled for the source, e.g. {@code maven_central}, {@code integration_tests} etc.
         * 
         * @return enabled modules
         */
        public List<String> getModules() {
            return modules;
        }

        /**
         * Check if this source is customer visible and should be advertised.
         * 
         * @return {@code true} if the source should be advertised
         */
        public boolean isAdvertised() {
            return advertise;
        }

        /**
         * Get the referenced parent POM for Maven sources or {@code null} if none is used.
         * 
         * @return parent POM
         */
        public ProjectKeeperRawConfig.ParentPomRef getParentPom() {
            return parentPom;
        }

        /**
         * Set the list of release artifact paths, relative to to {@link #getPath()}, example:
         * {@code target/my-artifact.jar}
         * 
         * @return release artifact paths
         */
        public List<String> getArtifacts() {
            return artifacts;
        }

        /**
         * Set the path.
         * 
         * @param path path
         */
        public void setPath(final String path) {
            this.path = path;
        }

        /**
         * Set the type.
         * 
         * @param type type
         */
        public void setType(final String type) {
            this.type = type;
        }

        /**
         * Set the modules.
         * 
         * @param modules modules
         */
        public void setModules(final List<String> modules) {
            this.modules = modules;
        }

        /**
         * Set the parent POM.
         * 
         * @param parentPom parent POM
         */
        public void setParentPom(final ProjectKeeperRawConfig.ParentPomRef parentPom) {
            this.parentPom = parentPom;
        }

        /**
         * Set the advertise flag.
         * 
         * @param advertise advertise
         */
        public void setAdvertise(final boolean advertise) {
            this.advertise = advertise;
        }

        /**
         * Set the list of release artifact paths, relative to to {@link #getPath()}, example:
         * {@code target/my-artifact.jar}
         * 
         * @param artifacts release artifact paths
         */
        public void setArtifacts(final List<String> artifacts) {
            this.artifacts = artifacts;
        }
    }

    /**
     * Reference to a parent pom of a Maven source.
     * <p>
     * SnakeYML requires this class to be public.
     * </p>
     */
    public static class ParentPomRef {
        private String groupId;
        private String artifactId;
        private String version;
        private String relativePath;

        /**
         * Get the group ID of the parent POM.
         * 
         * @return group ID
         */
        public String getGroupId() {
            return groupId;
        }

        /**
         * Get the artifact ID of the parent POM.
         * 
         * @return artifact ID
         */
        public String getArtifactId() {
            return artifactId;
        }

        /**
         * Get the version of the parent POM.
         * 
         * @return version
         */
        public String getVersion() {
            return version;
        }

        /**
         * Get the relative path to the parent POM, e.g. {@code ../parent-pom/pom.xml}
         * 
         * @return relative path
         */
        public String getRelativePath() {
            return relativePath;
        }

        /**
         * Set the group ID.
         * 
         * @param groupId group ID
         */
        public void setGroupId(final String groupId) {
            this.groupId = groupId;
        }

        /**
         * Set the artifact ID.
         * 
         * @param artifactId artifact ID
         */
        public void setArtifactId(final String artifactId) {
            this.artifactId = artifactId;
        }

        /**
         * Set the version.
         * 
         * @param version version
         */
        public void setVersion(final String version) {
            this.version = version;
        }

        /**
         * Set the relative path.
         * 
         * @param relativePath relative path.
         */
        public void setRelativePath(final String relativePath) {
            this.relativePath = relativePath;
        }
    }

    /**
     * Intermediate class for de-serializing build options from PK's YAML configuration file
     * {@link ProjectKeeperConfigReader#CONFIG_FILE_NAME}.
     * <p>
     * SnakeYML requires this class to be public.
     */
    public static class Build {
        private String runnerOs;
        private boolean freeDiskSpace = false;
        private List<String> exasolDbVersions = emptyList();
        private List<Map<String, Object>> setupSteps = emptyList();
        private Map<String, Object> buildStep = null;
        private List<Map<String, Object>> cleanupSteps = emptyList();

        /**
         * Get CI build runner operating system, e.g. {@code ubuntu-20.04}.
         * 
         * @return CI build runner operating system
         */
        public String getRunnerOs() {
            return runnerOs;
        }

        /**
         * Set CI build runner operating system, e.g. {@code ubuntu-20.04}. Default: {@code ubuntu-latest}.
         * 
         * @param runnerOs CI build runner operating system
         */
        public void setRunnerOs(final String runnerOs) {
            this.runnerOs = runnerOs;
        }

        /** @return {@code true} if the CI build should free disk space before running the build */
        public boolean shouldFreeDiskSpace() {
            return freeDiskSpace;
        }

        /** @param freeDiskSpace {@code true} if the CI build should free disk space before running the build */
        public void setFreeDiskSpace(final boolean freeDiskSpace) {
            this.freeDiskSpace = freeDiskSpace;
        }

        /** @return Exasol DB versions for which to run the CI build */
        public List<String> getExasolDbVersions() {
            return exasolDbVersions;
        }

        /** @param exasolDbVersions Exasol DB versions for which to run the CI build */
        public void setExasolDbVersions(final List<String> exasolDbVersions) {
            this.exasolDbVersions = exasolDbVersions;
        }

        /** @return list of setup steps */
        public List<Map<String, Object>> getSetupSteps() {
            if (setupSteps == null) {
                return emptyList();
            }
            return setupSteps;
        }

        /** @param setupSteps list of setup steps */
        public void setSetupSteps(final List<Map<String, Object>> setupSteps) {
            this.setupSteps = setupSteps;
        }

        /** @return build step */
        public Map<String, Object> getBuildStep() {
            return buildStep;
        }

        /** @param buildStep build step */
        public void setBuildStep(final Map<String, Object> buildStep) {
            this.buildStep = buildStep;
        }

        /** @return list of cleanup steps */
        public List<Map<String, Object>> getCleanupSteps() {
            if (cleanupSteps == null) {
                return emptyList();
            }
            return cleanupSteps;
        }

        /** @param cleanupSteps list of cleanup steps */
        public void setCleanupSteps(final List<Map<String, Object>> cleanupSteps) {
            this.cleanupSteps = cleanupSteps;
        }
    }
}
