package com.exasol.projectkeeper.test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Builder;

abstract class BaseProjectFixture implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(BaseProjectFixture.class.getName());
    private static final Duration PROCESS_TIMEOUT = Duration.ofSeconds(120);
    protected final Path projectDir;
    private Git gitRepo;

    protected BaseProjectFixture(final Path projectDir) {
        this.projectDir = projectDir;
    }

    public void gitInit() {
        try {
            this.gitRepo = Git.init().setDirectory(this.projectDir.toFile()).setInitialBranch("main").call();
        } catch (final IllegalStateException | GitAPIException exception) {
            throw new AssertionError("Error running git init: " + exception.getMessage(), exception);
        }
    }

    public void gitAddCommitTag(final String tagName) {
        try {
            this.gitRepo.add().addFilepattern(".").call();
            this.gitRepo.commit().setMessage("Prepare release " + tagName).call();
            this.gitRepo.tag().setName(tagName).call();
        } catch (final GitAPIException exception) {
            throw new AssertionError("Error running git add/commit/tag: " + exception.getMessage(), exception);
        }
    }

    public void writeConfig(final Builder configBuilder) {
        new ProjectKeeperConfigWriter().writeConfig(configBuilder.build(), this.projectDir);
    }

    protected void execute(final Path workingDir, final String... command) {
        LOG.fine(() -> "Running command %s in %s...".formatted(Arrays.toString(command), workingDir));
        try {
            final Process process = new ProcessBuilder(command)
                    .directory(workingDir.toFile())
                    .redirectErrorStream(false)
                    .start();
            final boolean success = process.waitFor(PROCESS_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
            if (!success) {
                final String stdOut = readStream(process.getInputStream());
                final String errorOut = readStream(process.getErrorStream());
                throw new AssertionError("Command " + Arrays.toString(command) + " did not finish after "
                        + PROCESS_TIMEOUT + ", std output: '" + stdOut + "', error output: '" + errorOut + "'.");
            }
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new AssertionError(exception);
        }
    }

    private static String readStream(final InputStream stream) throws IOException {
        return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public void close() {
        if (this.gitRepo != null) {
            this.gitRepo.close();
        }
    }
}
