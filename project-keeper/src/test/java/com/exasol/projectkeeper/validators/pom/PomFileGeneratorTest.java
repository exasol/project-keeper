package com.exasol.projectkeeper.validators.pom;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import com.exasol.projectkeeper.RepoInfo;
import com.exasol.projectkeeper.shared.config.ParentPomRef;
import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;

class PomFileGeneratorTest {

    private static final String TEST_REPO_NAME = "my-repo";
    private static final String TEST_REPO_LICENSE = "My License";

    static Stream<Arguments> testGenerationWithParentPomCases() {
        return Stream.of(//
                Arguments.of(null, "../pom.xml" /* that seems to be a default in the reader */),
                Arguments.of("./myParentPom.xml", "./myParentPom.xml")//
        );
    }

    private List<String> getPluginNames(final Model pom) {
        return pom.getBuild().getPlugins().stream().map(Plugin::getArtifactId).toList();
    }

    private List<String> getDependencyNames(final Model pom) {
        return pom.getDependencies().stream().map(Dependency::getArtifactId).toList();
    }

    // [utest -> dsn~mvn-toolchain~1]
    @Test
    void testGenerateWithDefaultModule() {
        final Model pom = runGeneration(List.of(ProjectKeeperModule.DEFAULT), null);
        final List<String> pluginNames = getPluginNames(pom);
        final License license = pom.getLicenses().get(0);
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
                () -> assertThat(license.getUrl(),
                        equalTo("https://github.com/exasol/" + TEST_REPO_NAME + "/blob/main/LICENSE")),
                () -> assertThat(license.getName(), equalTo("My License")),
                () -> assertThat(license.getDistribution(), equalTo("repo")),
                () -> assertThat(pluginNames,
                        containsInAnyOrder("sonar-maven-plugin", "maven-compiler-plugin", "maven-enforcer-plugin",
                                "flatten-maven-plugin", "ossindex-maven-plugin", "git-commit-id-maven-plugin",
                                "maven-surefire-plugin", "versions-maven-plugin", "jacoco-maven-plugin",
                                "error-code-crawler-maven-plugin", "duplicate-finder-maven-plugin",
                                "maven-toolchains-plugin", "maven-clean-plugin", "maven-install-plugin",
                                "maven-resources-plugin", "maven-site-plugin", "quality-summarizer-maven-plugin")));
    }

    static Stream<Arguments> testPluginsAddedByModuleCases() {
        return Stream.of(//
                Arguments.of("JAR_ARTIFACT", List.of("maven-assembly-plugin", "maven-jar-plugin")), //
                Arguments.of("MAVEN_CENTRAL",
                        List.of("maven-deploy-plugin", "maven-gpg-plugin", "maven-source-plugin",
                                "nexus-staging-maven-plugin")),
                Arguments.of("UDF_COVERAGE", List.of("maven-dependency-plugin")),
                Arguments.of("INTEGRATION_TESTS", List.of("maven-failsafe-plugin")),
                Arguments.of("NATIVE_IMAGE", List.of("native-image-maven-plugin")),
                Arguments.of("LOMBOK", List.of("lombok-maven-plugin"))//
        );
    }

    @Test
    void testProfilesForNativeImages() {
        final Model pom = runGeneration(List.of(ProjectKeeperModule.NATIVE_IMAGE), null);
        final List<Profile> profiles = pom.getProfiles();
        final Profile profile1 = profiles.get(0);
        final Profile profile2 = profiles.get(1);
        assertAll(//
                () -> assertThat(profile1.getId(), equalTo("default")),
                () -> assertThat(profile1.getActivation().isActiveByDefault(), equalTo(true)),
                () -> assertThat(profile2.getId(), equalTo("skipNativeImage"))//
        );
    }

    @Test
    void testAddJavaVersionPropertyForRootPom() {
        final Model pom = runGeneration(emptyList(), null);
        assertThat(pom.getProperties().get("java.version"), equalTo(PomFileGenerator.DEFAULT_JAVA_VERSION));
    }

    @Test
    void testOmitJavaVersionPropertyForNonRootPom() {
        final Model pom = runGeneration(emptyList(), new ParentPomRef("group", "parent-artifact", "version", "path"));
        assertThat(pom.getProperties().get("java.version"), nullValue());
    }

    @Test
    void testAddTestExcludeTagsPropertyForRootPom() {
        final Model pom = runGeneration(emptyList(), null);
        assertThat(pom.getProperties().get("test.excludeTags"), equalTo(""));
    }

    @Test
    void testOmitTestExcludeTagsPropertyForNonRootPom() {
        final Model pom = runGeneration(emptyList(), new ParentPomRef("group", "parent-artifact", "version", "path"));
        assertThat(pom.getProperties().get("test.excludeTags"), nullValue());
    }

    private Model runGeneration(final List<ProjectKeeperModule> modules, final ParentPomRef parentPomRef) {
        final String result = new PomFileGenerator().generatePomContent(modules, "com.example", "my-parent-pom",
                "1.0.0", parentPomRef, new RepoInfo(TEST_REPO_NAME, TEST_REPO_LICENSE));
        try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(
                result.getBytes(StandardCharsets.UTF_8))) {
            return new MavenXpp3Reader().read(inputStream);
        } catch (IOException | XmlPullParserException exception) {
            throw new IllegalStateException("Failed to generate POM file: " + exception.getMessage(), exception);
        }
    }

    static Stream<Arguments> testDependenciesAddedByModuleCases() {
        return Stream.of(//
                Arguments.of("UDF_COVERAGE", List.of("org.jacoco.agent")), //
                Arguments.of("LOMBOK", List.of("lombok"))//
        );
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void distributionManagement(final boolean hasDm) {
        final Model pom = runGeneration(hasDm ? List.of(ProjectKeeperModule.MAVEN_CENTRAL) : List.of(), null);
        assertThat(pom.getDistributionManagement(), hasDm ? not(nullValue()) : nullValue());
    }

    @ParameterizedTest
    @MethodSource("testPluginsAddedByModuleCases")
    void testPluginsAddedByModule(final ProjectKeeperModule module, final List<String> expectedPlugins) {
        final Model pom = runGeneration(List.of(module), null);
        assertThat(getPluginNames(pom), hasItems(expectedPlugins.toArray(String[]::new)));
    }

    @ParameterizedTest
    @MethodSource("testDependenciesAddedByModuleCases")
    void testDependenciesAddedByModule(final ProjectKeeperModule module, final List<String> expectedDependencies) {
        final Model pom = runGeneration(List.of(module), null);
        assertThat(getDependencyNames(pom), hasItems(expectedDependencies.toArray(String[]::new)));
    }

    @ParameterizedTest
    @MethodSource("testGenerationWithParentPomCases")
    void testGenerationWithParentPom(final String relativePath, final String expectedRelativePath) {
        final Model pom = runGeneration(List.of(ProjectKeeperModule.DEFAULT),
                new ParentPomRef("com.example", "my-parent", "1.2.3", relativePath));
        final Parent parent = pom.getParent();
        assertAll(//
                () -> assertThat(parent.getGroupId(), equalTo("com.example")),
                () -> assertThat(parent.getArtifactId(), equalTo("my-parent")),
                () -> assertThat(parent.getVersion(), equalTo("1.2.3")),
                () -> assertThat(parent.getRelativePath(), equalTo(expectedRelativePath))//
        );
    }
}
