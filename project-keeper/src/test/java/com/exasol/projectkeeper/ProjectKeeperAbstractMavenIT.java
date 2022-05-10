package com.exasol.projectkeeper;

import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;

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
}
