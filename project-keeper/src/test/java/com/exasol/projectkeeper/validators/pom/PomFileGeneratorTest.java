package com.exasol.projectkeeper.validators.pom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.projectkeeper.ProjectKeeperModule;

class PomFileGeneratorTest {

    @Test
    void testGenerateWithDefaultModule() throws XmlPullParserException, IOException {
        final Model pom = runGeneration(List.of(ProjectKeeperModule.DEFAULT));
        final List<String> pluginNames = getPluginNames(pom);
        assertAll(//
                () -> assertThat(pom.getGroupId(), equalTo("com.example")),
                () -> assertThat(pom.getArtifactId(), equalTo("my-parent-pom")),
                () -> assertThat(pom.getVersion(), equalTo("1.0.0")),
                () -> assertThat(pluginNames,
                        containsInAnyOrder("maven-enforcer-plugin", "flatten-maven-plugin", "ossindex-maven-plugin",
                                "reproducible-build-maven-plugin", "maven-surefire-plugin", "versions-maven-plugin",
                                "jacoco-maven-plugin", "error-code-crawler-maven-plugin")));
    }

    private List<String> getPluginNames(final Model pom) {
        return pom.getBuild().getPlugins().stream().map(Plugin::getArtifactId).collect(Collectors.toList());
    }

    private List<String> getDependencyNames(final Model pom) {
        return pom.getDependencies().stream().map(Dependency::getArtifactId).collect(Collectors.toList());
    }

    private Model runGeneration(final List<ProjectKeeperModule> modules) throws IOException, XmlPullParserException {
        final String result = new PomFileGenerator().generatePomContent(modules, "com.example", "my-parent-pom",
                "1.0.0");
        try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(
                result.getBytes(StandardCharsets.UTF_8))) {
            return new MavenXpp3Reader().read(inputStream);
        }
    }

    static Stream<Arguments> testPluginsAddedByModuleCases() {
        return Stream.of(//
                Arguments.of("JAR_ARTIFACT", List.of("maven-assembly-plugin", "maven-jar-plugin")), //
                Arguments.of("MAVEN_CENTRAL",
                        List.of("maven-deploy-plugin", "maven-gpg-plugin", "maven-source-plugin",
                                "nexus-staging-maven-plugin")),
                Arguments.of("UDF_COVERAGE", List.of("maven-dependency-plugin")),
                Arguments.of("INTEGRATION_TESTS", List.of("maven-failsafe-plugin")),
                Arguments.of("LOMBOK", List.of("lombok-maven-plugin"))//
        );
    }

    @ParameterizedTest
    @MethodSource("testPluginsAddedByModuleCases")
    void testPluginsAddedByModule(final ProjectKeeperModule module, final List<String> expectedPlugins)
            throws XmlPullParserException, IOException {
        final Model pom = runGeneration(List.of(module));
        assertThat(getPluginNames(pom), hasItems(expectedPlugins.toArray(String[]::new)));
    }

    static Stream<Arguments> testDependenciesAddedByModuleCases() {
        return Stream.of(//
                Arguments.of("UDF_COVERAGE", List.of("org.jacoco.agent")), //
                Arguments.of("LOMBOK", List.of("lombok"))//
        );
    }

    @ParameterizedTest
    @MethodSource("testDependenciesAddedByModuleCases")
    void testDependenciesAddedByModule(final ProjectKeeperModule module, final List<String> expectedDependencies)
            throws XmlPullParserException, IOException {
        final Model pom = runGeneration(List.of(module));
        assertThat(getDependencyNames(pom), hasItems(expectedDependencies.toArray(String[]::new)));
    }
}