package com.exasol.projectkeeper.test;

import static java.util.Collections.emptySet;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.exasol.projectkeeper.shared.config.*;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Builder;

public class NpmProjectFixture implements AutoCloseable {
    private static final String PACKAGE_FILE_NAME = "package.json";
    private static final String PROJECT_VERSION = "1.2.3";

    private final Path projectDir;
    private Git gitRepo;

    public NpmProjectFixture(final Path projectDir) {
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

    public ProjectKeeperConfig.Builder createDefaultConfig() {
        return ProjectKeeperConfig.builder().sources(List.of(
                Source.builder().modules(emptySet()).type(SourceType.NPM).path(Path.of(PACKAGE_FILE_NAME)).build()));
    }

    public void prepareProjectFiles(final Builder configBuilder) {
        this.writeConfig(configBuilder);
        this.prepareProjectFiles();
        FixtureHelpers.execute(projectDir, "npm", "install");
    }

    public void prepareProjectFiles() {
        prepareProjectFiles(Path.of("."));
    }

    public void prepareProjectFiles(final Path moduleDir) {
        writePackageJsonFile(moduleDir);
    }

    public String getProjectVersion() {
        return PROJECT_VERSION;
    }

    public void writeConfig(final ProjectKeeperConfig.Builder config) {
        new ProjectKeeperConfigWriter().writeConfig(config.build(), this.projectDir);
    }

    void initializeGitRepo() throws GitAPIException {
        Git.init().setDirectory(this.projectDir.toFile()).call().close();
    }

    private void writePackageJsonFile(final Path moduleDir) {
        final String content = """
                {
                    "name": "@exasol/project-name",
                    "version": "%s",
                    "dependencies": {
                        "@exasol/extension-parameter-validator": "0.3.1"
                    }
                }
                """.formatted(PROJECT_VERSION);
        writeFile(this.projectDir.resolve(moduleDir).resolve(PACKAGE_FILE_NAME), content);
    }

    private void writeFile(final Path path, final String content) {
        try {
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.writeString(path, content);
        } catch (final IOException exception) {
            throw new UncheckedIOException("Error writing content to file " + path, exception);
        }
    }

    @Override
    public void close() {
        if (this.gitRepo != null) {
            this.gitRepo.close();
        }
    }
}
