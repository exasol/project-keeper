package com.exasol.projectkeeper.validators.pom.plugin;

import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.Optional;

import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;

/**
 * Generator for the maven-dependency-plugin
 */
public class DependencyPluginTemplateGenerator implements PluginTemplateGenerator {

    private static final String TEMPLATE = "maven_templates/maven-dependency-plugin.xml";

    /**
     * Create a new instance.
     */
    public DependencyPluginTemplateGenerator() {
        // Empty constructor required by javadoc
    }

    @Override
    public Optional<Node> generateTemplate(final Collection<ProjectKeeperModule> enabledModules) {
        if (!enabledModules.contains(ProjectKeeperModule.UDF_COVERAGE)
                && !enabledModules.contains(ProjectKeeperModule.MOCKITO_AGENT)) {
            return Optional.empty();
        }

        final Node pluginTemplate = new PluginTemplateReader().readPluginTemplate(TEMPLATE);
        addJacocoExecution(pluginTemplate, enabledModules);
        addMockitoExecution(pluginTemplate, enabledModules);

        return Optional.of(pluginTemplate);
    }

    private static void addMockitoExecution(final Node node, final Collection<ProjectKeeperModule> enabledModules) {
        if (!enabledModules.contains(ProjectKeeperModule.MOCKITO_AGENT)) {
            return;
        }

        addExecutionContent(node, """
                <execution>
                    <goals>
                        <!-- Allow resolving dependency placeholders like ${org.mockito:mockito-core:jar} -->
                        <goal>properties</goal>
                    </goals>
                </execution>
                """);
    }

    private static void addJacocoExecution(final Node node, final Collection<ProjectKeeperModule> enabledModules) {
        if (!enabledModules.contains(ProjectKeeperModule.UDF_COVERAGE)) {
            return;
        }

        addExecutionContent(node, """
                <execution>
                    <id>copy-jacoco</id>
                    <goals>
                        <goal>copy-dependencies</goal>
                    </goals>
                    <phase>compile</phase>
                    <configuration>
                        <includeArtifactIds>org.jacoco.agent</includeArtifactIds>
                        <includeClassifiers>runtime</includeClassifiers>
                        <outputDirectory>${project.build.directory}/jacoco-agent</outputDirectory>
                        <stripVersion>true</stripVersion>
                    </configuration>
                </execution>
                """);
    }

    private static void addExecutionContent(final Node node, final String executionContent) {
        final Node executionNode = new PluginTemplateReader().readXmlContent(executionContent);
        final Node exectutionsNode = runXPath(node, "/plugin/executions");
        exectutionsNode.getOwnerDocument().adoptNode(executionNode);
        exectutionsNode.appendChild(executionNode);
    }
}
