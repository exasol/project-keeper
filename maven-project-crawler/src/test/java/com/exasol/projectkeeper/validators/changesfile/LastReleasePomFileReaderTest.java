package com.exasol.projectkeeper.validators.changesfile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.apache.maven.model.Parent;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.TestMavenModel;
import com.exasol.projectkeeper.TestRepo;

class LastReleasePomFileReaderTest {
    private static final String CURRENT_VERSION = "1.0.0";
    @TempDir
    Path tempDir;
    private Path tempRepo;
    private Path resultDir;

    @BeforeEach
    void setUp() throws IOException {
        this.tempRepo = this.tempDir.resolve("repo");
        Files.createDirectory(this.tempRepo);
        this.resultDir = this.tempDir.resolve("result");
        Files.createDirectory(this.resultDir);
    }

    @Test
    void testSimpleReading() throws IOException, GitAPIException {
        try (final TestRepo gitRepo = new TestRepo(this.tempRepo);) {
            final String myContent = makeRelease(gitRepo, "0.1.0", this.tempRepo);
            final Optional<Path> result = runLatestReleasePomFileReader(this.tempRepo);
            assertAll(//
                    () -> assertThat(Files.readString(result.orElseThrow()), equalTo(myContent)),
                    () -> assertTrue(Files.exists(this.resultDir.resolve("parent/pom.xml")))//
            );
        }
    }

    /**
     * In this test we check a setup where the project directory is in a subfolder of the project directory.
     */
    @Test
    void testWithNestedProject() throws IOException, GitAPIException {
        try (final TestRepo gitRepo = new TestRepo(this.tempRepo)) {
            final Path subDir = this.tempRepo.resolve("sub-dir");
            Files.createDirectory(subDir);
            final String myContent = makeRelease(gitRepo, "0.1.0", subDir);
            final Optional<Path> result = runLatestReleasePomFileReader(subDir);
            assertThat(Files.readString(result.orElseThrow()), equalTo(myContent));
        }
    }

    @Test
    void testReadingWithNonReleaseTag() throws IOException, GitAPIException {
        try (final TestRepo gitRepo = new TestRepo(this.tempRepo)) {
            final String myContent = makeRelease(gitRepo, "0.1.0", this.tempRepo);
            makeRelease(gitRepo, "jutATag", this.tempRepo);
            final Optional<Path> result = runLatestReleasePomFileReader(this.tempRepo);
            assertThat(Files.readString(result.orElseThrow()), equalTo(myContent));
        }
    }

    @Test
    void testReadingWithTwoRelease() throws IOException, GitAPIException {
        try (final TestRepo gitRepo = new TestRepo(this.tempRepo)) {
            makeRelease(gitRepo, "0.1.0", this.tempRepo);
            final String myContent = makeRelease(gitRepo, "0.2.0", this.tempRepo);
            final Optional<Path> result = runLatestReleasePomFileReader(this.tempRepo);
            assertThat(Files.readString(result.orElseThrow()), equalTo(myContent));
        }
    }

    @Test
    void testReadingWithNoCommits() throws GitAPIException {
        try (final TestRepo gitRepo = new TestRepo(this.tempRepo)) {
            final Optional<Path> result = runLatestReleasePomFileReader(this.tempRepo);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testReadingNoRelease() throws GitAPIException, IOException {
        try (final TestRepo gitRepo = new TestRepo(this.tempRepo)) {
            makeRelease(gitRepo, "jutATag", this.tempRepo);
            final Optional<Path> result = runLatestReleasePomFileReader(this.tempRepo);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testCurrentReleaseIsSkipped() throws IOException, GitAPIException {
        try (final TestRepo gitRepo = new TestRepo(this.tempRepo)) {
            final String originalContent = makeRelease(gitRepo, "0.1.0", this.tempRepo);
            makeRelease(gitRepo, CURRENT_VERSION, this.tempRepo);
            final Optional<Path> result = runLatestReleasePomFileReader(this.tempRepo);
            assertThat(Files.readString(result.orElseThrow()), equalTo(originalContent));
        }
    }

    @Test
    void testInvalidPom() throws IOException, GitAPIException {
        try (final TestRepo gitRepo = new TestRepo(this.tempRepo)) {
            Files.writeString(this.tempRepo.resolve("pom.xml"), "<invalid xml");
            gitRepo.addAll().commit().createTag("0.1.0");
            final IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> runLatestReleasePomFileReader(this.tempRepo));
            assertThat(exception.getMessage(),
                    equalTo("E-PK-MPC-60: Failed to read pom file of previous version for extracting it's parent."));
        }
    }

    @Test
    void testMissingParent() throws IOException, GitAPIException {
        try (final TestRepo gitRepo = new TestRepo(this.tempRepo)) {
            final TestMavenModel model = new TestMavenModel();
            final Parent parent = new Parent();
            parent.setRelativePath("./missing-parent.pom");
            model.setParent(parent);
            model.writeAsPomToProject(this.tempRepo);
            gitRepo.addAll().commit().createTag("0.1.0");
            final IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> runLatestReleasePomFileReader(this.tempRepo));
            assertThat(exception.getMessage(), startsWith("E-PK-MPC-61: Failed to extract the parent pom"));
        }
    }

    private Optional<Path> runLatestReleasePomFileReader(final Path projectDirectory) {
        return new LastReleasePomFileReader().extractLatestReleasesPomFile(projectDirectory, Path.of("pom.xml"),
                CURRENT_VERSION, this.resultDir);
    }

    private String makeRelease(final TestRepo git, final String name, final Path directory)
            throws IOException, GitAPIException {
        final Path dirForParent = directory.resolve("parent/");
        final TestMavenModel parentPom = new TestMavenModel();
        if (!Files.exists(dirForParent)) {
            Files.createDirectory(dirForParent);
        }
        parentPom.writeAsPomToProject(dirForParent);
        final TestMavenModel testMavenModel = new TestMavenModel();
        testMavenModel.setVersion(name);
        final Parent parentRef = new Parent();
        parentRef.setRelativePath("./parent/pom.xml");
        testMavenModel.setParent(parentRef);
        testMavenModel.writeAsPomToProject(directory);
        git.addAll().commit().createTag(name);
        return Files.readString(directory.resolve("pom.xml"));
    }
}