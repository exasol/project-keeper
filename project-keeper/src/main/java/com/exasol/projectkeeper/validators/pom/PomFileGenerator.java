package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.ProjectKeeperModule.*;
import static com.exasol.projectkeeper.validators.pom.XmlHelper.addTextElement;

import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.validators.pom.plugin.*;

/**
 * This class generates the expected content for the auto-generated parent pom file.
 */
public class PomFileGenerator {
    private static final List<PluginTemplateGenerator> PLUGIN_GENERATORS = List.of(
            new SimplePluginTemplateGenerator("maven_templates/maven-enforcer-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/flatten-maven-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/ossindex-maven-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/reproducible-build-maven-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/maven-surefire-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/versions-maven-plugin.xml", DEFAULT),
            new SimplePluginTemplateGenerator("maven_templates/maven-assembly-plugin.xml", JAR_ARTIFACT),
            new SimplePluginTemplateGenerator("maven_templates/maven-jar-plugin-exclusion.xml", JAR_ARTIFACT),
            new SimplePluginTemplateGenerator("maven_templates/artifact-reference-checker-maven-plugin.xml",
                    JAR_ARTIFACT),
            new SimplePluginTemplateGenerator("maven_templates/maven-deploy-plugin.xml", MAVEN_CENTRAL),
            new SimplePluginTemplateGenerator("maven_templates/maven-gpg-plugin.xml", MAVEN_CENTRAL),
            new SimplePluginTemplateGenerator("maven_templates/maven-source-plugin.xml", MAVEN_CENTRAL),
            new SimplePluginTemplateGenerator("maven_templates/maven-javadoc-plugin.xml", MAVEN_CENTRAL),
            new SimplePluginTemplateGenerator("maven_templates/nexus-staging-maven-plugin.xml", MAVEN_CENTRAL),
            new SimplePluginTemplateGenerator("maven_templates/maven-dependency-plugin.xml", UDF_COVERAGE),
            new SimplePluginTemplateGenerator("maven_templates/lombok-maven-plugin.xml", LOMBOK),
            new FailsafePluginTemplateGenerator(), new JacocoPluginTemplateGenerator(),
            new ErrorCodeCrawlerPluginTemplateGenerator());
    private static final String VERSION = "version";
    private static final String ARTIFACT_ID = "artifactId";
    private static final String GROUP_ID = "groupId";

    /*
     * Inspired by https://stackoverflow.com/a/33564346
     */
    private static void trimWhitespace(final Node node) {
        final NodeList children = node.getChildNodes();
        for (int index = 0; index < children.getLength(); ++index) {
            final Node child = children.item(index);
            if (child.getNodeType() == Node.TEXT_NODE) {
                child.setTextContent(child.getTextContent().trim());
            }
            trimWhitespace(child);
        }
    }

    /**
     * Generate the content of the pom file.
     *
     * @param enabledModules list of enabled modules
     * @param groupId        group id for the generated pom file
     * @param artifactId     artifact id for the generated pom file
     * @param version        version for the generated pom file
     * @param parentPomRef   reference to a parent pom or {@code null}
     * @return pom file content
     */
    public String generatePomContent(final Collection<ProjectKeeperModule> enabledModules, final String groupId,
            final String artifactId, final String version, final ProjectKeeperConfig.ParentPomRef parentPomRef) {
        final Document document = createDocument();
        document.appendChild(document
                .createComment("This file is auto-generated by project-keeper. All changes will be overwritten."));
        final Element project = buildProject(enabledModules, groupId, artifactId, version, document, parentPomRef);
        document.appendChild(project);
        return new PomFileIO().writePomFileToString(document);
    }

