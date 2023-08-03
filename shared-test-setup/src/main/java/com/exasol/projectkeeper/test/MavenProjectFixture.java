package com.exasol.projectkeeper.test;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperModule.values;
import static com.exasol.projectkeeper.shared.config.SourceType.MAVEN;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.exasol.projectkeeper.shared.config.*;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.ProjectKeeperConfigBuilder;

public class MavenProjectFixture {
    private static final String POM_XML = "pom.xml";
    private final Path projectDir;

    public MavenProjectFixture(final Path projectDir) {
        this.projectDir = projectDir;
    }

    public void gitInit() {
        try {
            Git.init().setDirectory(this.projectDir.toFile()).call().close();
        } catch (IllegalStateException | GitAPIException exception) {
            throw new AssertionError("Error running git init", exception);
        }
    }

    public void writePomWithOneDependency(final String pomVersion, final String dependencyVersion) {
        final TestMavenModel testMavenModel = new TestMavenModel();
        testMavenModel.setVersion(pomVersion);
        testMavenModel.addDependency("error-reporting-java", "com.exasol", "compile", dependencyVersion);
        testMavenModel.configureAssemblyPluginFinalName();
        writePom(testMavenModel);
    }

    public void writeDefaultPom() {
        writePom(new TestMavenModel());
    }

    public void writePom(final TestMavenModel model) {
        model.writeAsPomToProject(this.projectDir);
    }

    public ProjectKeeperConfig.ProjectKeeperConfigBuilder getConfigWithAllModulesBuilder() {
        return ProjectKeeperConfig.builder()
                .sources(List.of(Source.builder().modules(Set.of(values())).type(MAVEN).path(Path.of(POM_XML)).build()))
                .versionConfig(new VersionFromSource(Path.of(POM_XML)));
    }

    public ProjectKeeperConfig.ProjectKeeperConfigBuilder getConfigWithoutModulesBuilder() {
        return ProjectKeeperConfig.builder()
                .sources(List.of(Source.builder().type(MAVEN).path(Path.of(POM_XML)).build()))
                .versionConfig(new VersionFromSource(Path.of(POM_XML)));
    }

    public void writeConfig(final ProjectKeeperConfigBuilder configBuilder) {
        new ProjectKeeperConfigWriter().writeConfig(configBuilder.build(), this.projectDir);
    }
}
