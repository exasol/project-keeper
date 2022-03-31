package com.exasol.projectkeeper.validators.changesfile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.repository.GitRepository;
import com.exasol.projectkeeper.shared.repository.TaggedCommit;

/**
 * This class reads the pom file of the latest previous release on the current branch.
 */
public class LastReleasePomFileReader {
    private static final Logger LOGGER = Logger.getLogger(LastReleasePomFileReader.class.getName());

    /**
     * Copy pom.xml file from a given git release tag to another directory. If the pom file specifies a parent with
     * relative path, this method extracts it too.
     *
     * @param projectDirectory  projects root directory
     * @param relativePathToPom path to the pom file to extract (relative to the {@code projectDirectory})
     * @param currentVersion    version (git tag) to read the file at
     * @param targetDirectory   directory to write the files to
     * @return {@code true} if the specified pom file was found
     */
    public Optional<Path> extractLatestReleasesPomFile(final Path projectDirectory, final Path relativePathToPom,
            final String currentVersion, final Path targetDirectory) {
        final Path gitRepo = findGitRepo(projectDirectory);
        final Path relativeProjectDirPath = gitRepo.relativize(projectDirectory);
        try (final var gitRepository = GitRepository.open(gitRepo)) {
            final Optional<TaggedCommit> latestReleaseCommit = gitRepository.findLatestReleaseCommit(currentVersion);
            if (latestReleaseCommit.isEmpty()) {
                return Optional.empty();
            } else {
                final Path pathToPomRelativeToGit = relativeProjectDirPath.resolve(relativePathToPom);
                final Optional<Path> pomFile = extractPomFile(gitRepository, latestReleaseCommit.get(),
                        pathToPomRelativeToGit, targetDirectory);
                pomFile.ifPresent(path -> extractParentStack(targetDirectory, gitRepository, latestReleaseCommit.get(),
                        projectDirectory.resolve(relativePathToPom), path, gitRepo));
                return pomFile;
            }
        }
    }

    private void extractParentStack(final Path targetDirectory, final GitRepository gitRepository,
            final TaggedCommit latestCommitWithExasolVersionTag, final Path pathToPom, final Path extractedPomFile,
            final Path pathToGitRepo) {
        Path currentPomPath = pathToPom;
        Parent parentRef = readPomFile(extractedPomFile).getParent();
        while ((parentRef != null) && (parentRef.getRelativePath() != null) && !parentRef.getRelativePath().isBlank()) {
            currentPomPath = currentPomPath.getParent().resolve(parentRef.getRelativePath()).normalize();
            final Path pathRelativeToGitRepo = pathToGitRepo.relativize(currentPomPath);
            final Optional<Path> parentPom = extractPomFile(gitRepository, latestCommitWithExasolVersionTag,
                    pathRelativeToGitRepo, targetDirectory);
            if (parentPom.isEmpty()) {
                throw new IllegalStateException(ExaError.messageBuilder("E-PK-MPC-61")
                        .message("Failed to extract the parent pom {{path}} fom git history.", currentPomPath)
                        .toString());
            } else {
                parentRef = readPomFile(parentPom.get()).getParent();
            }
        }
    }

    private Model readPomFile(final Path pomFile) {
        try (final FileReader fileReader = new FileReader(pomFile.toFile())) {
            return new MavenXpp3Reader().read(fileReader);
        } catch (final XmlPullParserException | IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-MPC-60")
                    .message("Failed to read pom file of previous version for extracting it's parent.").toString(),
                    exception);
        }
    }

    private Path findGitRepo(final Path projectDirectory) {
        Path currentDir = projectDirectory;
        while (!Files.exists(currentDir.resolve(".git"))) {
            currentDir = currentDir.getParent();
        }
        return currentDir;
    }

    private Optional<Path> extractPomFile(final GitRepository gitRepository, final TaggedCommit taggedCommit,
            final Path pathToPom, final Path targetDirectory) {
        try {
            final Path targetFile = targetDirectory.resolve(pathToPom);
            gitRepository.extractFileFromCommit(pathToPom, taggedCommit.getCommit(), targetFile);
            return Optional.of(targetFile);
        } catch (final FileNotFoundException exception) {
            LOGGER.warning("Could not read pom file from previous release. Assuming empty file.");
            return Optional.empty();
        }
    }
}
