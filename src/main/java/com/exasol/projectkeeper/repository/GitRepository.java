package com.exasol.projectkeeper.repository;

import static org.eclipse.jgit.lib.Constants.R_TAGS;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import com.exasol.errorreporting.ExaError;

/**
 * This gives access to a local git repository.
 */
public class GitRepository {
    private final Path projectDirectory;

    /**
     * Create a new instance of {@link GitRepository}.
     * 
     * @param projectDirectory the projects directory.
     */
    public GitRepository(final Path projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    /**
     * Get the tags of the current branch ordered descending by commit date. If one commit has multiple tags, they are
     * returned sequential with no specified order.
     * 
     * @return descending ordered list of tags
     */
    public List<TaggedCommit> getTagsInCurrentBranch() {
        try (final var git = openLocalGithubRepository()) {
            final String currentBranch = git.getRepository().getFullBranch();
            validateBranchExists(currentBranch);
            return getTagsInBranch(git.getRepository(), currentBranch);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-33")
                            .message("Failed to retrieve latest tag from the local git repository.").toString(),
                    exception);
        }
    }

    private void validateBranchExists(final String currentBranch) {
        if (currentBranch == null) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-37").message("Could not get checked out branch of repository.")
                            .mitigation("Create a branch and check it out.").toString());
        }
    }

    private List<TaggedCommit> getTagsInBranch(final Repository repository, final String branchName)
            throws IOException {
        final ObjectId branch = repository.resolve(branchName);
        final Map<ObjectId, List<String>> tags = getTagsByTheIdOfTheCommitTheyPointTo(repository);
        final var commitWalker = new RevWalk(repository);
        try {
            commitWalker.markStart(commitWalker.parseCommit(branch));
        } catch (final NullPointerException exception) {
            // NullPointerException is thrown if the branch has no commits
            return Collections.emptyList();
        }
        commitWalker.sort(RevSort.COMMIT_TIME_DESC);
        return readTagsFromCommits(tags, commitWalker);
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
        for (final Ref tagRef : repository.getRefDatabase().getRefsByPrefix(R_TAGS)) {
            final String tagName = tagRef.getName().replace(R_TAGS, "");
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
                throw new UnsupportedOperationException(ExaError.messageBuilder("F-PK-44")
                        .message("Unsupported tag target {{target class name}}.")
                        .parameter("target class name", revObject.getClass().getName()).ticketMitigation().toString());
            }
        }
    }

    /**
     * Read a specific file at a given commit.
     * 
     * @param relativeFilePath file path
     * @param commit           commit
     * @return file contents
     */
    public String readFileAtCommit(final Path relativeFilePath, final GitCommit commit) throws FileNotFoundException {
        try (final var git = openLocalGithubRepository()) {
            return readFileAtCommit(relativeFilePath, git, commit.getCommit());
        } catch (final FileNotFoundException exception) {
            throw exception;
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-43")
                            .message("Failed to retrieve latest tag from the local git repository.").toString(),
                    exception);
        }
    }

    private String readFileAtCommit(final Path relativeFilePath, final Git git, final RevCommit commit)
            throws IOException {
        final var repository = git.getRepository();
        final var reader = repository.newObjectReader();
        final var treeParser = new CanonicalTreeParser(null, reader, commit.getTree());
        if (!treeParser.findFile(relativeFilePath.toString())) {
            throw new FileNotFoundException(ExaError.messageBuilder("E-PK-35")
                    .message("Failed to read file {{file path}} from commit {{commit id}}.")
                    .parameter("file path", relativeFilePath).parameter("commit id", commit.getName())
                    .mitigation("Make sure that the file exists at the given commit.").toString());
        }
        final var objectForVersionOfFile = treeParser.getEntryObjectId();
        final var objectLoader = reader.open(objectForVersionOfFile);
        final var contentBuffer = new ByteArrayOutputStream();
        objectLoader.copyTo(contentBuffer);
        return contentBuffer.toString(StandardCharsets.UTF_8);
    }

    private Git openLocalGithubRepository() {
        try {
            return Git.open(this.projectDirectory.toFile());
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-32")
                    .message("Failed to open local git repository {{repository}}.")
                    .mitigation("If this is a new project you maybe need to create the git project using `git init`.")
                    .parameter("repository", this.projectDirectory.toString()).toString(), exception);
        }
    }

    /**
     * Try to get a the name of the repository from the git remote configuration.
     * 
     * @return repository name
     */
    public Optional<String> getRepoNameFromRemote() {
        try {
            final List<RemoteConfig> remotes = openLocalGithubRepository().remoteList().call();
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
}
