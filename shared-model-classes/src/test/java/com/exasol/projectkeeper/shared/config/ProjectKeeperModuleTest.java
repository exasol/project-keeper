package com.exasol.projectkeeper.shared.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProjectKeeperModuleTest {
    @ParameterizedTest
    @CsvSource({ "DEFAULT, DEFAULT", "maven_central, MAVEN_CENTRAL" })
    void getModuleByNameReturnsModule(final String moduleName, final ProjectKeeperModule expected) {
        assertThat(ProjectKeeperModule.getModuleByName(moduleName), equalTo(expected));
    }

    @Test
    void getModuleByNameFails() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ProjectKeeperModule.getModuleByName("unknown"));
        assertThat(exception.getMessage(), startsWith(
                "E-PK-SMC-81: Unknown module: 'unknown'. Please update your modules configuration in the .project-keeper.yml to use one of the supported modules: 'default, maven_central"));
    }
}
