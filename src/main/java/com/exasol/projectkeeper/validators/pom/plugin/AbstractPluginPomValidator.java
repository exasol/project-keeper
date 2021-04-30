package com.exasol.projectkeeper.validators.pom.plugin;

import static com.exasol.xpath.XPathErrorHandlingWrapper.runXPath;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.maven.plugin.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.validators.pom.*;
import com.exasol.xpath.XPathSplitter;

/**
 * Abstract basis for maven plugin configuration validation.
 * <p>
 * Create a new plugin validation by adding a template file to {@code src/main/resources/maven_templates/}. Next create
 * an instance of this class and pass the template's name to the super constructor. If you want to enforce more than
 * only the existence of the plugin definition, override
 * {@link #validatePluginConfiguration(Node, Collection, Consumer)}. Finally add your class to
 * {@link PomFileValidator#ALL_VALIDATORS}.
 * </p>
 */
public abstract class AbstractPluginPomValidator extends AbstractPomValidator implements PomValidator {
    public static final String GROUP_ID_XPATH = "groupId";
    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
    @SuppressWarnings("java:S1075") // not an customizable url
    private static final String PLUGINS_XPATH = "/project/build/plugins";
    private final String pluginArtifactId;
    private final String pluginGroupId;
    private final Node template;
    private final String pluginXPath;

    AbstractPluginPomValidator(final String templateResourceName) {
        this.template = readPluginTemplate(templateResourceName);
        this.pluginArtifactId = runXPath(this.template, "artifactId/text()").getNodeValue();
        this.pluginGroupId = runXPath(this.template, GROUP_ID_XPATH + "/text()").getNodeValue();
        this.pluginXPath = PLUGINS_XPATH + "/plugin[artifactId[text()='" + this.pluginArtifactId + "']]";
    }

    @Override
    public void validate(final Document pom, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        if (validatePluginExists(pom, enabledModules, findingConsumer)) {
            final Node plugin = runXPath(pom, this.pluginXPath);
            verifyPluginPropertyHasExactValue(plugin, GROUP_ID_XPATH, findingConsumer);
            validatePluginConfiguration(plugin, enabledModules, findingConsumer);
        }
    }

