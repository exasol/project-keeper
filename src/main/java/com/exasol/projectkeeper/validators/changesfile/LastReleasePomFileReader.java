package com.exasol.projectkeeper.validators.changesfile;

import java.nio.file.Path;
import java.util.Optional;

import com.exasol.projectkeeper.repository.GitRepository;
import com.exasol.projectkeeper.repository.TaggedCommit;

/**
 * This class reads the pom file of the latest previous release on the current branch.
 */
public class LastReleasePomFileReader {
    private static final Path POM_PATH = Path.of("pom.xml");

    /**
     * Read the content of the pom.xml file from the previous release on the current branch.
     * 
     * @param projectDirectory projects root directory
     * @param currentVersion   current release version
     * @return content of the pom file
     */
    public Optional<String> readLatestReleasesPomFile(final Path projectDirectory, final String currentVersion) {
        final var gitRepository = new GitRepository(projectDirectory);
        final var exasolVersionMatcher = new ExasolVersionMatcher();
        final Optional<TaggedCommit> latestCommitWithExasolVersionTag = gitRepository.getTagsInCurrentBranch().stream()
                .filter(taggedCommit -> exasolVersionMatcher.isExasolStyleVersion(taggedCommit.getTag()))//
                .filter(taggedCommit -> !taggedCommit.getTag().equals(currentVersion))//
                .findFirst();
        return latestCommitWithExasolVersionTag
                .map(taggedCommit -> gitRepository.readFileAtCommit(POM_PATH, taggedCommit.getCommit()));
    }
}
