package com.exasol.projectkeeper.validators.changesfile;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import com.exasol.projectkeeper.ExasolVersionMatcher;
import com.exasol.projectkeeper.repository.GitRepository;
import com.exasol.projectkeeper.repository.TaggedCommit;

/**
 * This class reads the pom file of the latest previous release on the current branch.
 */
public class LastReleasePomFileReader {
    private static final Logger LOGGER = Logger.getLogger(LastReleasePomFileReader.class.getName());
    private static final Path POM_PATH = Path.of("pom.xml");

    /**
     * Read the content of the pom.xml file from the previous release on the current branch.
     * 
     * @param projectDirectory projects root directory
     * @param currentVersion   current release version
     * @return content of the pom file
     */
    public Optional<String> readLatestReleasesPomFile(final Path projectDirectory, final String currentVersion) {
        final Path gitRepo = findGitRepo(projectDirectory);
        final Path relativeProjectDirPath = gitRepo.relativize(projectDirectory);
        final var gitRepository = new GitRepository(gitRepo);
        final var exasolVersionMatcher = new ExasolVersionMatcher();
        final Optional<TaggedCommit> latestCommitWithExasolVersionTag = gitRepository.getTagsInCurrentBranch().stream()
                .filter(taggedCommit -> exasolVersionMatcher.isExasolStyleVersion(taggedCommit.getTag()))//
                .filter(taggedCommit -> !taggedCommit.getTag().equals(currentVersion))//
                .findFirst();
        return latestCommitWithExasolVersionTag.flatMap(
                taggedCommit -> readPomFile(gitRepository, taggedCommit, relativeProjectDirPath.resolve(POM_PATH)));
    }

    private Path findGitRepo(final Path projectDirectory) {
        Path currentDir = projectDirectory;
        while (!Files.exists(currentDir.resolve(".git"))) {
            currentDir = currentDir.getParent();
        }
        return currentDir;
    }

    private Optional<String> readPomFile(final GitRepository gitRepository, final TaggedCommit taggedCommit,
            final Path pathToPom) {
        try {
            return Optional.of(gitRepository.readFileAtCommit(pathToPom, taggedCommit.getCommit()));
        } catch (final FileNotFoundException exception) {
            LOGGER.warning("Could not read pom file from previous release. Assuming empty file.");
            return Optional.empty();
        }
    }
}
