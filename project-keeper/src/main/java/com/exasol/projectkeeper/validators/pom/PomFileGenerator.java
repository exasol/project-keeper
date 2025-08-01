package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperModule.*;
import static com.exasol.projectkeeper.validators.pom.builder.NodeBuilder.*;
import static com.exasol.projectkeeper.validators.pom.io.PomFileReader.trimWhitespace;

import java.util.*;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.RepoInfo;
import com.exasol.projectkeeper.shared.config.ParentPomRef;
import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.validators.pom.builder.*;
import com.exasol.projectkeeper.validators.pom.io.PomFileWriter;
import com.exasol.projectkeeper.validators.pom.plugin.*;

/**
 * This class generates the expected content for the auto-generated parent pom file {@code pk_generated_parent.pom}.
 */
// [impl -> dsn~mvn-toolchain~1]
public class PomFileGenerator {
    PomFileGenerator() {
    }

    /** Default Java version if none is specified in {@code pom.xml} property {@code java.version}. */
    public static final String DEFAULT_JAVA_VERSION = "11";
    private static final List<PluginTemplateGenerator> PLUGIN_GENERATORS = List.of(
            // Pin version for the following plugins
            new SimplePluginTemplateGenerator("maven_templates/maven-clean-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/maven-install-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/maven-resources-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/maven-site-plugin.xml", DEFAULT),
            // Plugins with configuration
            new SimplePluginTemplateGenerator("maven_templates/sonar-maven-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/maven-toolchains-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/maven-compiler-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/maven-enforcer-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/flatten-maven-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/ossindex-maven-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/maven-surefire-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/versions-maven-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/duplicate-finder-maven-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/maven-artifact-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/maven-assembly-plugin.xml", JAR_ARTIFACT),
            new SimplePluginTemplateGenerator("maven_templates/maven-jar-plugin-exclusion.xml", JAR_ARTIFACT),
            new SimplePluginTemplateGenerator("maven_templates/artifact-reference-checker-maven-plugin.xml",
                    JAR_ARTIFACT),
            new SimplePluginTemplateGenerator("maven_templates/maven-deploy-plugin.xml", MAVEN_CENTRAL),
            new SimplePluginTemplateGenerator("maven_templates/maven-gpg-plugin.xml", MAVEN_CENTRAL),
            new SimplePluginTemplateGenerator("maven_templates/maven-source-plugin.xml", MAVEN_CENTRAL),
            new SimplePluginTemplateGenerator("maven_templates/maven-javadoc-plugin.xml", MAVEN_CENTRAL),
            new SimplePluginTemplateGenerator("maven_templates/central-publishing-maven-plugin.xml", MAVEN_CENTRAL),
            new SimplePluginTemplateGenerator("maven_templates/maven-dependency-plugin.xml", UDF_COVERAGE),
            new SimplePluginTemplateGenerator("maven_templates/native-image-maven-plugin.xml", NATIVE_IMAGE),
            new SimplePluginTemplateGenerator("maven_templates/lombok-maven-plugin.xml", LOMBOK),
            new FailsafePluginTemplateGenerator(), new JacocoPluginTemplateGenerator(),
            // quality-summarizer-maven-plugin must come after Jacoco
            new SimplePluginTemplateGenerator("maven_templates/quality-summarizer-maven-plugin.xml", DEFAULT),
            new ErrorCodeCrawlerPluginTemplateGenerator(),
            new SimplePluginTemplateGenerator("maven_templates/git-commit-id-maven-plugin.xml", DEFAULT));
    private static final String VERSION = "version";
    private static final String ARTIFACT_ID = "artifactId";
    private static final String GROUP_ID = "groupId";

