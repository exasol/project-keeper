package com.exasol.projectkeeper.validators.pom;

import static java.util.Collections.emptyList;

import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;
import com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper;

class MockitoAgentCustomizer {
    private final AnalyzedMavenSource mavenSource;

    public MockitoAgentCustomizer(final AnalyzedMavenSource mavenSource) {
        this.mavenSource = mavenSource;
    }

    public List<ValidationFinding> customize(final Document pom) {
        if (!containsMockitoDependency()) {
            return emptyList();
        }
        final List<ValidationFinding> findings = new ArrayList<>();
        findings.addAll(addJvmAgentToFailsafePlugin(pom));
        findings.addAll(addJvmAgentToSurefirePlugin(pom));
        findings.addAll(addExecutionToDependenciesPlugin(pom));
        return findings;
    }

    private List<ValidationFinding> addJvmAgentToFailsafePlugin(final Document pom) {
        return runXPath(pom,
                "/project/build/plugins/plugin[groupId='org.apache.maven.plugins' and artifactId='maven-failsafe-plugin']")
                .map(this::addJvmAgentToTestPlugin)
                .orElseGet(Collections::emptyList);
    }

    private List<ValidationFinding> addJvmAgentToSurefirePlugin(final Document pom) {
        return runXPath(pom,
                "/project/build/plugins/plugin[groupId='org.apache.maven.plugins' and artifactId='maven-surefire-plugin']")
                .map(this::addJvmAgentToTestPlugin)
                .orElseGet(() -> List.of(SimpleValidationFinding.withMessage(
                        "Mockito dependency found, but maven-surefire-plugin is missing - cannot add JVM agent")
                        .build()));
    }

    private List<ValidationFinding> addJvmAgentToTestPlugin(final Node plugin) {
        final SimpleValidationFinding finding = SimpleValidationFinding.withMessage(
                "Adding Mockito JVM agent to plugin " + plugin)
                .andFix((log) -> {
                    final Node argLineNode = runXPath(plugin, "configuration/argLine").orElseThrow();
                    final String existingArgs = argLineNode.getTextContent();
                    argLineNode.setTextContent("-javaagent:${org.mockito:mockito-core:jar}" + " " + existingArgs);
                })
                .build();
        return List.of(finding);
    }

    private List<ValidationFinding> addExecutionToDependenciesPlugin(final Document pom) {
        final Node dependencyPluginNode = runXPath(pom,
                "/project/build/plugins/plugin[groupId='org.apache.maven.plugins' and artifactId='maven-dependency-plugin']")
                .orElseThrow();
                SimpleValidationFinding.withMessage("Adding Mockito")
        // TODO Auto-generated method stub
        return emptyList();
    }

    private boolean containsMockitoDependency() {
        return mavenSource.getDependencies().getDependencies().stream()
                .map(ProjectDependency::coordinates)
                .anyMatch("org.mockito:mockito-core"::equals);
    }

    private Optional<Node> runXPath(final Node current, final String xPath) {
        return Optional.ofNullable(XPathErrorHandlingWrapper.runXPath(current, xPath));
    }
}
