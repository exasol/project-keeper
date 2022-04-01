package com.exasol.projectkeeper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.projectkeeper.validators.TestMavenModel;

public class ProjectKeeperAbstractMavenIT extends ProjectKeeperAbstractIT {
    private static MavenIntegrationTestEnvironment mavenIntegrationTestEnvironment;

    @BeforeAll
    static void beforeAll() {
        mavenIntegrationTestEnvironment = TestEnvBuilder.getTestEnv();
    }

    @Override
    protected Optional<Path> getMavenRepo() {
        return Optional.of(mavenIntegrationTestEnvironment.getLocalMavenRepository());
    }

    protected void writePomWithOneDependency(final String pomVersion, final String dependencyVersion)
            throws IOException {
        final TestMavenModel testMavenModel = new TestMavenModel();
        testMavenModel.setVersion(pomVersion);
        testMavenModel.addDependency("error-reporting-java", "com.exasol", "compile", dependencyVersion);
        testMavenModel.configureAssemblyPluginFinalName();
        testMavenModel.writeAsPomToProject(this.projectDir);
    }

}