    /**
     * Generate the content of the pom file.
     *
     * @param enabledModules list of enabled modules
     * @param groupId        group id for the generated pom file
     * @param artifactId     artifact id for the generated pom file
     * @param version        version for the generated pom file
     * @param parentPomRef   reference to a parent pom or {@code null}
     * @param repoInfo       information about the repository
     * @return pom file content
     */
    public String generatePomContent(final Collection<ProjectKeeperModule> enabledModules, final String groupId,
            final String artifactId, final String version, final ParentPomRef parentPomRef, final RepoInfo repoInfo) {
        return generatePomContent(new Config(enabledModules, groupId, artifactId, version, parentPomRef, repoInfo));
    }

    /**
     * Generate the content of the pom file.
     *
     * @param config config object
     * @return pom file content
     */
    private String generatePomContent(final Config config) {
        final DocumentBuilder builder = document() //
                .child(comment("@formatter:off")) //
                .child(comment("This file is auto-generated by project-keeper. All changes will be overwritten.")) //
                .child(projectBuilder(config));
        return PomFileWriter.writeString(builder.build());
    }

    private NodeBuilder projectBuilder(final Config config) {
        final ParentPomRef parentPomRef = config.getParentPomRef();
        final Collection<ProjectKeeperModule> modules = config.getEnabledModules();
        return element("http://maven.apache.org/POM/4.0.0", "project") //
                .attribute("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation",
                        "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd") //
                .child("modelVersion", "4.0.0") //
                .child(GROUP_ID, config.getGroupId()) //
                .child(ARTIFACT_ID, config.getArtifactId()) //
                .child(VERSION, config.getVersion()) //
                .child("packaging", "pom") //
                .nullableChild(parentReference(parentPomRef)) //
                .child(properties(modules, config.getRepoInfo().getRepoName(), parentPomRef != null)) //
                .nullableChild(profiles(modules)) //
                .child(licenses(config)) //
                .child(developers()) //
                .child(scm(config.getRepoInfo().getRepoName())) //
                .child(dependencies(modules)) //
                .child(build(modules));
    }

    private ElementBuilder profiles(final Collection<ProjectKeeperModule> modules) {
        if (!modules.contains(NATIVE_IMAGE)) {
            return null;
        }
        return element("profiles") //
                .child(profile("default", true, Map.of("native-image.skip", "false"))) //
                .child(profile("skipNativeImage", false,
                        Map.of("native-image.skip", "true", "test.excludeTags", "native-image")));
    }

    private ElementBuilder profile(final String id, final boolean isDefault, final Map<String, String> properties) {
        final List<NodeBuilder> propertyBuilders = properties.entrySet().stream() //
                .sorted(Map.Entry.comparingByKey()) //
                .map(e -> element(e.getKey()).child(textNode(e.getValue()))) //
                .map(NodeBuilder.class::cast)
                .toList();
        return element("profile") //
                .child("id", id) //
                .nullableChild(isDefault ? element("activation").child("activeByDefault", "true") : null)
                .child(element("properties").children(propertyBuilders));
    }

    private ElementBuilder licenses(final Config config) {
        final String url = "https://github.com/exasol/" + config.getRepoInfo().getRepoName() + "/blob/main/LICENSE";
        return element("licenses").child( //
                element("license") //
                        .child("name", config.getRepoInfo().getLicenseName()) //
                        .child("url", url) //
                        .child("distribution", "repo"));
    }

    private ElementBuilder scm(final String repoName) {
        final String gitUrl = "scm:git:https://github.com/exasol/" + repoName + ".git";
        return element("scm") //
                .child("connection", gitUrl) //
                .child("developerConnection", gitUrl) //
                .child("url", "https://github.com/exasol/" + repoName + "/");
    }

    private ElementBuilder developers() {
        return element("developers") //
                .child(element("developer") //
                        .child("name", "Exasol") //
                        .child("email", "opensource@exasol.com") //
                        .child("organization", "Exasol AG") //
                        .child("organizationUrl", "https://www.exasol.com/"));
    }

    private ElementBuilder parentReference(final ParentPomRef parentPomRef) {
        if (parentPomRef == null) {
            return null;
        }
        final String path = parentPomRef.getRelativePath();
        return element("parent") //
                .child(GROUP_ID, parentPomRef.getGroupId()) //
                .child(ARTIFACT_ID, parentPomRef.getArtifactId()) //
                .child(VERSION, parentPomRef.getVersion()) //
                .nullableChild(path == null ? null : element("relativePath").text(path));
    }

