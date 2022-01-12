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
            makeRelease(git, "0.1.0", myContent);
            final Optional<String> result = runLatestReleasePomFileReader();
            assertThat(result.orElseThrow(), equalTo(myContent));
        }
    }

    @Test
    void testReadingWithNonReleaseTag() throws IOException, GitAPIException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            final String myContent = "myContent";
            makeRelease(git, "0.1.0", myContent);
            makeRelease(git, "jutATag", "otherContent");
            final Optional<String> result = runLatestReleasePomFileReader();
            assertThat(result.orElseThrow(), equalTo(myContent));
        }
    }

    @Test
    void testReadingWithTwoRelease() throws IOException, GitAPIException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            final String myContent = "myContent";
            makeRelease(git, "0.1.0", "original content");
            makeRelease(git, "0.2.0", myContent);
            final Optional<String> result = runLatestReleasePomFileReader();
            assertThat(result.orElseThrow(), equalTo(myContent));
        }
    }

    @Test
    void testReadingWithNoCommits() throws GitAPIException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            final Optional<String> result = runLatestReleasePomFileReader();
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testReadingNoRelease() throws GitAPIException, IOException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            makeRelease(git, "jutATag", "otherContent");
            final Optional<String> result = runLatestReleasePomFileReader();
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testCurrentReleaseIsSkipped() throws IOException, GitAPIException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            final String originalContent = "original content";
            makeRelease(git, "0.1.0", originalContent);
            makeRelease(git, CURRENT_VERSION, "other content");
            final Optional<String> result = runLatestReleasePomFileReader();
            assertThat(result.orElseThrow(), equalTo(originalContent));
        }
    }

    private Optional<String> runLatestReleasePomFileReader() {
        return new LastReleasePomFileReader().readLatestReleasesPomFile(this.tempDir, CURRENT_VERSION);
    }

    private void makeRelease(final Git git, final String name, final String content)
            throws IOException, GitAPIException {
        final Path testFile = this.tempDir.resolve("pom.xml");
        Files.writeString(testFile, content);
        git.add().addFilepattern("pom.xml").call();
        git.commit().setMessage("commit for release " + name).call();
        git.tag().setName(name).call();
    }
}