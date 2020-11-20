package com.exasol.projectkeeper.validators.pom.dependencies;

import static com.exasol.projectkeeper.validators.pom.PomTesting.POM_WITH_NO_PLUGINS;
import static com.exasol.projectkeeper.validators.pom.PomTesting.readXmlFromResources;
import static com.exasol.xpath.XPathErrorHanlingWrapper.runXPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

class JacocoAgentDependencyValidatorTest extends AbstractDependencyValidatorAbstractTest {

    JacocoAgentDependencyValidatorTest() {
        super(new JacocoAgentDependencyValidator());
    }

    @Test
    void testMissingClassifier() throws ParserConfigurationException, SAXException, IOException {
        final Document pom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        applyFixes(pom);
        final Node classifier = runXPath(pom, "/project/dependencies/dependency/classifier");
        classifier.getParentNode().removeChild(classifier);
        final List<String> findings = new ArrayList<>();
        new JacocoAgentDependencyValidator().validate(pom, List.of(), finding -> findings.add(finding.getMessage()));
        assertThat(findings, containsInAnyOrder(
                "E-PK-30: Missing property 'classifier' in dependency 'org.jacoco:org.jacoco.agent'."));
    }

    @Test
    void testWrongClassifier() throws ParserConfigurationException, SAXException, IOException {
        final Document pom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        applyFixes(pom);
        final Node classifier = runXPath(pom, "/project/dependencies/dependency/classifier");
        final Element wrongClassifier = pom.createElement("classifier");
        classifier.appendChild(pom.createTextNode("other"));
        classifier.getParentNode().replaceChild(wrongClassifier, classifier);
        final List<String> findings = new ArrayList<>();
        new JacocoAgentDependencyValidator().validate(pom, List.of(), finding -> findings.add(finding.getMessage()));
        assertThat(findings, containsInAnyOrder(
                "E-PK-31: The property 'classifier' of the dependency 'org.jacoco:org.jacoco.agent' has an illegal value."));
    }
}