    private Element buildProject(final Collection<ProjectKeeperModule> enabledModules, final String groupId,
            final String artifactId, final String version, final Document document,
            final ProjectKeeperConfig.ParentPomRef parentPomRef) {
        final Element project = createProject(document);
        addTextElement(project, "modelVersion", "4.0.0");
        addTextElement(project, GROUP_ID, groupId);
        addTextElement(project, ARTIFACT_ID, artifactId);
        addTextElement(project, VERSION, version);
        addTextElement(project, "packaging", "pom");
        if (parentPomRef != null) {
            project.appendChild(buildParent(document, parentPomRef));
        }
        project.appendChild(buildProperties(enabledModules, document));

        project.appendChild(buildDependencies(enabledModules, document));
        project.appendChild(buildBuild(enabledModules, document));
        return project;
    }

    private Element buildParent(final Document document, final ProjectKeeperConfig.ParentPomRef parentPomRef) {
        final Element parent = document.createElement("parent");
        addTextElement(parent, GROUP_ID, parentPomRef.getGroupId());
        addTextElement(parent, ARTIFACT_ID, parentPomRef.getArtifactId());
        addTextElement(parent, VERSION, parentPomRef.getVersion());
        if (parentPomRef.getRelativePath() != null) {
            addTextElement(parent, "relativePath", parentPomRef.getRelativePath());
        }
        return parent;
    }

    private Element buildDependencies(final Collection<ProjectKeeperModule> enabledModules, final Document document) {
        final Element dependencies = document.createElement("dependencies");
        if (enabledModules.contains(LOMBOK)) {
            addDependency(dependencies, "org.projectlombok", "lombok", "1.18.20", "provided");
        }
        if (enabledModules.contains(UDF_COVERAGE)) {
            addDependency(dependencies, "org.jacoco", "org.jacoco.agent", "0.8.5", "test", "runtime");
        }
        return dependencies;
    }

    private void addDependency(final Element dependencies, final String groupId, final String artifactId,
            final String version, final String scope) {
        addDependency(dependencies, groupId, artifactId, version, scope, null);
    }

    private void addDependency(final Element dependencies, final String groupId, final String artifactId,
            final String version, final String scope, final String classifier) {
        final Element dependency = dependencies.getOwnerDocument().createElement("dependency");
        addTextElement(dependency, GROUP_ID, groupId);
        addTextElement(dependency, ARTIFACT_ID, artifactId);
        addTextElement(dependency, VERSION, version);
        addTextElement(dependency, "scope", scope);
        if (classifier != null) {
            addTextElement(dependency, "classifier", classifier);
        }
        dependencies.appendChild(dependency);
    }

    private Element buildBuild(final Collection<ProjectKeeperModule> enabledModules, final Document document) {
        final Element plugins = buildPlugins(enabledModules, document);
        final Element build = document.createElement("build");
        build.appendChild(plugins);
        return build;
    }

    private Element createProject(final Document document) {
        final Element project = document.createElementNS("http://maven.apache.org/POM/4.0.0", "project");
        project.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation",
                "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd");
        return project;
    }

    private Element buildProperties(final Collection<ProjectKeeperModule> enabledModules, final Document document) {
        final Element properties = document.createElement("properties");
        addTextElement(properties, "project.build.sourceEncoding", "UTF-8");
        addTextElement(properties, "project.reporting.outputEncoding", "UTF-8");
        addTextElement(properties, "java.version", "11");
        if (enabledModules.contains(MAVEN_CENTRAL)) {
            addTextElement(properties, "gpg.skip", "true");
        }
        return properties;
    }

    private Document createDocument() {
        try {
            final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            document.setXmlVersion("1.0");
            document.setXmlStandalone(false);
            return document;
        } catch (final ParserConfigurationException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-109")
                    .message("Failed to create document.").ticketMitigation().toString(), exception);
        }
    }

    private Element buildPlugins(final Collection<ProjectKeeperModule> enabledModules, final Document document) {
        final Element plugins = document.createElement("plugins");
        for (final PluginTemplateGenerator pluginGenerator : PLUGIN_GENERATORS) {
            final Optional<Node> pluginTemplate = pluginGenerator.generateTemplate(enabledModules);
            pluginTemplate.ifPresent(template -> {
                trimWhitespace(template);
                plugins.appendChild(document.adoptNode(template));
            });
        }
        return plugins;
    }
}