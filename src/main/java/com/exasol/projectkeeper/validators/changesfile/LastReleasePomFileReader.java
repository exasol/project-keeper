package com.exasol.projectkeeper.validators.changesfile;

import java.nio.file.Path;
import java.util.Optional;

import com.exasol.projectkeeper.repository.GitRepository;
import com.exasol.projectkeeper.repository.TaggedCommit;

/**
 * This class reads the pom file of the latest release on the current branch.
 */
public class LastReleasePomFileReader {
    private static final Path POM_PATH = Path.of("pom.xml");

    /**
     * Read the content of the pom.xml file from the last release on the current branch.
     * 
     * @param projectDirectory projects root directory
     * @return content of the pom file
     */
    public Optional<String> readLatestReleasesPomFile(final Path projectDirectory) {
        final GitRepository gitRepository = new GitRepository(projectDirectory);
        final ExasolVersionMatcher exasolVersionMatcher = new ExasolVersionMatcher();
        final Optional<TaggedCommit> latestCommitWithExasolVersionTag = gitRepository.getTagsInCurrentBranch().stream()
                .filter(taggedCommit -> exasolVersionMatcher.isExasolStyleVersion(taggedCommit.getTag())).findFirst();
        return latestCommitWithExasolVersionTag
                .map(taggedCommit -> gitRepository.readFileAtCommit(POM_PATH, taggedCommit.getCommit()));
    }
}
