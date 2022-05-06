package com.exasol.projectkeeper.test;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.SourceType.MAVEN;
import static com.exasol.projectkeeper.shared.config.ProjectKeeperModule.values;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.ProjectKeeperConfigBuilder;

public class MavenProjectFixture {
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

    public void writePomWithOneDependency(final String pomVersion, final String dependencyVersion) throws IOException {
        final TestMavenModel testMavenModel = new TestMavenModel();
        testMavenModel.setVersion(pomVersion);
        testMavenModel.addDependency("error-reporting-java", "com.exasol", "compile", dependencyVersion);
        testMavenModel.configureAssemblyPluginFinalName();
        writePom(testMavenModel);
    }

    public void writeDefaultPom() throws IOException {
        writePom(new TestMavenModel());
    }

    public void writePom(final TestMavenModel model) throws IOException {
        model.writeAsPomToProject(this.projectDir);
    }

    public ProjectKeeperConfig.ProjectKeeperConfigBuilder getConfigWithAllModulesBuilder() {
        return ProjectKeeperConfig.builder()
                .sources(List.of(ProjectKeeperConfig.Source.builder().modules(Set.of(values())).type(MAVEN)
                        .path(Path.of("pom.xml")).build()))
                .versionConfig(new ProjectKeeperConfig.VersionFromSource(Path.of("pom.xml")));
    }

    public void writeConfig(final ProjectKeeperConfigBuilder configBuilder) {
        new ProjectKeeperConfigWriter().writeConfig(configBuilder.build(), this.projectDir);
    }
}