    private ElementBuilder dependencies(final Collection<ProjectKeeperModule> enabledModules) {
        return element("dependencies") //
                .nullableChild(enabledModules.contains(LOMBOK) //
                        ? dependency("org.projectlombok", "lombok", "1.18.38", "provided", null)
                        : null) //
                .nullableChild(enabledModules.contains(UDF_COVERAGE) //
                        ? dependency("org.jacoco", "org.jacoco.agent", "0.8.13", "test", "runtime")
                        : null);
    }

    private ElementBuilder dependency(final String groupId, final String artifactId, final String version,
            final String scope, final String classifier) {
        return element("dependency") //
                .child(GROUP_ID, groupId) //
                .child(ARTIFACT_ID, artifactId) //
                .child(VERSION, version) //
                .child("scope", scope) //
                .nullableChild(classifier == null ? null : element("classifier").text(classifier));
    }

    private ElementBuilder build(final Collection<ProjectKeeperModule> enabledModules) {
        return element("build").child(plugins(enabledModules));
    }

    private ElementBuilder properties(final Collection<ProjectKeeperModule> enabledModules, final String repoName,
            final boolean hasParentPom) {
        final String javaVersion = hasParentPom ? null : DEFAULT_JAVA_VERSION;
        final ElementBuilder properties = element("properties")
                .child("project.build.sourceEncoding", "UTF-8")
                .child("project.reporting.outputEncoding", "UTF-8")
                .child("project.build.outputTimestamp", "${git.commit.time}")
                .nullableChild(javaVersion == null ? null : element("java.version").text(javaVersion))
                .child("sonar.organization", "exasol")
                .child("sonar.host.url", "https://sonarcloud.io")
                .nullableChild(hasParentPom ? null : element("test.excludeTags").text(""));
        if (enabledModules.contains(MAVEN_CENTRAL)) {
            properties
                    .child(element("gpg.skip").text("true"))
                    .child(element("central-publishing.autoPublish").text("false"))
                    .child(element("central-publishing.skipPublishing").text("false"))
                    // Other options for waitUntil: published, uploaded
                    .child(element("central-publishing.waitUntil").text("validated"))
                    .child(element("central-publishing.deploymentName")
                            .text("Manual deployment of repo %s".formatted(repoName)));
        }
        return properties;
    }

    private ElementBuilder plugins(final Collection<ProjectKeeperModule> enabledModules) {
        final ElementBuilder builder = element("plugins");
        for (final PluginTemplateGenerator pluginGenerator : PLUGIN_GENERATORS) {
            final Optional<Node> pluginTemplate = pluginGenerator.generateTemplate(enabledModules);
            pluginTemplate.ifPresent(template -> {
                trimWhitespace(template);
                builder.child(plainNode(template));
            });
        }
        return builder;
    }

    private static class Config {
        private final Collection<ProjectKeeperModule> enabledModules;
        private final String groupId;
        private final String artifactId;
        private final String version;
        private final ParentPomRef parentPomRef;
        private final RepoInfo repoInfo;

        private Config(final Collection<ProjectKeeperModule> enabledModules, final String groupId,
                final String artifactId, final String version, final ParentPomRef parentPomRef,
                final RepoInfo repoInfo) {
            this.enabledModules = enabledModules;
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
            this.parentPomRef = parentPomRef;
            this.repoInfo = repoInfo;
        }

        private Collection<ProjectKeeperModule> getEnabledModules() {
            return enabledModules;
        }

        private String getGroupId() {
            return groupId;
        }

        private String getArtifactId() {
            return artifactId;
        }

        private String getVersion() {
            return version;
        }

        private ParentPomRef getParentPomRef() {
            return parentPomRef;
        }

        private RepoInfo getRepoInfo() {
            return repoInfo;
        }
    }
}
