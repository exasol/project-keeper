package com.exasol.projectkeeper.test;

import static java.util.Collections.emptySet;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import com.exasol.projectkeeper.shared.config.*;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Builder;

public class NpmProjectFixture extends BaseProjectFixture {
    private static final String PACKAGE_FILE_NAME = "package.json";
    private static final String PROJECT_VERSION = "1.2.3";

    public NpmProjectFixture(final Path projectDir) {
        super(projectDir);
    }

    public ProjectKeeperConfig.Builder createDefaultConfig() {
        return ProjectKeeperConfig.builder().sources(List.of(
                Source.builder().modules(emptySet()).type(SourceType.NPM).path(Path.of(PACKAGE_FILE_NAME)).build()));
    }

    public void prepareProjectFiles(final Builder configBuilder) {
        this.writeConfig(configBuilder);
        this.prepareProjectFiles();
        execute(projectDir, "npm" + suffixForWindows(".cmd"), "install");
    }

    private String suffixForWindows(final String suffix) {
        final String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if (os.indexOf("win") >= 0) {
            return suffix;
        }
        return "";
    }

    private void prepareProjectFiles() {
        prepareProjectFiles(Path.of("."));
    }

    private void prepareProjectFiles(final Path moduleDir) {
        writePackageJsonFile(moduleDir);
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
}
