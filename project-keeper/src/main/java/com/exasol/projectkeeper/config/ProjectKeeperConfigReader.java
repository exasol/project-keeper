package com.exasol.projectkeeper.config;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ProjectKeeperModule;

import lombok.Data;

/**
 * This class reads {@link ProjectKeeperConfig} from file.
 */
public class ProjectKeeperConfigReader {
    private static final String USER_GUIDE_URL = "https://github.com/exasol/project-keeper-maven-plugin";
    private static final String CHECK_THE_USER_GUIDE = "Please check the user-guide " + USER_GUIDE_URL + ".";
    private static final String CONFIG_FILE_NAME = ".project-keeper.yml";
    private static final String INVALID_CONFIG_FILE = "Invalid " + CONFIG_FILE_NAME + ".";

    /**
     * Read a {@link ProjectKeeperConfig} from file.
     * 
     * @param projectDirectory project directory
     * @return read {@link ProjectKeeperConfig}
     */
    public ProjectKeeperConfig readConfig(final Path projectDirectory) {
        final Path configFile = projectDirectory.resolve(CONFIG_FILE_NAME);
        validateConfigFileExists(configFile);
        try (final FileReader fileReader = new FileReader(configFile.toFile())) {
            final ProjectKeeperRawConfig rawConfig = readRawConfig(fileReader);
            return parseRawConfig(rawConfig, projectDirectory);
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-82")
                    .message("Failed to read '" + CONFIG_FILE_NAME + "'.").toString(), exception);
        }
    }

    private ProjectKeeperRawConfig readRawConfig(final FileReader fileReader) {
        try {
            return new Yaml().loadAs(fileReader, ProjectKeeperRawConfig.class);
        } catch (final YAMLException exception) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-85").message(INVALID_CONFIG_FILE)
                    .mitigation(CHECK_THE_USER_GUIDE).toString(), exception);
        }
    }

    private void validateConfigFileExists(final Path configFile) {
        if (!Files.exists(configFile)) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-89")
                    .message("Could not find '" + CONFIG_FILE_NAME + "'.")
                    .mitigation("Please create this configuration according to the user-guide " + USER_GUIDE_URL + ".")
                    .toString());//
        }
    }

    private ProjectKeeperConfig parseRawConfig(final ProjectKeeperRawConfig rawConfig, final Path projectDir) {
        final List<ProjectKeeperRawConfig.Source> rawSources = rawConfig.getSources();
        final List<ProjectKeeperConfig.Source> sources = convertSources(projectDir, rawSources);
        final List<String> excludes = convertExcludes(rawConfig.getExcludes());
        final List<String> linkReplacements = Objects.requireNonNullElseGet(rawConfig.getLinkReplacements(),
                Collections::emptyList);
        return new ProjectKeeperConfig(sources, linkReplacements, excludes);
    }

    private List<String> convertExcludes(final List<Object> rawExcludes) {
        if (rawExcludes == null) {
            return Collections.emptyList();
        }
        final List<String> excludes = new ArrayList<>(rawExcludes.size());
        for (final Object rawExclude : rawExcludes) {
            excludes.add(convertExclude(rawExclude));
        }
        return excludes;
    }

    /**
     * Convert the value of an exclude-object to a regex string
     *
     * @param rawExclude raw exclude object: non regex string or { regex: "" }
     * @return regex string
     */
    private String convertExclude(final Object rawExclude) {
        try {
            if (rawExclude instanceof String) {
                return Pattern.quote((String) rawExclude);
            } else {
                final Map<?, ?> excludeMap = (Map<?, ?>) rawExclude;
                return (String) Objects.requireNonNull(excludeMap.get("regex"));
            }
        } catch (final ClassCastException | NullPointerException exception) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-87")
                    .message(INVALID_CONFIG_FILE + " Invalid value for property 'excludes'.")
                    .mitigation("Please use either a string or 'regex: \"my-regx\"'.").mitigation(CHECK_THE_USER_GUIDE)
                    .toString());
        }
    }

    private List<ProjectKeeperConfig.Source> convertSources(final Path projectDir,
            final List<ProjectKeeperRawConfig.Source> rawSources) {
        if (rawSources == null) {
            return Collections.emptyList();
        } else {
            final List<ProjectKeeperConfig.Source> sources = new ArrayList<>(rawSources.size());
            for (final ProjectKeeperRawConfig.Source rawSource : rawSources) {
                final ProjectKeeperConfig.Source source = convertSource(projectDir, rawSource);
                sources.add(source);
            }
            return sources;
        }
    }

    private ProjectKeeperConfig.Source convertSource(final Path projectDir,
            final ProjectKeeperRawConfig.Source rawSource) {
        final String rawType = rawSource.getType();
        final Set<ProjectKeeperModule> modules = convertModules(rawSource.getModules());
        final Path path = convertPath(projectDir, rawSource.getPath());
        final List<String> excludes = convertExcludes(rawSource.getExcludes());
        return new ProjectKeeperConfig.Source(path, convertType(rawType), modules, excludes);
    }

    private Set<ProjectKeeperModule> convertModules(final List<String> rawModules) {
        if (rawModules == null) {
            return Set.of(ProjectKeeperModule.DEFAULT);
        }
        final Set<ProjectKeeperModule> modules = new HashSet<>(rawModules.size() + 1);
        for (final String rawModule : rawModules) {
            modules.add(ProjectKeeperModule.getModuleByName(rawModule));
        }
        modules.add(ProjectKeeperModule.DEFAULT);
        return modules;
    }

    private Path convertPath(final Path projectDir, final String rawPath) {
        requireProperty(rawPath, "sources/path");
        final Path path = projectDir.resolve(rawPath);
        validatePathExists(path);
        return path;
    }

    private void requireProperty(final Object propertyValue, final String propertyName) {
        if (propertyValue == null) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-86")
                    .message(INVALID_CONFIG_FILE + " Missing required property {{property name}}.", propertyName)
                    .toString());
        }
    }

    private void validatePathExists(final Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-83")
                    .message(INVALID_CONFIG_FILE
                            + " The specified path {{path}} does not exist in the project directory.", path)
                    .toString());
        }
    }

    private ProjectKeeperConfig.SourceType convertType(final String rawType) {
        requireProperty(rawType, "sources/type");
        try {
            return ProjectKeeperConfig.SourceType.valueOf(rawType.toUpperCase(Locale.ROOT));
        } catch (final IllegalArgumentException exception) {
            final String supportedTypes = Arrays.stream(ProjectKeeperConfig.SourceType.values()).map(Enum::toString)
                    .map(String::toLowerCase).collect(Collectors.joining(", "));
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-84")
                    .message(INVALID_CONFIG_FILE + " Unsupported source type {{type}}.", rawType)
                    .mitigation("Please use one of the supported types: {{supported types|uq}}.", supportedTypes)
                    .toString());
        }
    }

    /**
     * Intermediate class for reading the config.
     * <p>
     * SnakeYML requires this class to be public.
     * </p>
     */
    @Data
    public static class ProjectKeeperRawConfig {
        private List<Source> sources;
        private List<String> linkReplacements;
        /** String or map (regex: string) */
        private List<Object> excludes;

        /**
         * Intermediate class for reading the config sources.
         * <p>
         * SnakeYML requires this class to be public.
         * </p>
         */
        @Data
        public static class Source {
            private String path;
            private String type;
            private List<String> modules;
            /** String or map (regex: string) */
            private List<Object> excludes;
        }
    }
}
