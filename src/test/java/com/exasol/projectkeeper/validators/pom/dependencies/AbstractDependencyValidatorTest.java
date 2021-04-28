package com.exasol.projectkeeper.validators.pom.dependencies;

import static com.exasol.projectkeeper.validators.pom.PomTesting.POM_WITH_NO_PLUGINS;
import static com.exasol.projectkeeper.validators.pom.PomTesting.readXmlFromResources;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmlunit.matchers.EvaluateXPathMatcher;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;

class AbstractDependencyValidatorTest extends AbstractDependencyValidatorAbstractTest {

    AbstractDependencyValidatorTest() {
        super(new DependencyValidatorStub());
    }

    @Test
    void testValidation() throws ParserConfigurationException, SAXException, IOException {
        final Document pom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        final List<String> findings = new ArrayList<>();
        new DependencyValidatorStub().validate(pom, Collections.emptyList(),
                finding -> findings.add(finding.getMessage()));
        assertThat(findings, containsInAnyOrder("E-PK-29: Missing dependency 'com.example:my-artifact'."));
    }

    @Test
    void testFix() throws ParserConfigurationException, SAXException, IOException {
        final Document pom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        applyFixes(pom);
        assertAll(//
                () -> assertThat(pom,
                        EvaluateXPathMatcher.hasXPath("/project/dependencies/dependency/groupId/text()",
                                equalTo("com.example"))),
                () -> assertThat(pom,
                        EvaluateXPathMatcher.hasXPath("/project/dependencies/dependency/artifactId/text()",
                                equalTo("my-artifact"))),
                () -> assertThat(pom,
                        EvaluateXPathMatcher.hasXPath("/project/dependencies/dependency/scope/text()",
                                equalTo("provided"))),
                () -> assertThat(pom, EvaluateXPathMatcher.hasXPath("/project/dependencies/dependency/version/text()",
                        equalTo("1.2.3")))//
        );
    }

    private static class DependencyValidatorStub extends AbstractDependencyValidator {

        protected DependencyValidatorStub() {
            super("com.example", "my-artifact", "1.2.3", Scope.PROVIDED);
        }

        @Override
        public ProjectKeeperModule getModule() {
            return null;
        }

        @Override
        protected void validateDetails(final Node dependency, final Consumer<ValidationFinding> findingConsumer) {

        }
    }
}