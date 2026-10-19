package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.Test;

class MavenWrapperTemplatesTest {
    @Test
    void createsTemplatesForAllMavenWrapperFiles() {
        final List<FileTemplate> templates = MavenWrapperTemplates.getTemplates();
        assertAll(
                () -> assertThat(templates.stream().map(template -> template.getPathInProject().toString()).toList(),
                        containsInAnyOrder("mvnw", "mvnw.cmd", ".mvn/wrapper/maven-wrapper.properties")),
                () -> assertThat(templates.stream().filter(template -> template.getPathInProject().toString().equals("mvnw")).findFirst()
                        .orElseThrow().requiresExecutablePermission(), is(true)));
    }
}
