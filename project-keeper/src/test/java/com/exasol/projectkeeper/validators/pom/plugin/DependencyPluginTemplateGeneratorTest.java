package com.exasol.projectkeeper.validators.pom.plugin;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;

class DependencyPluginTemplateGeneratorTest {

    @Test
    void noModuleActive() {
        assertThat(testee().generateTemplate(emptyList()).isEmpty(), is(true));
    }

    @ParameterizedTest
    @ValueSource(strings = { "UDF_COVERAGE", "MOCKITO_AGENT" })
    void moduleActive(final ProjectKeeperModule module) {
        assertThat(testee().generateTemplate(List.of(module)).isEmpty(), is(false));
    }

    DependencyPluginTemplateGenerator testee() {
        return new DependencyPluginTemplateGenerator();
    }
}