    private boolean validatePluginExists(final Document pom, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        if (runXPath(pom, this.pluginXPath) == null) {
            findingConsumer.accept(ValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-15")
                            .message("Missing maven plugin {{plugin group|uq}}:{{plugin name|uq}}.", this.pluginGroupId,
                                    this.pluginArtifactId)
                            .toString())
                    .andFix(getFixForMissingPlugin(pom, enabledModules)).build());
            return false;
        } else {
            return true;
        }
    }

    private ValidationFinding.Fix getFixForMissingPlugin(final Document pom,
            final Collection<ProjectKeeperModule> enabledModules) {
        return (Log log) -> {
            createObjectPathIfNotExists(runXPath(pom, "/project"), List.of("build", "plugins"));
            final var plugin = pom.importNode(getPluginTemplate(), true);
            validatePluginConfiguration(plugin, enabledModules, finding -> finding.getFix().fixError(log));
            runXPath(pom, PLUGINS_XPATH).appendChild(plugin);
        };
    }

    /**
     * Get the XML template for this plugin.
     *
     * @return XML template for this plugin
     */
    protected final Node getPluginTemplate() {
        return this.template;
    }

    private Node readPluginTemplate(final String templateResourceName) {
        try (final var templateInputStream = getClass().getClassLoader().getResourceAsStream(templateResourceName)) {
            if (templateInputStream == null) {
                throw new IllegalStateException(ExaError.messageBuilder("F-PK-11")
                        .message("Failed to open {{plugin name|uq}}'s template.", this.pluginArtifactId).toString());
            }
            DOCUMENT_BUILDER_FACTORY.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            DOCUMENT_BUILDER_FACTORY.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            final var documentBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
            return documentBuilder.parse(templateInputStream).getFirstChild();
        } catch (final IOException | SAXException | ParserConfigurationException e) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-10")
                    .message("Failed to parse {{plugin name|uq}}'s template.", this.pluginArtifactId).toString());
        }
    }

    /**
     * Hook for validating the plugin specific parts.
     * <p>
     * Hint: use {@link #verifyPluginHasProperty(Node, String, Consumer)} or
     * {@link #verifyPluginPropertyHasExactValue(Node, String, Consumer)} in this method's implementation.
     * </p>
     *
     * @param plugin          the plugin to validate
     * @param enabledModules  list of enabled modules
     * @param findingConsumer to report the validation findings to
     */
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
    }

    /**
     * Helper function for validating that an element exists. In FIX mode, this method creates the missing element with
     * the content from the template.
     * <p>
     * This method does not validate, that the element has the correct content. For that purpose use
     * {@link #verifyPluginPropertyHasExactValue(Node, String, Consumer)}.
     * </p>
     *
     * @param plugin          the plugin to validate
     * @param xPath           path of the property to validate / fix. Only use simple XPaths here (only / and []).
     * @param findingConsumer to report the validation findings to
     * @return {@code true} if validation had no findings.
     */
    protected boolean verifyPluginHasProperty(final Node plugin, final String xPath,
            final Consumer<ValidationFinding> findingConsumer) {
        if (runXPath(plugin, xPath) != null) {
            return true;
        } else {
            findingConsumer.accept(ValidationFinding.withMessage(ExaError.messageBuilder("E-PK-13").message(
                    "The {{plugin|uq}}'s configuration does not contain the required property {{required property}}.",
                    this.pluginArtifactId, xPath).toString())//
                    .andFix(getCopyFixForMissingProperty(plugin, xPath)).build());
            return false;
        }
    }

    private ValidationFinding.Fix getCopyFixForMissingProperty(final Node plugin, final String missingPropertiesXPath) {
        return (Log log) -> {
            final List<String> pathSegments = XPathSplitter.split(missingPropertiesXPath);
            findAndCopyFirstMissingPropertyFromTemplate(plugin, pathSegments);
        };
    }

    private void findAndCopyFirstMissingPropertyFromTemplate(final Node plugin, final List<String> pathSegments) {
        for (var pathLength = 1; pathLength <= pathSegments.size(); pathLength++) {
            final var currentXpath = String.join("/", pathSegments.subList(0, pathLength));
            if (runXPath(plugin, currentXpath) == null) {
                final Node parent = runXpathFromSegments(plugin, pathSegments, pathLength - 1);
                copyPropertyFromTemplate(plugin, currentXpath, parent);
                break;
            }
        }
    }

    private void copyPropertyFromTemplate(final Node plugin, final String xPath, final Node parent) {
        final var node = runXPath(getPluginTemplate(), xPath);
        final var importedNode = plugin.getOwnerDocument().importNode(node, true);
        parent.appendChild(importedNode);
    }

    private Node runXpathFromSegments(final Node plugin, final List<String> pathSegmments, final int pathLength) {
        if (pathLength == 0) {
            return plugin;
        } else {
            return runXPath(plugin, String.join("/", pathSegmments.subList(0, pathLength)));
        }
    }

    /**
     * Helper function for validating that an element exists and has same content as in the template. In FIX mode, this
     * method creates the missing element and replaces wrong content with the content from the template.
     *
     * @param plugin          the plugin to validate
     * @param propertyXpath   path of the property to validate / fix. Only use simple XPaths here (only / and [])
     * @param findingConsumer consumer to report findings to
     */
    protected void verifyPluginPropertyHasExactValue(final Node plugin, final String propertyXpath,
            final Consumer<ValidationFinding> findingConsumer) {
        if (verifyPluginHasProperty(plugin, propertyXpath, findingConsumer)) {
            final Node property = runXPath(plugin, propertyXpath);
            final Node templateProperty = runXPath(getPluginTemplate(), propertyXpath);
            validatePropertiesAreEqual(plugin, propertyXpath, findingConsumer, property, templateProperty);
        }
    }

    /**
     * Verify that a plugin does not have a specific property.
     * 
     * @param plugin          the plugin to validate
     * @param propertyXpath   path of the property to validate / fix. Only use simple XPaths here (only / and [])
     * @param findingConsumer consumer to report findings to
     */
    protected void verifyPluginDoesNotHaveProperty(final Node plugin, final String propertyXpath,
            final Consumer<ValidationFinding> findingConsumer) {
        final var node = runXPath(plugin, propertyXpath);
        if (node != null) {
            findingConsumer.accept(ValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-28")
                            .message("The plugin {{PLUGIN}} has an illegal property {{PROPERTY}}.")
                            .mitigation("Please remove it.").parameter("PLUGIN", this.pluginArtifactId)
                            .parameter("PROPERTY", propertyXpath).toString())
                    .andFix(log -> node.getParentNode().removeChild(node)).build());
        }
    }

    private void validatePropertiesAreEqual(final Node plugin, final String propertyXpath,
            final Consumer<ValidationFinding> findingConsumer, final Node property, final Node templateProperty) {
        if (!isXmlEqual(property, templateProperty)) {
            findingConsumer.accept(ValidationFinding.withMessage(ExaError.messageBuilder("E-PK-14")
                    .message("The {{plugin|uq}}'s configuration-property {{property path}} has an illegal value.",
                            this.pluginArtifactId, propertyXpath)
                    .toString())//
                    .andFix((Log log) -> {
                        final var importedProperty = plugin.getOwnerDocument().importNode(templateProperty, true);
                        property.getParentNode().replaceChild(importedProperty, property);
                    }).build());
        }
    }

    @Override
    public boolean isExcluded(final Collection<String> excludedPlugins) {
        return excludedPlugins.contains(this.pluginGroupId + ":" + this.pluginArtifactId);
    }
}
