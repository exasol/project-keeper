package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.XPathErrorHanlingWrapper.runXpath;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Abstract basis for maven plugin configuration validation.
 * <p>
 * Create a new plugin validation by adding a template file to {@code src/main/resources/maven_templates/}. Next create
 * an instance of this class and pass the template's name to the super constructor. If you want to enforce more than
 * only the existence of the plugin definition, override {@link #validatePluginConfiguration(Node, RunMode)}. Finally
 * add your class to {@link PomFileTemplateRunner#TEMPLATES}.
 * </p>
 */
public abstract class AbstractPluginPomTemplate implements PomTemplate {
    public static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
    private final String pluginName;
    private final String pluginGroup;
    private final Node template;

    public AbstractPluginPomTemplate(final String pluginName, final String pluginGroup,
            final String templateResourceName) {
        this.pluginName = pluginName;
        this.pluginGroup = pluginGroup;
        this.template = readPluginTemplate(templateResourceName);
    }

    @Override
    public void run(final Document pom, final RunMode runMode) throws PomTemplateValidationException {
        final Node plugins = runXpath(pom, "/project/build/plugins");
        Node plugin = findPluginNode(plugins);
        if (plugin == null) {
            if (runMode == RunMode.VERIFY) {
                throw new PomTemplateValidationException(
                        "Missing maven plugin " + this.pluginGroup + ":" + this.pluginName + ".");
            } else {// fix
                plugin = pom.importNode(getPluginTemplate(), true);
                plugins.appendChild(plugin);
            }
        }
        validatePluginConfiguration(plugin, runMode);
    }

    private Node findPluginNode(final Node pluginsNode) {
        final String xPath = "plugin[artifactId[text()='" + this.pluginName + "'] and groupId[text()='"
                + this.pluginGroup + "']]";
        return runXpath(pluginsNode, xPath);
    }

    protected Node getPluginTemplate() {
        return this.template;
    }

    private Node readPluginTemplate(final String templateResourceName) {
        try (final InputStream templateInputStream = getClass().getClassLoader()
                .getResourceAsStream(templateResourceName)) {
            if (templateInputStream == null) {
                throw new IllegalStateException("F-PK-11 Failed to open " + this.pluginName + "'s template.");
            }
            DOCUMENT_BUILDER_FACTORY.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            DOCUMENT_BUILDER_FACTORY.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            final DocumentBuilder documentBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
            return documentBuilder.parse(templateInputStream).getFirstChild();
        } catch (final IOException | SAXException | ParserConfigurationException e) {
            throw new IllegalStateException("F-PK-10 Failed to parse " + this.pluginName + "'s template.");
        }
    }

    /**
     * Hook for validating the plugin specific parts.
     * <p>
     * Hint: use {@link #verifyOrFixHasElement(Node, RunMode, String)} or
     * {@link #verifyOrFixPluginPropertyHasExactValue(Node, RunMode, String)} in this method's implementation.
     * </p>
     *
     * @param plugin  the plugin to validate
     * @param runMode mode (verify or fix)
     * @throws PomTemplateValidationException if validation fails
     */
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode)
            throws PomTemplateValidationException {
    }

    /**
     * Helper function for validating that an element exists. In FIX mode, this method creates the missing element with
     * the content from the template.
     * <p>
     * This method does not validate, that the element has the correct content. For that purpose use
     * {@link #verifyOrFixPluginPropertyHasExactValue(Node, RunMode, String)}.
     * </p>
     * 
     * @param plugin  the plugin to validate
     * @param runMode mode (verify or fix)
     * @param xPath   path of the property to validate / fix. Only use simple XPaths here (only /).
     * @throws PomTemplateValidationException if validation fails
     */
    protected void verifyOrFixHasElement(final Node plugin, final RunMode runMode, final String xPath)
            throws PomTemplateValidationException {
        final Node actualConfiguration = runXpath(plugin, xPath);
        if (actualConfiguration == null) {
            if (runMode == RunMode.VERIFY) {
                throw new PomTemplateValidationException("pom.xml: The " + this.pluginName
                        + "'s configuration does not contain the required property " + xPath + ".");
            } else {
                addMissingElement(plugin, xPath);
            }
        }
    }

    private void addMissingElement(final Node plugin, final String xPath) {
        final List<String> pathSegments = Arrays.asList(xPath.split("/"));
        for (int pathLength = 1; pathLength <= pathSegments.size(); pathLength++) {
            final String currentXpath = String.join("/", pathSegments.subList(0, pathLength));
            if (runXpath(plugin, currentXpath) == null) {
                final Node parent = runXpathFromSegments(plugin, pathSegments, pathLength - 1);
                final Node templateConfiguration = plugin.getOwnerDocument()
                        .importNode(runXpath(getPluginTemplate(), currentXpath), true);
                parent.appendChild(templateConfiguration);
            }
        }
    }

    private Node runXpathFromSegments(final Node plugin, final List<String> pathSegmments, final int pathLength) {
        if (pathLength == 0) {
            return plugin;
        } else {
            return runXpath(plugin, String.join("/", pathSegmments.subList(0, pathLength)));
        }
    }

    /**
     * Helper function for validating that an element exists and has same same content as in the template. In FIX mode,
     * this method creates the missing element and replaces wrong content with the content from the template.
     *
     * @param plugin        the plugin to validate
     * @param runMode       mode (verify or fix)
     * @param propertyXpath path of the property to validate / fix. Only use simple XPaths here (only /).
     * @throws PomTemplateValidationException if validation fails
     */
    protected void verifyOrFixPluginPropertyHasExactValue(final Node plugin, final RunMode runMode,
            final String propertyXpath) throws PomTemplateValidationException {
        verifyOrFixHasElement(plugin, runMode, propertyXpath);
        final Node property = runXpath(plugin, propertyXpath);
        final Node templateProperty = runXpath(getPluginTemplate(), propertyXpath);
        if (!property.isEqualNode(templateProperty)) {
            if (runMode == RunMode.VERIFY) {
                throw new PomTemplateValidationException("pom.xml: The " + this.pluginName
                        + "'s configuration-property " + propertyXpath + " has an illegal value.");
            } else {
                final Node importedProperty = plugin.getOwnerDocument().importNode(templateProperty, true);
                property.getParentNode().replaceChild(importedProperty, property);
            }
        }
    }
}
