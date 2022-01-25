package com.exasol.projectkeeper.config;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import lombok.Data;

public class ProjectKeeperConfigWriter {

    public void writeConfig(final ProjectKeeperConfig config, final Path projectDirectory) {
        try (final FileWriter fileWriter = new FileWriter(projectDirectory.resolve(".project-keeper.yml").toFile())) {
            final DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setAllowReadOnlyProperties(true);
            new Yaml(dumperOptions).dump(prepareWriting(config), fileWriter);
        } catch (final IOException exception) {
            throw new IllegalStateException("Failed to write project-keeper config.", exception);
        }
    }

    public ConfigForWriting prepareWriting(final ProjectKeeperConfig config) {
        final List<ConfigForWriting.Source> sourcesForWriting = new ArrayList<>();
        for (final ProjectKeeperConfig.Source source : config.getSources()) {
            final List<String> modules = source.getModules().stream().map(Enum::name).collect(Collectors.toList());
            sourcesForWriting.add(new ConfigForWriting.Source(source.getPath().toString(), source.getType().name(),
                    modules, source.getExcludes()));
        }
        return new ConfigForWriting(sourcesForWriting, config.getLinkReplacements(), config.getExcludes());
    }

    @Data
    public static class ConfigForWriting {
        private final List<Source> sources;
        private final List<String> linkReplacements;
        private final List<String> excludes;

        @Data
        public static class Source {
            private final String path;
            private final String type;
            private final List<String> modules;
            private final List<String> excludes;
        }
    }
}