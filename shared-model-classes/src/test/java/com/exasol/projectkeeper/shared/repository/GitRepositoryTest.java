package com.exasol.projectkeeper.shared.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.URIish;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GitRepositoryTest {

    @TempDir
    Path tempDir;
    private GitRepository repository;

    @AfterEach
    void cleanup() {
        if (this.repository != null) {
            this.repository.close();
        }
    }

    @Test
    void testGetTags() throws GitAPIException, IOException {
        try (final Git git = gitInit();) {
            makeCommitAndTag(git, 1, false);
            git.branchCreate().setName("other").call();
            makeCommitAndTag(git, 2, false);
            git.checkout().setName("other").call();
            makeCommitAndTag(git, 3, false);
            this.repository = openRepo(this.tempDir);
            final List<TaggedCommit> result = this.repository.getTagsInCurrentBranch();
            final List<String> tagNames = result.stream().map(TaggedCommit::getTag).toList();
            assertThat(tagNames, contains("tag3", "tag1"));
        }
    }

    /**
     * Test get a lightweight git tag. Lightweight tags are among others created by github releases. For details see
     * https://git-scm.com/book/en/v2/Git-Internals-Git-References.
     */
    @Test
    void testGetLightweightTag() throws GitAPIException, IOException {
        try (final Git git = gitInit();) {
            makeCommitAndTag(git, 1, true);
            this.repository = openRepo(this.tempDir);
            final List<TaggedCommit> result = this.repository.getTagsInCurrentBranch();
            final List<String> tagNames = result.stream().map(TaggedCommit::getTag).toList();
            assertThat(tagNames, contains("tag1"));
        }
    }

    @Test
    void testGetTagsFromDetachedHead() throws GitAPIException, IOException {
        try (final Git git = gitInit();) {
            makeCommitAndTag(git, 1, true);
            final RevCommit secondCommit = makeCommitAndTag(git, 2, true);
            git.checkout().setName(secondCommit.getName()).call();
            this.repository = openRepo(this.tempDir);
            final List<TaggedCommit> result = this.repository.getTagsInCurrentBranch();
            final List<String> tagNames = result.stream().map(TaggedCommit::getTag).toList();
            assertThat(tagNames, contains("tag2", "tag1"));
        }
    }

    @Test
    void testGetTagsInNoGitDirectory() {
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> openRepo(this.tempDir));
        assertThat(exception.getMessage(), startsWith("E-PK-SMC-32"));
    }

    private RevCommit makeCommitAndTag(final Git git, final int counter, final boolean lightweight)
            throws IOException, GitAPIException {
        return makeCommitAndTag(git, counter, lightweight, "tag" + counter);
    }

    private RevCommit makeCommitAndTag(final Git git, final int counter, final boolean lightweight,
            final String tagName) throws IOException, GitAPIException {
        final RevCommit commit = makeCommit(git, counter);
        makeTag(git, tagName, lightweight);
        return commit;
    }

    private RevCommit makeCommit(final Git git, final int counter)
            throws IOException, GitAPIException {
        Files.writeString(this.tempDir.resolve("myFile.txt"), counter + "");
        git.add().addFilepattern("myFile.txt").call();
        return git.commit().setMessage(counter + ". commit").call();
    }

    private void makeTag(final Git git, final String tagName, final boolean lightweight)
            throws GitAPIException, ConcurrentRefUpdateException, InvalidTagNameException, NoHeadException {
        git.tag().setName(tagName).setAnnotated(!lightweight).call();
    }

    @Test
    void testReadFileAtCommit() throws GitAPIException, IOException {
        final Path gitDir = this.tempDir.resolve("repo");
        Files.createDirectory(gitDir);
        try (final Git git = Git.init().setDirectory(gitDir.toFile()).call()) {
            final Path testFile = gitDir.resolve("myFile.txt");
            final String fileContent = "some string";
            Files.writeString(testFile, fileContent);
            git.add().addFilepattern("myFile.txt").call();
            final RevCommit initialCommit = git.commit().setMessage("initial commit").call();
            Files.delete(testFile);
            git.add().addFilepattern("myFile.txt").call();
            git.commit().setMessage("removed file").call();
            this.repository = openRepo(gitDir);
            final Path destinationFile = this.tempDir.resolve("result");
            this.repository.extractFileFromCommit(Path.of("myFile.txt"), new GitCommit(initialCommit), destinationFile);
            final String result = Files.readString(destinationFile);
            assertThat(result, equalTo(fileContent));
        }
    }

    @Test
    void testReadFileAtCommitInSubDir() throws GitAPIException, IOException {
        final Path gitRepo = this.tempDir.resolve("repo");
        Files.createDirectory(gitRepo);
        try (final Git git = Git.init().setDirectory(gitRepo.toFile()).call()) {
            final Path subdir = gitRepo.resolve("subdir");
            Files.createDirectory(subdir);
            final Path testFile = subdir.resolve("myFile.txt");
            final String fileContent = "some string";
            Files.writeString(testFile, fileContent);
            git.add().addFilepattern("subdir/myFile.txt").call();
            final RevCommit initialCommit = git.commit().setMessage("initial commit").call();
            Files.delete(testFile);
            git.add().addFilepattern("subdir/myFile.txt").call();
            git.commit().setMessage("removed file").call();
            this.repository = openRepo(gitRepo);
            final Path destinationFile = this.tempDir.resolve("result");
            this.repository.extractFileFromCommit(Path.of("subdir/myFile.txt"), new GitCommit(initialCommit),
                    destinationFile);
            final String result = Files.readString(destinationFile);
            assertThat(result, equalTo(fileContent));
        }
    }

    @Test
    void testReadFileAtCommitWithMissingFile() throws GitAPIException, IOException {
        try (final Git git = gitInit()) {
            final GitCommit commit = new GitCommit(makeCommitAndTag(git, 1, false));
            this.repository = openRepo(this.tempDir);
            final Path nonExistingPath = Path.of("nonExistingFile.md");
            final Path targetFile = this.tempDir.resolve("targetFile");
            final FileNotFoundException exception = assertThrows(FileNotFoundException.class,
                    () -> this.repository.extractFileFromCommit(nonExistingPath, commit, targetFile));
            assertThat(exception.getMessage(), startsWith("E-PK-SMC-35"));
        }
    }

    @Test
    void testGetRepoName() throws GitAPIException, URISyntaxException {
        try (final Git git = gitInit()) {
            git.remoteAdd().setName("origin")
                    .setUri(new URIish("git@github.com:exasol/project-keeper-maven-plugin.git")).call();
            this.repository = openRepo(this.tempDir);
            assertThat(this.repository.getRepoNameFromRemote().orElseThrow(), equalTo("project-keeper-maven-plugin"));
        }
    }

    @Test
    @SuppressWarnings("try") // auto-closeable resource git is never referenced in body of corresponding try statement
    void testFindLatestReleaseCommitNoCommit() throws GitAPIException {
        try (final Git git = gitInit()) {
            this.repository = openRepo(this.tempDir);
            assertTrue(this.repository.findLatestReleaseCommit(null).isEmpty());
        }
    }

    @Test
    void testFindLatestReleaseCommitNoTag() throws GitAPIException, IOException {
        try (final Git git = gitInit()) {
            this.repository = openRepo(this.tempDir);
            makeCommit(git, 1);
            assertTrue(this.repository.findLatestReleaseCommit(null).isEmpty());
        }
    }

    @Test
    void testFindLatestReleaseCommitNotMatchingTag() throws GitAPIException, IOException {
        try (final Git git = gitInit()) {
            this.repository = openRepo(this.tempDir);
            makeCommitAndTag(git, 1, false, "my-tag");
            assertTrue(this.repository.findLatestReleaseCommit(null).isEmpty());
        }
    }

    @Test
    void testFindLatestReleaseCommitMatchingTag() throws GitAPIException, IOException {
        try (final Git git = gitInit()) {
            this.repository = openRepo(this.tempDir);
            makeCommitAndTag(git, 1, false, "1.2.3");
            final Optional<TaggedCommit> commit = this.repository.findLatestReleaseCommit(null);
            assertAll(() -> assertTrue(commit.isPresent()), //
                    () -> assertThat(commit.get().getTag(), equalTo("1.2.3")));
        }
    }

    @Test
    void testFindLatestReleaseCommitMatchingAnnotatedTag() throws GitAPIException, IOException {
        try (final Git git = gitInit()) {
            this.repository = openRepo(this.tempDir);
            makeCommitAndTag(git, 1, true, "1.2.3");
            final Optional<TaggedCommit> commit = this.repository.findLatestReleaseCommit(null);
            assertAll(() -> assertTrue(commit.isPresent()), //
                    () -> assertThat(commit.get().getTag(), equalTo("1.2.3")));
        }
    }

    @Test
    void testFindLatestReleaseCommitMatchingTagIsCurrentTag()
            throws GitAPIException, IOException {
        try (final Git git = gitInit()) {
            this.repository = openRepo(this.tempDir);
            makeCommitAndTag(git, 1, false, "1.2.3");
            assertTrue(this.repository.findLatestReleaseCommit("1.2.3").isEmpty());
        }
    }

    @Test
    void testFindLatestReleaseCommitIgnoresCurrentVersion() throws GitAPIException, IOException {
        try (final Git git = gitInit()) {
            this.repository = openRepo(this.tempDir);
            makeCommitAndTag(git, 1, false, "1.2.2");
            makeCommitAndTag(git, 2, false, "1.2.3");
            final Optional<TaggedCommit> commit = this.repository.findLatestReleaseCommit("1.2.3");
            assertAll(() -> assertTrue(commit.isPresent()), //
                    () -> assertThat(commit.get().getTag(), equalTo("1.2.2")));
        }
    }

    @Test
    void testGetFileFromCommit() throws GitAPIException, IOException {
        try (final Git git = gitInit()) {
            this.repository = openRepo(this.tempDir);
            makeCommitAndTag(git, 1, false, "1.2.2");
            makeCommitAndTag(git, 2, false, "1.2.3");
            final String fileContent1 = this.repository.getFileFromCommit(Path.of("myFile.txt"),
                    this.repository.findLatestReleaseCommit("1.2.3").get().getCommit());
            final String fileContent2 = this.repository.getFileFromCommit(Path.of("myFile.txt"),
                    this.repository.findLatestReleaseCommit(null).get().getCommit());
            assertAll(() -> assertThat("file content tag 1", fileContent1, equalTo("1")), //
                    () -> assertThat("file content tag 1", fileContent2, equalTo("2")));
        }
    }

    private GitRepository openRepo(final Path path) {
        return GitRepository.open(path);
    }

    private Git gitInit() throws GitAPIException {
        return Git.init().setDirectory(this.tempDir.toFile()).call();
    }
}
