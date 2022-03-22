package com.exasol.projectkeeper;

import java.io.IOException;
import java.nio.file.*;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class TestRepo implements AutoCloseable {
    private final Path directory;
    private final Git git;

    public TestRepo(final Path directory) throws GitAPIException {
        this.directory = directory;
        this.git = Git.init().setDirectory(directory.toFile()).call();
    }

    public TestRepo addAll() throws IOException {
        Files.walk(this.directory).forEach(path -> {
            if (Files.isRegularFile(path)) {
                add(path);
            }
        });
        return this;
    }

    private void add(final Path path) {
        try {
            this.git.add().addFilepattern(makeOsIndependentPattern(this.directory.relativize(path).toString())).call();
        } catch (final GitAPIException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private String makeOsIndependentPattern(final String pattern) {
        return pattern.replace(FileSystems.getDefault().getSeparator(), "/");
    }

    public TestRepo commit() throws GitAPIException {
        this.git.commit().setMessage("default commit message").call();
        return this;
    }

    public TestRepo createTag(final String tagName) throws GitAPIException {
        this.git.tag().setName(tagName).call();
        return this;
    }

    @Override
    public void close() {
        this.git.close();
    }
}
