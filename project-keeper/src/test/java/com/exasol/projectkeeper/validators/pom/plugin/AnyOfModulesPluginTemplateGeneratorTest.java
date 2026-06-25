package com.exasol.projectkeeper.validators.pom.plugin;

import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;

class AnyOfModulesPluginTemplateGeneratorTest {
    private static final String TEMPLATE = "maven_templates/spdx-maven-plugin.xml";

    @Test
    void noModuleActive() {
        assertThat(testee().generateTemplate(emptyList()).isEmpty(), is(true));
    }

    @Test
    void oneMatchingModuleActive() {
        final var template = testee().generateTemplate(List.of(ProjectKeeperModule.JAR_ARTIFACT));
        assertThat(template.isEmpty(), is(false));
        assertThat(runXPath(template.orElseThrow(), "/plugin/artifactId").getTextContent(), equalTo("spdx-maven-plugin"));
    }

    @Test
    void onlyNonMatchingModuleActive() {
        assertThat(testee().generateTemplate(List.of(ProjectKeeperModule.DEFAULT)).isEmpty(), is(true));
    }

    private AnyOfModulesPluginTemplateGenerator testee() {
        return new AnyOfModulesPluginTemplateGenerator(TEMPLATE,
                java.util.Set.of(ProjectKeeperModule.MAVEN_CENTRAL, ProjectKeeperModule.JAR_ARTIFACT));
    }
}
