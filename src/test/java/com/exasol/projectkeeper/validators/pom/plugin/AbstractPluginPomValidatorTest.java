package com.exasol.projectkeeper.validators.pom.plugin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.*;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.validators.pom.PomRenderer;

class AbstractPluginPomValidatorTest {

    @Test
    void testVerifyPluginPropertyHasExactValue() throws ParserConfigurationException {
        final Node plugin = createPlugin();
        final List<ValidationFinding> findings = new ArrayList<>();
        final Element expectedProperty = plugin.getOwnerDocument().createElement("test");
        expectedProperty.setTextContent("expectedValue");
        new StubPluginValidator().verifyPluginPropertyHasExactValue(plugin, "/test", expectedProperty, findings::add);
        final List<String> messages = findings.stream().map(ValidationFinding::getMessage).collect(Collectors.toList());
        assertThat(messages, contains(
                "E-PK-70: The maven-dependency-plugin's configuration does not contain the required property '/test'."));
    }

    @Test
    void testVerifyPluginPropertyHasExactValueWithWrongValue() throws ParserConfigurationException {
        final Node plugin = createPlugin();
        final Element property = plugin.getOwnerDocument().createElement("test");
        property.setTextContent("otherValue");
        plugin.appendChild(property);
        final List<ValidationFinding> findings = new ArrayList<>();
        final Element expectedProperty = plugin.getOwnerDocument().createElement("test");
        expectedProperty.setTextContent("expectedValue");
        new StubPluginValidator().verifyPluginPropertyHasExactValue(plugin, "test", expectedProperty, findings::add);
        final List<String> messages = findings.stream().map(ValidationFinding::getMessage).collect(Collectors.toList());
        assertThat(messages, contains(
                "E-PK-71: The maven-dependency-plugin's configuration-property 'test' has an illegal value. Actual value: '<?xml version=\"1.0\" encoding=\"UTF-8\"?><test>otherValue</test>'. Expected value: '<?xml version=\"1.0\" encoding=\"UTF-8\"?><test>expectedValue</test>'."));
    }

    private Node createPlugin() throws ParserConfigurationException {
        final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        return document.createElement("plugin");
    }

    private static class StubPluginValidator extends AbstractPluginPomValidator {
        StubPluginValidator() {
            super("maven_templates/maven-dependency-plugin.xml");
        }

        @Override
        public ProjectKeeperModule getModule() {
            return ProjectKeeperModule.DEFAULT;
        }
    }
}