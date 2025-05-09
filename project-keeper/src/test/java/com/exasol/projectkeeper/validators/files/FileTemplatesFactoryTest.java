package com.exasol.projectkeeper.validators.files;

import static java.util.Collections.emptySet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.shared.config.BuildOptions;
import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;

@ExtendWith(MockitoExtension.class)
class FileTemplatesFactoryTest {
    private static final String OWN_VERSION = "version";
    @Mock
    Logger loggerMock;

    @Test
    void testGetCiBuildTemplatesForMavenProject() {
        final Set<ProjectKeeperModule> modules = Collections.emptySet();
        final List<AnalyzedSource> sources = getMavenSourceWithModules(modules);
        final List<FileTemplate> templates = testee(sources).getGlobalTemplates();
        assertContainsTemplate(templates, ".github/workflows/ci-build.yml");
        assertContainsTemplate(templates, ".vscode/settings.json");
        final Optional<FileTemplate> gitattributes = findTemplate(templates, ".gitattributes");
        assertTrue(gitattributes.isPresent());
        assertThat(gitattributes.get().getContent(), containsString("pk_generated_parent.pom"));
    }

    // [utest->dsn~dependency-updater.workflow.generate~1]
    @Test
    void testGenerateDependencyUpdateWorkflow() {
        final Set<ProjectKeeperModule> modules = Collections.emptySet();
        final List<AnalyzedSource> sources = getMavenSourceWithModules(modules);
        final List<FileTemplate> templates = testee(sources).getGlobalTemplates();
        assertContainsTemplate(templates, ".github/workflows/dependencies_check.yml");
        assertContainsTemplate(templates, ".github/workflows/dependencies_update.yml");
    }

    private FileTemplatesFactory testee(final List<AnalyzedSource> sources) {
        return testee(true, sources);
    }

    private FileTemplatesFactory testee(final boolean hasNpmModule, final List<AnalyzedSource> sources) {
        return new FileTemplatesFactory(this.loggerMock, OWN_VERSION, hasNpmModule,
                BuildOptions.builder().runnerOs("ci-build-runner-os").build(), sources);
    }

    private List<AnalyzedSource> getMavenSourceWithModules(final Set<ProjectKeeperModule> modules) {
        return List.of(AnalyzedMavenSource.builder().modules(modules).isRootProject(true).javaVersion("11").build());
    }

    /*
     * In this test case we create an unsupported project (maven build in nested project but not aggregator pom in the
     * root). For those projects project-keeper can't create CI-templates. So we expect a warning.
     */
    @Test
    void testGetCiBuildTemplatesForNonAggregatedMvnProject() {
        final List<AnalyzedSource> sources = List
                .of(AnalyzedMavenSource.builder().modules(Collections.emptySet()).isRootProject(false).build());
        final List<FileTemplate> templates = testee(sources).getGlobalTemplates();
        assertFalse(() -> templates.stream()
                .anyMatch(template -> template.getPathInProject().equals(Path.of(".github/workflows/ci-build.yml"))));
        verify(this.loggerMock).warn(
                "W-PK-CORE-91: For this project structure project keeper does not know how to configure ci-build. Please create the required actions on your own.");
    }

    @Test
    void testReleaseWorkflow() {
        final List<AnalyzedSource> sources = getMavenSourceWithModules(Set.of());
        final List<FileTemplate> templates = testee(sources).getGlobalTemplates();
        assertContainsTemplate(templates, ".github/workflows/release.yml");
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testProjectKeeperVerifyYml(final boolean hasNpmModule) {
        final List<AnalyzedSource> sources = List.of(AnalyzedMavenSource.builder() //
                .modules(Collections.emptySet()).isRootProject(false).build());
        final List<FileTemplate> actual = testee(hasNpmModule, sources).getGlobalTemplates();
        final Optional<FileTemplate> template = findTemplate(actual, ".github/workflows/project-keeper-verify.yml");
        assertTrue(template.isPresent());
        final String content = template.get().getContent();
        assertThat(content, allOf(not(containsString("$npmSetup")), //
                not(containsString("$installNode")), //
                containsString("uses: actions/setup-node@v4\n" +
                        "        if: ${{ " + hasNpmModule + " }}")));
    }

    @Test
    void testCiBuildNextJava() {
        final List<AnalyzedSource> sources = List
                .of(AnalyzedMavenSource.builder().javaVersion("17").isRootProject(true).modules(emptySet()).build());
        final List<FileTemplate> actual = testee(false, sources).getGlobalTemplates();
        final Optional<FileTemplate> template = findTemplate(actual, ".github/workflows/ci-build.yml");
        assertTrue(template.isPresent());
        final String content = template.get().getContent();
        System.out.println(content);
        assertThat(content, allOf(containsString("java-version: '21',"), containsString("-Djava.version=21")));
    }

    @ParameterizedTest
    @CsvSource({ //
            "DEFAULT, .settings/org.eclipse.jdt.ui.prefs", //
            "JAR_ARTIFACT, src/assembly/all-dependencies.xml", //
            "LOMBOK, lombok.config", //
            "NATIVE_IMAGE, src/main/reflect-config.json"//
    })
    void testGetTemplatesPerSource(final ProjectKeeperModule module, final String expectedTemplate) {
        final AnalyzedMavenSource source = AnalyzedMavenSource.builder().modules(Set.of(module)).build();
        final List<FileTemplate> templates = testee(List.of(source)).getTemplatesForSource(source);
        assertContainsTemplate(templates, expectedTemplate);
    }

    @ParameterizedTest
    @CsvSource({ "8, 1.8", "11, 11", "17, 17", "21, 21" })
    void testSettingsOrgEclipseJdtUiPrefs(final String javaVersion, final String expected) {
        final AnalyzedMavenSource source = AnalyzedMavenSource.builder() //
                .modules(Set.of(ProjectKeeperModule.DEFAULT)) //
                .javaVersion(javaVersion) //
                .build();
        final List<FileTemplate> templates = testee(List.of(source)).getTemplatesForSource(source);
        final FileTemplate template = findTemplate(templates, ".settings/org.eclipse.jdt.ui.prefs").get();
        template.getContent().contains("org.eclipse.jdt.core.compiler.codegen.targetPlatform=" + expected);
    }

    private void assertContainsTemplate(final List<FileTemplate> templates, final String pathInProject) {
        assertTrue(findTemplate(templates, pathInProject).isPresent(), "Expected template " + pathInProject
                + " to exist but templates the generated templates did not contain it.");
    }

    private Optional<FileTemplate> findTemplate(final List<FileTemplate> templates, final String pathInProject) {
        return templates.stream() //
                .filter(template -> template.getPathInProject().equals(Path.of(pathInProject))) //
                .findFirst();
    }
}
