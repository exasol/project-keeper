package com.exasol.projectkeeper.validators.pom;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.ProjectKeeperModule;

class PomFileGeneratorTest {

    @Test
    void test() {
        final String result = new PomFileGenerator().generatePomContent(List.of(ProjectKeeperModule.DEFAULT),
                "com.example", "my-parent-pom", "1.0.0");
        System.out.println(result);
    }
}