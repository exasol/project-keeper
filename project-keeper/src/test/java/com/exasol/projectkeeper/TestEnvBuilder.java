package com.exasol.projectkeeper;

import java.io.File;
import java.nio.file.Path;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;

public class TestEnvBuilder {
    private static final String PROJECT_ROOT_OFFSET = "../";
    private static final File PARENT_POM = Path.of(PROJECT_ROOT_OFFSET, "parent-pom/pom.xml").toFile();
    public static final String CURRENT_VERSION = MavenProjectVersionGetter.getProjectRevision(PARENT_POM.toPath());
    private static final File SHARED_MODEL = Path
            .of(PROJECT_ROOT_OFFSET,
                    "shared-model-classes/target/project-keeper-shared-model-classes-" + CURRENT_VERSION + ".jar")
            .toFile();
    private static final File JAVA_CRAWLER = Path
            .of(PROJECT_ROOT_OFFSET,
                    "maven-project-crawler/target/project-keeper-java-project-crawler-" + CURRENT_VERSION + ".jar")
            .toFile();
    private static final File SHARED_MODEL_POM = Path.of(PROJECT_ROOT_OFFSET, "shared-model-classes/.flattened-pom.xml")
            .toFile();
    private static final File JAVA_CRAWLER_POM = Path
            .of(PROJECT_ROOT_OFFSET, "maven-project-crawler/.flattened-pom.xml").toFile();
    private static MavenIntegrationTestEnvironment mavenIntegrationTestEnvironment;

    public static MavenIntegrationTestEnvironment getTestEnv() {
        if (mavenIntegrationTestEnvironment == null) {
            mavenIntegrationTestEnvironment = createTestEnv();
        }
        return mavenIntegrationTestEnvironment;
    }

    private static MavenIntegrationTestEnvironment createTestEnv() {
        final MavenIntegrationTestEnvironment testEnv = new MavenIntegrationTestEnvironment();
        testEnv.installWithoutJar(PARENT_POM);
        testEnv.installPlugin(SHARED_MODEL, SHARED_MODEL_POM, "project-keeper-shared-model-classes", "com.exasol",
                CURRENT_VERSION);
        testEnv.installPlugin(JAVA_CRAWLER, JAVA_CRAWLER_POM, "project-keeper-java-project-crawler", "com.exasol",
                CURRENT_VERSION);
        return testEnv;
    }
}
