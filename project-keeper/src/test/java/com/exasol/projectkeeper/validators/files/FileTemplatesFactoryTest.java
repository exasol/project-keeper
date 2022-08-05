package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperModule.MAVEN_CENTRAL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;

class FileTemplatesFactoryTest {
    private static final String OWN_VERSION = "version";

    @Test
    void testGetCiBuildTemplatesForMavenProject() {
        final Set<ProjectKeeperModule> modules = Collections.emptySet();
        final List<AnalyzedSource> sources = getMavenSourceWithModules(modules);
        final List<FileTemplate> templates = new FileTemplatesFactory(mock(Logger.class), OWN_VERSION)
                .getGlobalTemplates(sources);
        assertContainsTemplate(templates, ".github/workflows/ci-build.yml");
    }

    private List<AnalyzedSource> getMavenSourceWithModules(final Set<ProjectKeeperModule> modules) {
        return List.of(AnalyzedMavenSource.builder().modules(modules).isRootProject(true).build());
    }

    /*
     * In this test case we create an unsupported project (maven build in nested project but not aggregator pom in the
     * root). For those projects project-keeper can't create CI-templates. So we expect a warning.
     */
    @Test
    void testGetCiBuildTemplatesForNonAggregatedMvnProject() {
        final Logger logger = mock(Logger.class);
        final List<AnalyzedSource> sources = List
                .of(AnalyzedMavenSource.builder().modules(Collections.emptySet()).isRootProject(false).build());
        final List<FileTemplate> templates = new FileTemplatesFactory(logger, OWN_VERSION).getGlobalTemplates(sources);
        assertFalse(() -> templates.stream()
                .anyMatch(template -> template.getPathInProject().equals(Path.of(".github/workflows/ci-build.yml"))));
        verify(logger).warn(
                "W-PK-CORE-91: For this project structure project keeper does not know how to configure ci-build. Please create the required actions on your own.");
    }

    @Test
    void testGetMavenCentralCiBuildTemplatesForMavenProject() {
        final List<AnalyzedSource> sources = getMavenSourceWithModules(Set.of(MAVEN_CENTRAL));
        final List<FileTemplate> templates = new FileTemplatesFactory(mock(Logger.class), OWN_VERSION)
                .getGlobalTemplates(sources);
        assertContainsTemplate(templates, ".github/workflows/release_droid_release_on_maven_central.yml");
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
        final List<FileTemplate> templates = new FileTemplatesFactory(mock(Logger.class), OWN_VERSION)
                .getTemplatesForSource(source);
        assertContainsTemplate(templates, expectedTemplate);
    }

    private void assertContainsTemplate(final List<FileTemplate> templates, final String pathInProject) {
        assertTrue(
                () -> templates.stream()
                        .anyMatch(template -> template.getPathInProject().equals(Path.of(pathInProject))),
                "Expected template " + pathInProject
                        + " to exist but templates the generated templates did not contain it.");
    }
}