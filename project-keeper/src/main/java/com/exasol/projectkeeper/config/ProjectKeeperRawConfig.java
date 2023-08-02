package com.exasol.projectkeeper.config;

import java.util.List;
import java.util.Map;

/**
 * Intermediate class for reading the config. This is used by {@link ProjectKeeperConfigReader}.
 * <p>
 * SnakeYML requires this to be public.
 * </p>
 */
public class ProjectKeeperRawConfig {
    private List<ProjectKeeperRawConfig.Source> sources;
    private List<String> linkReplacements;
    private List<Object> excludes;
    private Object version;

    /**
     * Get the sources.
     * 
     * @return sources
     */
    public List<ProjectKeeperRawConfig.Source> getSources() {
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
     * Set link replacements
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
        public boolean isAdvertise() {
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
    }

    /**
     * Reference to a parent pom of a maven source.
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
}