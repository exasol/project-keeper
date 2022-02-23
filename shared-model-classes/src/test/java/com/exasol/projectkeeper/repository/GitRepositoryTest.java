package com.exasol.projectkeeper.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.URIish;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GitRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void testGetTags() throws GitAPIException, IOException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            makeCommitAndTag(git, 1, false);
            git.branchCreate().setName("other").call();
            makeCommitAndTag(git, 2, false);
            git.checkout().setName("other").call();
            makeCommitAndTag(git, 3, false);
            final GitRepository repository = new GitRepository(this.tempDir);
            final List<TaggedCommit> result = repository.getTagsInCurrentBranch();
            final List<String> tagNames = result.stream().map(TaggedCommit::getTag).collect(Collectors.toList());
            assertThat(tagNames, contains("tag3", "tag1"));
        }
    }

    /**
     * Test get a lightweight git tag. Lightweight tags are among others created by github releases. For details see
     * https://git-scm.com/book/en/v2/Git-Internals-Git-References.
     */
    @Test
    void testGetLightweightTag() throws GitAPIException, IOException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            makeCommitAndTag(git, 1, true);
            final GitRepository repository = new GitRepository(this.tempDir);
            final List<TaggedCommit> result = repository.getTagsInCurrentBranch();
            final List<String> tagNames = result.stream().map(TaggedCommit::getTag).collect(Collectors.toList());
            assertThat(tagNames, contains("tag1"));
        }
    }

    @Test
    void testGetTagsFromDetachedHead() throws GitAPIException, IOException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            makeCommitAndTag(git, 1, true);
            final RevCommit secondCommit = makeCommitAndTag(git, 2, true);
            git.checkout().setName(secondCommit.getName()).call();
            final GitRepository repository = new GitRepository(this.tempDir);
            final List<TaggedCommit> result = repository.getTagsInCurrentBranch();
            final List<String> tagNames = result.stream().map(TaggedCommit::getTag).collect(Collectors.toList());
            assertThat(tagNames, contains("tag2", "tag1"));
        }
    }

    @Test
    void testGetTagsInNoGitDirectory() {
        final GitRepository repository = new GitRepository(this.tempDir);
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                repository::getTagsInCurrentBranch);
        assertThat(exception.getMessage(), startsWith("E-PK-32"));
    }

    private RevCommit makeCommitAndTag(final Git git, final int counter, final boolean lightweight)
            throws IOException, GitAPIException {
        Files.writeString(this.tempDir.resolve("myFile.txt"), counter + "");
        git.add().addFilepattern("myFile.txt").call();
        final RevCommit commit = git.commit().setMessage(counter + ". commit").call();
        git.tag().setName("tag" + counter).setAnnotated(!lightweight).call();
        return commit;
    }

    @Test
    void testReadFileAtCommit() throws GitAPIException, IOException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call()) {
            final Path testFile = this.tempDir.resolve("myFile.txt");
            final String fileContent = "some string";
            Files.writeString(testFile, fileContent);
            git.add().addFilepattern("myFile.txt").call();
            final RevCommit initialCommit = git.commit().setMessage("initial commit").call();
            Files.delete(testFile);
            git.add().addFilepattern("myFile.txt").call();
            git.commit().setMessage("removed file").call();

            final GitRepository repository = new GitRepository(this.tempDir);
            final String result = repository.readFileAtCommit(Path.of("myFile.txt"), new GitCommit(initialCommit));
            assertThat(result, equalTo(fileContent));
        }
    }

    @Test
    void testReadFileAtCommitInSubDir() throws GitAPIException, IOException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call()) {
            final Path subdir = this.tempDir.resolve("subdir");
            Files.createDirectory(subdir);
            final Path testFile = subdir.resolve("myFile.txt");
            final String fileContent = "some string";
            Files.writeString(testFile, fileContent);
            git.add().addFilepattern("subdir/myFile.txt").call();
            final RevCommit initialCommit = git.commit().setMessage("initial commit").call();
            Files.delete(testFile);
            git.add().addFilepattern("subdir/myFile.txt").call();
            git.commit().setMessage("removed file").call();
            final GitRepository repository = new GitRepository(this.tempDir);
            final String result = repository.readFileAtCommit(Path.of("subdir/myFile.txt"),
                    new GitCommit(initialCommit));
            assertThat(result, equalTo(fileContent));
        }
    }

    @Test
    void testReadFileAtCommitInNoGitDirectory() {
        final GitRepository repository = new GitRepository(this.tempDir);
        final Path testPath = Path.of("test.md");
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> repository.readFileAtCommit(testPath, null));
        assertThat(exception.getMessage(), startsWith("E-PK-32"));
    }

    @Test
    void testReadFileAtCommitWithMissingFile() throws GitAPIException, IOException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            final GitCommit commit = new GitCommit(makeCommitAndTag(git, 1, false));
            final GitRepository repository = new GitRepository(this.tempDir);
            final Path nonExistingPath = Path.of("nonExistingFile.md");
            final FileNotFoundException exception = assertThrows(FileNotFoundException.class,
                    () -> repository.readFileAtCommit(nonExistingPath, commit));
            assertThat(exception.getMessage(), startsWith("E-PK-35"));
        }
    }

    @Test
    void testGetRepoName() throws GitAPIException, IOException, URISyntaxException {
        try (final Git git = Git.init().setDirectory(this.tempDir.toFile()).call();) {
            git.remoteAdd().setName("origin")
                    .setUri(new URIish("git@github.com:exasol/project-keeper-maven-plugin.git")).call();
            final GitRepository repository = new GitRepository(this.tempDir);
            assertThat(repository.getRepoNameFromRemote().orElseThrow(), equalTo("project-keeper-maven-plugin"));
        }
    }
}