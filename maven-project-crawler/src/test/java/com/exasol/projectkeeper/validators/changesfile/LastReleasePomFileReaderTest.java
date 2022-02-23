package com.exasol.projectkeeper.validators.changesfile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LastReleasePomFileReaderTest {
    private static final String CURRENT_VERSION = "1.0.0";
    @TempDir
    Path tempDir;

    @Test
    void testSimpleReading() throws IOException, GitAPIException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            final String myContent = "myContent";
            makeRelease(git, "0.1.0", myContent, this.tempDir);
            final Optional<String> result = runLatestReleasePomFileReader(this.tempDir);
            assertThat(result.orElseThrow(), equalTo(myContent));
        }
    }

    /**
     * In this test we check a setup where the project directory is in a subfolder of the project directory.
     */
    @Test
    void testWithNestedProject() throws IOException, GitAPIException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            final Path subDir = this.tempDir.resolve("sub-dir");
            Files.createDirectory(subDir);
            final String myContent = "myContent";
            makeRelease(git, "0.1.0", myContent, subDir);
            final Optional<String> result = runLatestReleasePomFileReader(subDir);
            assertThat(result.orElseThrow(), equalTo(myContent));
        }
    }

    @Test
    void testReadingWithNonReleaseTag() throws IOException, GitAPIException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            final String myContent = "myContent";
            makeRelease(git, "0.1.0", myContent, this.tempDir);
            makeRelease(git, "jutATag", "otherContent", this.tempDir);
            final Optional<String> result = runLatestReleasePomFileReader(this.tempDir);
            assertThat(result.orElseThrow(), equalTo(myContent));
        }
    }

    @Test
    void testReadingWithTwoRelease() throws IOException, GitAPIException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            final String myContent = "myContent";
            makeRelease(git, "0.1.0", "original content", this.tempDir);
            makeRelease(git, "0.2.0", myContent, this.tempDir);
            final Optional<String> result = runLatestReleasePomFileReader(this.tempDir);
            assertThat(result.orElseThrow(), equalTo(myContent));
        }
    }

    @Test
    void testReadingWithNoCommits() throws GitAPIException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            final Optional<String> result = runLatestReleasePomFileReader(this.tempDir);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testReadingNoRelease() throws GitAPIException, IOException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            makeRelease(git, "jutATag", "otherContent", this.tempDir);
            final Optional<String> result = runLatestReleasePomFileReader(this.tempDir);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testCurrentReleaseIsSkipped() throws IOException, GitAPIException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            final String originalContent = "original content";
            makeRelease(git, "0.1.0", originalContent, this.tempDir);
            makeRelease(git, CURRENT_VERSION, "other content", this.tempDir);
            final Optional<String> result = runLatestReleasePomFileReader(this.tempDir);
            assertThat(result.orElseThrow(), equalTo(originalContent));
        }
    }

    private Optional<String> runLatestReleasePomFileReader(final Path projectDirectory) {
        return new LastReleasePomFileReader().readLatestReleasesPomFile(projectDirectory, CURRENT_VERSION);
    }

    private void makeRelease(final Git git, final String name, final String content, final Path directory)
            throws IOException, GitAPIException {
        final Path subDir = this.tempDir.relativize(directory);
        final Path testFile = directory.resolve("pom.xml");
        Files.writeString(testFile, content);
        final String pattern = subDir.resolve("pom.xml").toString();
        git.add().addFilepattern(pattern).call();
        git.commit().setMessage("commit for release " + name).call();
        git.tag().setName(name).call();
    }
}