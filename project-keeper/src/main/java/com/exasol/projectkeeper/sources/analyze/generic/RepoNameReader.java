package com.exasol.projectkeeper.sources.analyze.generic;

import java.nio.file.Path;

import com.exasol.projectkeeper.shared.repository.GitRepository;

/**
 * Read name of repository or return folder name
 */
public class RepoNameReader {

    private RepoNameReader() {
        // only static usage
    }

    /**
     * @param folder project's root folder
     * @return name of the repository or of the specified folder
     */
    public static String getRepoName(final Path folder) {
        final String folderName = folder.getFileName().toString();
        try (final GitRepository git = GitRepository.open(folder)) {
            return git.getRepoNameFromRemote().orElseGet(() -> folderName);
        } catch (final IllegalStateException exception) {
            return folderName;
        }
    }
}
