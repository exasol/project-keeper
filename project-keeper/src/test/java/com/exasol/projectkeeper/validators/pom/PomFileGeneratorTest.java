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
import com.exasol.projectkeeper.config.ProjectKeeperConfig;

class PomFileGeneratorTest {

    private static final String TEST_REPO_NAME = "my-repo";

    static Stream<Arguments> testGenerationWithParentPomCases() {
        return Stream.of(//
                Arguments.of(null, "../pom.xml" /* that seems to be a default in the reader */),
                Arguments.of("./myParentPom.xml", "./myParentPom.xml")//
        );
    }

    private List<String> getPluginNames(final Model pom) {
        return pom.getBuild().getPlugins().stream().map(Plugin::getArtifactId).collect(Collectors.toList());
    }

    private List<String> getDependencyNames(final Model pom) {
        return pom.getDependencies().stream().map(Dependency::getArtifactId).collect(Collectors.toList());
    }

    @Test
    void testGenerateWithDefaultModule() throws XmlPullParserException, IOException {
        final Model pom = runGeneration(List.of(ProjectKeeperModule.DEFAULT), null);
        final List<String> pluginNames = getPluginNames(pom);
        assertAll(//
                () -> assertThat(pom.getGroupId(), equalTo("com.example")),
                () -> assertThat(pom.getArtifactId(), equalTo("my-parent-pom")),
                () -> assertThat(pom.getScm().getConnection(),
                        equalTo("scm:git:https://github.com/exasol/my-repo.git")),
                () -> assertThat(pom.getScm().getDeveloperConnection(),
                        equalTo("scm:git:https://github.com/exasol/my-repo.git")),
                () -> assertThat(pom.getScm().getUrl(), equalTo("https://github.com/exasol/my-repo/")),
                () -> assertThat(pom.getDevelopers().get(0).getName(), equalTo("Exasol")),
                () -> assertThat(pom.getDevelopers().get(0).getOrganization(), equalTo("Exasol AG")),
                () -> assertThat(pom.getDevelopers().get(0).getOrganizationUrl(), equalTo("https://www.exasol.com/")),
                () -> assertThat(pom.getDevelopers().get(0).getEmail(), equalTo("opensource@exasol.com")),
                () -> assertThat(pom.getVersion(), equalTo("1.0.0")),
                () -> assertThat(pluginNames,
                        containsInAnyOrder("sonar-maven-plugin", "maven-compiler-plugin", "maven-enforcer-plugin",
                                "flatten-maven-plugin", "ossindex-maven-plugin", "reproducible-build-maven-plugin",
                                "maven-surefire-plugin", "versions-maven-plugin", "jacoco-maven-plugin",
                                "error-code-crawler-maven-plugin")));
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

    private Model runGeneration(final List<ProjectKeeperModule> modules,
            final ProjectKeeperConfig.ParentPomRef parentPomRef) throws IOException, XmlPullParserException {
        final String result = new PomFileGenerator().generatePomContent(modules, "com.example", "my-parent-pom",
                "1.0.0", parentPomRef, TEST_REPO_NAME);
        try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(
                result.getBytes(StandardCharsets.UTF_8))) {
            return new MavenXpp3Reader().read(inputStream);
        }
    }

    static Stream<Arguments> testDependenciesAddedByModuleCases() {
        return Stream.of(//
                Arguments.of("UDF_COVERAGE", List.of("org.jacoco.agent")), //
                Arguments.of("LOMBOK", List.of("lombok"))//
        );
    }

    @ParameterizedTest
    @MethodSource("testPluginsAddedByModuleCases")
    void testPluginsAddedByModule(final ProjectKeeperModule module, final List<String> expectedPlugins)
            throws XmlPullParserException, IOException {
        final Model pom = runGeneration(List.of(module), null);
        assertThat(getPluginNames(pom), hasItems(expectedPlugins.toArray(String[]::new)));
    }

    @ParameterizedTest
    @MethodSource("testDependenciesAddedByModuleCases")
    void testDependenciesAddedByModule(final ProjectKeeperModule module, final List<String> expectedDependencies)
            throws XmlPullParserException, IOException {
        final Model pom = runGeneration(List.of(module), null);
        assertThat(getDependencyNames(pom), hasItems(expectedDependencies.toArray(String[]::new)));
    }

    @ParameterizedTest
    @MethodSource("testGenerationWithParentPomCases")
    void testGenerationWithParentPom(final String relativePath, final String expectedRelativePath)
            throws XmlPullParserException, IOException {
        final Model pom = runGeneration(List.of(ProjectKeeperModule.DEFAULT),
                new ProjectKeeperConfig.ParentPomRef("com.example", "my-parent", "1.2.3", relativePath));
        final Parent parent = pom.getParent();
        assertAll(//
                () -> assertThat(parent.getGroupId(), equalTo("com.example")),
                () -> assertThat(parent.getArtifactId(), equalTo("my-parent")),
                () -> assertThat(parent.getVersion(), equalTo("1.2.3")),
                () -> assertThat(parent.getRelativePath(), equalTo(expectedRelativePath))//
        );
    }
}