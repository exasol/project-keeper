package com.exasol.projectkeeper.pom;

import java.util.Map;

import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;

/**
 * Applies the legacy Maven 3.9 super-POM plugin versions removed in Maven 3.10.
 *
 * <p>
 * Maven 3.9.11 makes these versions part of a project's effective model. Maven 3.10.0 deliberately no longer
 * does so. Keeping these compatibility defaults makes Project Keeper's dependency reports independent of the Maven
 * version that runs the crawler.
 * </p>
 */
final class MavenPluginVersionCompatibility {
    private static final String MAVEN_PLUGIN_GROUP_ID = "org.apache.maven.plugins";
    private static final Map<String, String> MAVEN_3_9_SUPER_POM_PLUGIN_VERSIONS = Map.of(
            "maven-antrun-plugin", "3.1.0",
            "maven-assembly-plugin", "3.7.1",
            "maven-dependency-plugin", "3.7.0",
            "maven-release-plugin", "3.0.1");

    private MavenPluginVersionCompatibility() {
        // static utility class
    }

    static void apply(final Model model) {
        model.getBuild().getPlugins().forEach(MavenPluginVersionCompatibility::applyTo);
    }

    private static void applyTo(final Plugin plugin) {
        if (plugin.getVersion() == null
                && (plugin.getGroupId() == null || MAVEN_PLUGIN_GROUP_ID.equals(plugin.getGroupId()))) {
            final String version = MAVEN_3_9_SUPER_POM_PLUGIN_VERSIONS.get(plugin.getArtifactId());
            if (version != null) {
                plugin.setVersion(version);
            }
        }
    }
}
