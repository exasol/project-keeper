package com.exasol.projectkeeper.validators.dependencies;

import static com.exasol.projectkeeper.shared.config.SourceType.MAVEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.apache.maven.model.Repository;
import org.junit.jupiter.api.*;

import com.exasol.projectkeeper.ProjectKeeperAbstractMavenIT;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Builder;
import com.exasol.projectkeeper.shared.config.Source;
import com.exasol.projectkeeper.test.MavenProjectFixture;
import com.exasol.projectkeeper.test.TestMavenModel;

@Tag("integration")
// [itest->dsn~dependency.md-file-validator~1]
// [itest->dsn~reading-project-dependencies~1]
class DependenciesValidatorIT extends ProjectKeeperAbstractMavenIT {

    private MavenProjectFixture fixture;
    private Path dependenciesFile;

    @BeforeEach
    void setup() {
        this.fixture = new MavenProjectFixture(this.projectDir);
        this.fixture.gitInit();
        this.dependenciesFile = this.projectDir.resolve("dependencies.md");
    }

    @Test
    void testVerify() throws IOException {
        createExamplePomFile();
        this.fixture.writeConfig(createConfigWithNoModules());
        runFix();
        Files.deleteIfExists(this.dependenciesFile);
        final String output = assertInvalidAndGetOutput();
        assertThat(output, containsString("E-PK-CORE-50: This project does not have a dependencies.md file."));
    }

    @Test
    void testWrongContent() throws IOException {
        createExamplePomFile();
        this.fixture.writeConfig(createConfigWithNoModules());
        runFix();
        Files.writeString(this.dependenciesFile, "wrong content");
        final String output = assertInvalidAndGetOutput();
        assertThat(output, containsString("E-PK-CORE-53: The dependencies.md file has outdated content."));
    }

    @Test
    void testWrongContentWithNonDefaultRepository() throws IOException {
        createExamplePomFileWithNonDefaultRepo();
        this.fixture.writeConfig(createConfigWithNoModules());
        runFix();
        Files.writeString(this.dependenciesFile, "wrong content");
        final String output = assertInvalidAndGetOutput();
        assertThat(output, containsString("E-PK-CORE-53: The dependencies.md file has outdated content."));
    }

    // [itest -> dsn~dependency.md-file-validator-excludes-implicit-plugins~1]
    @Test
    void testFix() throws IOException {
        createExamplePomFile();
        this.fixture.writeConfig(createConfigWithNoModules());
        runFix();
        final String dependenciesFileContent = Files.readString(this.dependenciesFile);
        assertAll(//
                () -> assertThat(dependenciesFileContent, containsString("error-reporting-java")),
                () -> assertThat(dependenciesFileContent, containsString("SonarQube Scanner for Maven")),
                () -> assertThat(dependenciesFileContent, not(containsString("Maven Clean Plugin")))//
        );
    }

    private Builder createConfigWithNoModules() {
        return ProjectKeeperConfig.builder().sources(
                List.of(Source.builder().modules(Collections.emptySet()).type(MAVEN).path(Path.of("pom.xml")).build()));
    }

    @Test
    void testBrokenLinkReplacing() throws IOException {
        createExamplePomFile();
        this.fixture.writeConfig(ProjectKeeperConfig.builder()
                .sources(List.of(
                        Source.builder().modules(Collections.emptySet()).type(MAVEN).path(Path.of("pom.xml")).build()))
                .linkReplacements(
                        List.of("https://www.apache.org/licenses/LICENSE-2.0.txt|https://my-replacement.de")));
        runFix();
        final String dependenciesFileContent = Files.readString(this.dependenciesFile);
        assertThat(dependenciesFileContent, containsString("https://my-replacement.de"));
    }

    private void createExamplePomFile() throws IOException {
        final TestMavenModel pomModel = new TestMavenModel();
        pomModel.addDependency("error-reporting-java", "com.exasol", "compile", "1.0.1");
        pomModel.configureAssemblyPluginFinalName();
        pomModel.writeAsPomToProject(this.projectDir);
    }

    private void createExamplePomFileWithNonDefaultRepo() throws IOException {
        final TestMavenModel pomModel = new TestMavenModel();
        pomModel.configureAssemblyPluginFinalName();
        final Repository exasolRepo = new Repository();
        exasolRepo.setUrl("https://repo1.maven.org/maven2");
        exasolRepo.setId("maven.exasol.com");
        pomModel.addRepository(exasolRepo);
        pomModel.addDependency("exasol-jdbc", "com.exasol", "compile", "24.1.0");
        pomModel.writeAsPomToProject(this.projectDir);
    }
}
