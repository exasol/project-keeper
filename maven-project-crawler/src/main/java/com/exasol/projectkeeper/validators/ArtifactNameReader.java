package com.exasol.projectkeeper.validators;

import java.util.Optional;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * This class reads the file name of the release artifact produced by this pom.
 */
public class ArtifactNameReader {

    private static final String ASSEMBLY_PLUGIN_QUALIFIED_NAME = "org.apache.maven.plugins:maven-assembly-plugin";
    private final MavenProject project;

    /**
     * Create a new instance.
     * 
     * @param project the Maven project
     */
    public ArtifactNameReader(final MavenProject project) {
        this.project = project;
    }

    /**
     * Get the file name of the release artifact relative to the {@code target} directory.
     * <p>
     * The name consists of the {@code finalName} configured for the {@code maven-assembly-plugin} plus the {@code jar}
     * suffix. Placeholders like <code>${project.version}</code> are already replaced with their actual values.
     * 
     * @return file name or {@code null} if the assembly plugin is not configured.
     */
    public String readFinalArtifactName() {
        return Optional.ofNullable(project.getBuild().getPluginsAsMap().get(ASSEMBLY_PLUGIN_QUALIFIED_NAME))
                .map(Plugin::getConfiguration) //
                .map(Xpp3Dom.class::cast) //
                .map(config -> config.getChild("finalName")) //
                .map(Xpp3Dom::getValue) //
                .map(artifactName -> artifactName + ".jar") //
                .orElse(null);
    }
}
