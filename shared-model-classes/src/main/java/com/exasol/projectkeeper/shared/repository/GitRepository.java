package com.exasol.projectkeeper.shared.repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.treewalk.TreeWalk;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.ExasolVersionMatcher;

/**
 * This gives access to a local git repository.
 */
public class GitRepository implements AutoCloseable {
    private final Path projectDirectory;
    private final Git git;

    private GitRepository(final Git git, final Path projectDirectory) {
        this.git = git;
        this.projectDirectory = projectDirectory;
    }

    /**
     * Open the given git repository and create a new instance of {@link GitRepository}.
     *
     * @param projectDirectory the projects directory.
     */
    public static GitRepository open(final Path projectDirectory) {
        return new GitRepository(openLocalGitRepository(projectDirectory), projectDirectory);
    }

    private static Git openLocalGitRepository(final Path projectDirectory) {
        try {
            return Git.open(projectDirectory.toFile());
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-SMC-32")
                    .message("Failed to open local git repository {{repository}}.")
                    .mitigation("If this is a new project you maybe need to create the git project using `git init`.")
                    .parameter("repository", projectDirectory.toString()).toString(), exception);
        }
    }

    public Optional<TaggedCommit> findLatestReleaseCommit(final String currentVersion) {
        final var exasolVersionMatcher = new ExasolVersionMatcher();
        return this.getTagsInCurrentBranch().stream()
                .filter(taggedCommit -> exasolVersionMatcher.isExasolStyleVersion(taggedCommit.getTag()))//
                .filter(taggedCommit -> !taggedCommit.getTag().equals(currentVersion))//
                .findFirst();
    }

    /**
     * Get the tags of the current branch ordered descending by commit date. If one commit has multiple tags, they are
     * returned sequential with no specified order.
     *
     * @return descending ordered list of tags
     */
    public List<TaggedCommit> getTagsInCurrentBranch() {
        try {
            final String currentBranch = this.git.getRepository().getFullBranch();
            validateBranchExists(currentBranch);
            return getTagsInBranch(this.git.getRepository(), currentBranch);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-SMC-33")
                            .message("Failed to retrieve latest tag from the local git repository.").toString(),
                    exception);
        }
    }

    private void validateBranchExists(final String currentBranch) {
        if (currentBranch == null) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-SMC-37").message("Could not get checked out branch of repository.")
                            .mitigation("Create a branch and check it out.").toString());
        }
    }

    private List<TaggedCommit> getTagsInBranch(final Repository repository, final String branchName)
            throws IOException {
        final ObjectId branch = repository.resolve(branchName);
        final Map<ObjectId, List<String>> tags = getTagsByTheIdOfTheCommitTheyPointTo(repository);
        try (final var commitWalker = new RevWalk(repository)) {
            try {
                commitWalker.markStart(commitWalker.parseCommit(branch));
            } catch (final NullPointerException exception) {
                // NullPointerException is thrown if the branch has no commits
                return Collections.emptyList();
            }
            commitWalker.sort(RevSort.COMMIT_TIME_DESC);
            return readTagsFromCommits(tags, commitWalker);
        }
    }

    private List<TaggedCommit> readTagsFromCommits(final Map<ObjectId, List<String>> tags, final RevWalk commitWalker) {
        final List<TaggedCommit> result = new ArrayList<>();
        for (final RevCommit commit : commitWalker) {
            if (tags.containsKey(commit.getId())) {
                final List<String> commitsTags = tags.get(commit.getId());
                for (final String tag : commitsTags) {
                    result.add(new TaggedCommit(commit, tag));
                }
            }
        }
        return result;
    }

    private Map<ObjectId, List<String>> getTagsByTheIdOfTheCommitTheyPointTo(final Repository repository)
            throws IOException {
        final Map<ObjectId, List<String>> taggedResources = new HashMap<>();
        for (final Ref tagRef : repository.getRefDatabase().getRefsByPrefix(Constants.R_TAGS)) {
            final String tagName = tagRef.getName().replace(Constants.R_TAGS, "");
            final ObjectId commitId = getTaggedCommitId(repository, tagRef);
            taggedResources.computeIfAbsent(commitId, commit -> new LinkedList<>()).add(tagName);
        }
        return taggedResources;
    }

    private ObjectId getTaggedCommitId(final Repository repository, final Ref tagRef) throws IOException {
        try (final var tagSearcher = new RevWalk(repository)) {
            final var revObject = tagSearcher.parseAny(tagRef.getObjectId());
            if (revObject instanceof RevCommit) {
                return revObject.getId();
            } else if (revObject instanceof RevTag) {
                return ((RevTag) revObject).getObject().getId();
            } else {
                throw new UnsupportedOperationException(ExaError.messageBuilder("F-PK-SMC-44")
                        .message("Unsupported tag target {{target class name}}.")
                        .parameter("target class name", revObject.getClass().getName()).ticketMitigation().toString());
            }
        }
    }

    /**
     * Read a specific file at a given commit and extract it to a target file.
     *
     * @param relativeFilePath file path
     * @param commit           commit
     * @param targetFile       path where this method writes the content to
     * @throws FileNotFoundException if the file does not exist in the repo
     */
    public void extractFileFromCommit(final Path relativeFilePath, final GitCommit commit, final Path targetFile)
            throws FileNotFoundException {
        try {
            extractFileFromCommit(relativeFilePath, this.git, commit.getCommit(), targetFile);
        } catch (final FileNotFoundException exception) {
            throw exception;
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-SMC-43")
                            .message("Failed to retrieve latest tag from the local git repository.").toString(),
                    exception);
        }
    }

    /**
     * Read a specific file at a given commit and return its content.
     *
     * @param relativeFilePath file path
     * @param commit           commit
     * @throws FileNotFoundException if the file does not exist in the repo
     */
    public String getFileFromCommit(final Path relativeFilePath, final GitCommit commit) throws FileNotFoundException {
        try {
            final var repository = this.git.getRepository();
            final ObjectId objectForVersionOfFile = findFile(commit.getCommit(), repository, relativeFilePath);
            try (final var reader = repository.newObjectReader()) {
                final var objectLoader = reader.open(objectForVersionOfFile);
                return new String(objectLoader.getBytes(), StandardCharsets.UTF_8);
            }
        } catch (final FileNotFoundException exception) {
            throw exception;
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-SMC-43")
                            .message("Failed to retrieve latest tag from the local git repository.").toString(),
                    exception);
        }
    }

    private void extractFileFromCommit(final Path relativeFilePath, final Git git, final RevCommit commit,
            final Path targetFile) throws IOException {
        final var repository = git.getRepository();
        final ObjectId objectForVersionOfFile = findFile(commit, repository, relativeFilePath);
        copyVersionOfFile(repository, objectForVersionOfFile, targetFile);
    }

    private void copyVersionOfFile(final Repository repository, final ObjectId objectForVersionOfFile,
            final Path targetFile) throws IOException {
        final Path targetDir = targetFile.getParent();
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        try (final var reader = repository.newObjectReader();
                final FileOutputStream outputStream = new FileOutputStream(targetFile.toFile());
                final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            final var objectLoader = reader.open(objectForVersionOfFile);
            objectLoader.copyTo(bufferedOutputStream);
        }
    }

    private ObjectId findFile(final RevCommit commit, final Repository repository, final Path expectedPath)
            throws IOException {
        final Path normalizedExpected = expectedPath.normalize();
        try (final TreeWalk treeWalk = new TreeWalk(repository)) {
            treeWalk.addTree(commit.getTree());
            treeWalk.setRecursive(true);
            while (treeWalk.next()) {
                final Path currentPath = Path.of(treeWalk.getPathString()).normalize();
                if (treeWalk.isSubtree()) {
                    if (normalizedExpected.startsWith(currentPath)) {
                        treeWalk.enterSubtree();
                    }
                } else {
                    if (normalizedExpected.equals(currentPath)) {
                        return treeWalk.getObjectId(0);
                    }
                }
            }
        }
        throw new FileNotFoundException(ExaError.messageBuilder("E-PK-SMC-35")
                .message("Failed to read file {{file path}} from commit {{commit id}}.")
                .parameter("file path", expectedPath).parameter("commit id", commit.getName())
                .mitigation("Make sure that the file exists at the given commit.").toString());
    }

    /**
     * Try to get a the name of the repository from the git remote configuration.
     *
     * @return repository name
     */
    public Optional<String> getRepoNameFromRemote() {
        try {
            final List<RemoteConfig> remotes = this.git.remoteList().call();
            final Optional<RemoteConfig> origin = remotes.stream().filter(remote -> remote.getName().equals("origin"))
                    .findAny();
            if (origin.isPresent()) {
                final String path = origin.get().getURIs().get(0).getPath();
                final String[] pathParts = path.split("/");
                final String repoName = pathParts[pathParts.length - 1].replace(".git", "");
                return Optional.of(repoName);
            } else {
                return Optional.empty();
            }
        } catch (final Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public void close() {
        this.git.close();
    }
}
