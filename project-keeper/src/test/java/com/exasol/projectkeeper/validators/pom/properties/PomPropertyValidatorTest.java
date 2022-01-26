package com.exasol.projectkeeper.validators.pom.properties;

import static com.exasol.projectkeeper.validators.FindingMatcher.hasFindingWithMessage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.*;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.validators.finding.FindingsFixer;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

class PomPropertyValidatorTest {
    @Test
    void testValidation() throws ParserConfigurationException {
        final Document document = createDocument();
        final List<ValidationFinding> findings = runValidation(document);
        assertThat(findings, hasFindingWithMessage(
                "E-PK-CORE-72: Missing required property '/project/myProperty' in pom.xml. Set the required property '/project/myProperty' to 'myValue'."));
    }

    @Test
    void testValid() throws ParserConfigurationException {
        final Document document = createDocumentWithMyProperty("myValue");
        final List<ValidationFinding> findings = runValidation(document);
        assertThat(findings, is(empty()));
    }

    private Document createDocumentWithMyProperty(final String propertyValue) throws ParserConfigurationException {
        final Document document = createDocument();
        final Element myProperty = document.createElement("myProperty");
        myProperty.setTextContent(propertyValue);
        final Node project = document.getFirstChild();
        project.appendChild(myProperty);
        return document;
    }

    @Test
    void testValidWithWhitespace() throws ParserConfigurationException {
        final Document document = createDocumentWithMyProperty("\n  \tmyValue\n  \t");
        final List<ValidationFinding> findings = runValidation(document);
        assertThat(findings, is(empty()));
    }

    @Test
    void testInvalid() throws ParserConfigurationException {
        final Document document = createDocumentWithMyProperty("wrongValue");
        final List<ValidationFinding> findings = runValidation(document);
        assertThat(findings, hasFindingWithMessage(
                "E-PK-CORE-73: The required property '/project/myProperty' pom.xml has an illegal value. Set the required property '/project/myProperty' to 'myValue'."));
    }

    @Test
    void testFix() throws ParserConfigurationException {
        final Document document = createDocument();
        final List<ValidationFinding> findingsInFirstRun = runValidation(document);
        new FindingsFixer(mock(Logger.class)).fixFindings(findingsInFirstRun);
        final List<ValidationFinding> findingsInSecondRun = runValidation(document);
        assertThat(findingsInSecondRun, is(empty()));
    }

    private Document createDocument() throws ParserConfigurationException {
        final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        document.appendChild(document.createElement("project"));
        return document;
    }

    private List<ValidationFinding> runValidation(final Document document) {
        final PomPropertyValidator validator = new PomPropertyValidator("/project/myProperty", "myValue",
                ProjectKeeperModule.DEFAULT);
        final List<ValidationFinding> findings = new ArrayList<>();
        validator.validate(document, Collections.emptyList(), findings::add);
        return findings;
    }
}