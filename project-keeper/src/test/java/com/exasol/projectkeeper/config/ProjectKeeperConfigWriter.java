package com.exasol.projectkeeper.config;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.exasol.projectkeeper.config.ProjectKeeperConfig.*;

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
            sourcesForWriting
                    .add(new ConfigForWriting.Source(source.getPath().toString(), source.getType().name(), modules));
        }
        final VersionConfig versionConfig = config.getVersionConfig();
        final Object version = convertVersionConfig(versionConfig);
        return new ConfigForWriting(sourcesForWriting, config.getLinkReplacements(), config.getExcludes(), version);
    }

    private Object convertVersionConfig(final VersionConfig versionConfig) {
        if (versionConfig == null) {
            return null;
        } else if (versionConfig instanceof FixedVersion) {
            return ((FixedVersion) versionConfig).getVersion();
        } else if (versionConfig instanceof VersionFromSource) {
            return Map.of("fromSource", ((VersionFromSource) versionConfig).getPathToPom().toString());
        } else {
            throw new UnsupportedOperationException(
                    "Writing version config of type " + versionConfig.getClass().getName() + " is not supported");
        }
    }

    @Data
    public static class ConfigForWriting {
        private final List<Source> sources;
        private final List<String> linkReplacements;
        private final List<String> excludes;
        private final Object version;

        @Data
        public static class Source {
            private final String path;
            private final String type;
            private final List<String> modules;
        }
    }
}
