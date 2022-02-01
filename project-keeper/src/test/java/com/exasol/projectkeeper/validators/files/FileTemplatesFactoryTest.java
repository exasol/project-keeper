package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.ProjectKeeperModule.MAVEN_CENTRAL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;

class FileTemplatesFactoryTest {
    @TempDir
    Path tempDir;

    @Test
    void testGetCiBuildTemplatesForMavenProject() {
        final Set<ProjectKeeperModule> modules = Collections.emptySet();
        final List<ProjectKeeperConfig.Source> sources = getMavenSourceWithModules(this.tempDir, modules);
        final List<FileTemplate> templates = new FileTemplatesFactory(mock(Logger.class))
                .getGlobalTemplates(this.tempDir, sources);
        assertContainsTemplate(templates, ".github/workflows/ci-build.yml");
    }

    private List<ProjectKeeperConfig.Source> getMavenSourceWithModules(final Path tempDir,
            final Set<ProjectKeeperModule> modules) {
        return List.of(ProjectKeeperConfig.Source.builder().type(ProjectKeeperConfig.SourceType.MAVEN).modules(modules)
                .path(tempDir.resolve("pom.xml")).build());
    }

    /*
     * In this test case we create an unsupported project (maven build in nested project but not aggregator pom in the
     * root). For those projects project-keeper can't create CI-templates. So we expect a warning.
     */
    @Test
    void testGetCiBuildTemplatesForNonAggregatedMvnProject() {
        final Logger logger = mock(Logger.class);
        final List<ProjectKeeperConfig.Source> sources = List
                .of(ProjectKeeperConfig.Source.builder().type(ProjectKeeperConfig.SourceType.MAVEN)
                        .modules(Collections.emptySet()).path(this.tempDir.resolve("nested/pom.xml")).build());
        final List<FileTemplate> templates = new FileTemplatesFactory(logger).getGlobalTemplates(this.tempDir, sources);
        assertFalse(() -> templates.stream()
                .anyMatch(template -> template.getPathInProject().equals(Path.of(".github/workflows/ci-build.yml"))));
        verify(logger).warn(
                "W-PK-CORE-91: For this project structure project keeper does not know how to configure ci-build. Please create the required actions on your own.");
    }

    @Test
    void testGetMavenCentralCiBuildTemplatesForMavenProject() {
        final List<ProjectKeeperConfig.Source> sources = getMavenSourceWithModules(this.tempDir, Set.of(MAVEN_CENTRAL));
        final List<FileTemplate> templates = new FileTemplatesFactory(mock(Logger.class))
                .getGlobalTemplates(this.tempDir, sources);
        assertContainsTemplate(templates, ".github/workflows/release_droid_release_on_maven_central.yml");
    }

    @ParameterizedTest
    @CsvSource({ //
            "DEFAULT, .settings/org.eclipse.jdt.ui.prefs", //
            "JAR_ARTIFACT, src/assembly/all-dependencies.xml", //
            "LOMBOK, lombok.config"//
    })
    void testGetTemplatesPerSource(final ProjectKeeperModule module, final String expectedTemplate) {
        final ProjectKeeperConfig.Source source = ProjectKeeperConfig.Source.builder()
                .type(ProjectKeeperConfig.SourceType.MAVEN).modules(Set.of(module))
                .path(this.tempDir.resolve("pom.xml")).build();
        final List<FileTemplate> templates = new FileTemplatesFactory(mock(Logger.class)).getTemplatesForSource(source);
        assertContainsTemplate(templates, expectedTemplate);
    }

    private void assertContainsTemplate(final List<FileTemplate> templates, final String pathInProject) {
        assertTrue(() -> templates.stream()
                .anyMatch(template -> template.getPathInProject().equals(Path.of(pathInProject))));
    }
}