package com.exasol.projectkeeper.test;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperModule.values;
import static com.exasol.projectkeeper.shared.config.SourceType.MAVEN;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import com.exasol.projectkeeper.shared.config.*;

public class MavenProjectFixture extends BaseProjectFixture {
    private static final String POM_XML = "pom.xml";

    public MavenProjectFixture(final Path projectDir) {
        super(projectDir);
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

    public ProjectKeeperConfig.Builder getConfigWithAllModulesBuilder() {
        return ProjectKeeperConfig.builder()
                .sources(List.of(Source.builder().modules(Set.of(values())).type(MAVEN).path(Path.of(POM_XML)).build()))
                .versionConfig(new VersionFromSource(Path.of(POM_XML)));
    }

    public ProjectKeeperConfig.Builder getConfigWithoutModulesBuilder() {
        return ProjectKeeperConfig.builder()
                .sources(List.of(Source.builder().type(MAVEN).path(Path.of(POM_XML)).build()))
                .versionConfig(new VersionFromSource(Path.of(POM_XML)));
    }

}
