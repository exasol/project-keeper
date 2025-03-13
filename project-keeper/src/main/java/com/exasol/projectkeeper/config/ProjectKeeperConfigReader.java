package com.exasol.projectkeeper.config;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.config.ProjectKeeperRawConfig.*;
import com.exasol.projectkeeper.shared.config.*;
import com.exasol.projectkeeper.shared.config.ParentPomRef;
import com.exasol.projectkeeper.shared.config.Source;
import com.exasol.projectkeeper.shared.config.workflow.*;

/**
 * This class reads {@link ProjectKeeperConfig} from file.
 */
public class ProjectKeeperConfigReader {
    /**
     * Create a new instance.
     */
    public ProjectKeeperConfigReader() {
        // Required for adding Javadoc.
    }

    /** Filename of project keeper's configuration file */
    public static final String CONFIG_FILE_NAME = ".project-keeper.yml";
    private static final String USER_GUIDE_URL = "https://github.com/exasol/project-keeper";
    private static final String CHECK_THE_USER_GUIDE = "Please check the user-guide " + USER_GUIDE_URL + ".";
    private static final String INVALID_CONFIG_FILE = "Invalid file " + CONFIG_FILE_NAME + ".";

    /**
     * Read a {@link ProjectKeeperConfig} from file.
     *
     * @param projectDirectory project directory
     * @return read {@link ProjectKeeperConfig}
     */
    public ProjectKeeperConfig readConfig(final Path projectDirectory) {
        verifyWeReInProjectRoot(projectDirectory);
        final Path configFile = projectDirectory.resolve(CONFIG_FILE_NAME);
        validateConfigFileExists(configFile);
        try (final FileReader fileReader = new FileReader(configFile.toFile())) {
            final ProjectKeeperRawConfig rawConfig = readRawConfig(fileReader, configFile);
            return parseRawConfig(rawConfig, projectDirectory);
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-82")
                    .message("Failed to read file {{config file}}.", CONFIG_FILE_NAME).toString(), exception);
        }
    }

    private void verifyWeReInProjectRoot(final Path projectDirectory) {
        if (!Files.exists(projectDirectory.resolve(".git"))) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-90")
                    .message("Could not find .git directory in project-root {{root path}}.", projectDirectory)
                    .mitigation("Run 'git init'.")
                    .mitigation(
                            "Make sure that you run project-keeper only in the root directory of the git-repository. If you have multiple projects in that directory, define them in file '.project-keeper.yml'.")
                    .toString());
        }
    }

    private ProjectKeeperRawConfig readRawConfig(final FileReader fileReader, final Path path) {
        try {
            return new Yaml().loadAs(fileReader, ProjectKeeperRawConfig.class);
        } catch (final YAMLException exception) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-85")
                    .message(INVALID_CONFIG_FILE + " Path: {{path}}", path).mitigation(CHECK_THE_USER_GUIDE).toString(),
                    exception);
        }
    }

    private void validateConfigFileExists(final Path configFile) {
        if (!Files.exists(configFile)) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-89")
                    .message("Could not find file {{config file}}.", CONFIG_FILE_NAME)
                    .mitigation("Please create this file according to the user-guide " + USER_GUIDE_URL + ".")
                    .toString());//
        }
    }

    private ProjectKeeperConfig parseRawConfig(final ProjectKeeperRawConfig rawConfig, final Path projectDir) {
        final List<ProjectKeeperRawConfig.Source> rawSources = rawConfig.getSources();
        final List<Source> sources = convertSources(projectDir, rawSources);
        final List<String> excludes = convertExcludes(rawConfig.getExcludes());
        final List<String> linkReplacements = Objects.requireNonNullElseGet(rawConfig.getLinkReplacements(),
                Collections::emptyList);

        return ProjectKeeperConfig.builder() //
                .sources(sources) //
                .linkReplacements(linkReplacements) //
                .excludes(excludes) //
                .versionConfig(parseVersion(rawConfig.getVersion(), projectDir)) //
                .buildOptions(convertBuildOptions(rawConfig.getBuild())) //
                .build();
    }

    private VersionConfig parseVersion(final Object rawVersion, final Path projectDir) {
        if (rawVersion == null) {
            return null;
        } else if (rawVersion instanceof final String version) {
            return new FixedVersion(version);
        }
        if (rawVersion instanceof final Map versionMap) {
            final Object fromMvnSource = versionMap.get("fromSource");
            if (fromMvnSource instanceof final String fromMvnSourceString) {
                return new VersionFromSource(projectDir.resolve(Path.of(fromMvnSourceString)));
            }
        }
        throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-113")
                .message(INVALID_CONFIG_FILE + " Invalid value from property 'version'.")
                .mitigation(
                        "You can either set a version as string or tell PK from which maven source to read the version from with 'fromSource: \"path to project file to read version from\"'.")
                .toString());
    }

    private BuildOptions convertBuildOptions(final Build build) {
        if (build == null) {
            return BuildOptions.builder().build();
        }
        return BuildOptions.builder() //
                .runnerOs(build.getRunnerOs()) //
                .freeDiskSpace(build.shouldFreeDiskSpace()) //
                .exasolDbVersions(build.getExasolDbVersions()) //
                .workflows(convertWorkflows(build.workflows)) //
                .build();
    }

    private List<CustomWorkflow> convertWorkflows(final List<Workflow> workflows) {
        return Optional.ofNullable(workflows).map(List::stream) //
                .orElseGet(Stream::empty) //
                .map(this::convertWorkflow) //
                .toList();
    }

    private CustomWorkflow convertWorkflow(final Workflow workflow) {
        final List<String> supportedWorkflowNames = List.of("ci-build.yml", "release.yml", "dependencies_check.yml",
                "dependencies_update.yml");
        if (workflow.name == null) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-199")
                    .message("Missing workflow name in file {{config file name}}.", CONFIG_FILE_NAME)
                    .mitigation("Add a workflow name to the workflow configuration.").toString());
        }
        if (!supportedWorkflowNames.contains(workflow.name)) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-198")
                    .message("Unsupported workflow name {{workflow name}} found in file {{config file name}}.",
                            workflow.name, CONFIG_FILE_NAME, supportedWorkflowNames)
                    .mitigation("Please only use one of the supported workflows from {{supported workflow names}}",
                            supportedWorkflowNames)
                    .toString());
        }
        return CustomWorkflow.builder() //
                .workflowName(workflow.name) //
                .environment(workflow.environment) //
                .jobs(convertJobs(workflow.jobs))
                .steps(convertSteps(workflow.stepCustomizations)) //
                .build();
    }

    private List<CustomJob> convertJobs(final List<Job> jobs) {
        return Optional.ofNullable(jobs).orElseGet(Collections::emptyList)
                .stream().map(this::convertJob).toList();
    }

    private CustomJob convertJob(final Job job) {
        final JobPermissions.Builder permissionsBuilder = JobPermissions.builder();
        Optional.ofNullable(job.permissions)
                .orElseGet(Collections::emptyMap)
                .forEach((permission, accessLevelName) -> {
                    final JobPermissions.AccessLevel level = convertAccessLevel(accessLevelName, permission, job);
                    permissionsBuilder.add(permission, level);
                });
        return CustomJob.builder().jobName(job.name).permissions(permissionsBuilder.build()).build();
    }

    private JobPermissions.AccessLevel convertAccessLevel(final String name, final String permission, final Job job) {
        return JobPermissions.AccessLevel.forName(name).orElseThrow(
                () -> new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-209")
                        .message(
                                "Got invalid access level {{invalid access level}} for permission {{permission}} of job {{job name}} in {{config file}}.",
                                name, permission, job.name, CONFIG_FILE_NAME)
                        .mitigation("Please use one of {{available access levels}}.",
                                Stream.of(JobPermissions.AccessLevel.values()).map(JobPermissions.AccessLevel::getName)
                                        .collect(joining(",")))
                        .toString()));
    }

    private List<StepCustomization> convertSteps(final List<RawStepCustomization> stepCustomizations) {
        return Optional.ofNullable(stepCustomizations).orElseGet(Collections::emptyList) //
                .stream().map(this::convertStep).toList();
    }

    private StepCustomization convertStep(final RawStepCustomization step) {
        if (step.action == null) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-200")
                    .message("Missing action in step customization of file {{config file}}.", CONFIG_FILE_NAME)
                    .mitigation("Add action with one of values {{available actions}}.",
                            asList(StepCustomization.Type.values()))
                    .toString());
        }
        if (step.stepId == null || step.stepId.isBlank()) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-201")
                    .message("Missing stepId in step customization of file {{config file}}.", CONFIG_FILE_NAME)
                    .mitigation("Add stepId to the step customization.").toString());
        }
        if (step.content == null || step.content.isEmpty()) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-202")
                    .message("Missing content in step customization of file {{config file}}.", CONFIG_FILE_NAME)
                    .mitigation("Add content to the step customization.").toString());
        }
        return StepCustomization.builder() //
                .jobId(step.job) //
                .type(step.action) //
                .stepId(step.stepId) //
                .step(WorkflowStep.createStep(step.content)) //
                .build();
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
            if (rawExclude instanceof final String exclude) {
                return Pattern.quote(exclude);
            } else {
                final Map<?, ?> excludeMap = (Map<?, ?>) rawExclude;
                return (String) Objects.requireNonNull(excludeMap.get("regex"));
            }
        } catch (final ClassCastException | NullPointerException exception) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-87")
                    .message(INVALID_CONFIG_FILE + " Invalid value {{value}} for property 'excludes'.", rawExclude)
                    .mitigation("Please use either a string or 'regex: \"my-regx\"'.").mitigation(CHECK_THE_USER_GUIDE)
                    .toString());
        }
    }

    private List<Source> convertSources(final Path projectDir, final List<ProjectKeeperRawConfig.Source> rawSources) {
        return Optional.ofNullable(rawSources) //
                .orElseGet(Collections::emptyList) //
                .stream() //
                .map(source -> convertSource(projectDir, source)) //
                .toList();
    }

    private Source convertSource(final Path projectDir, final ProjectKeeperRawConfig.Source rawSource) {
        final String rawType = rawSource.getType();
        final Set<ProjectKeeperModule> modules = convertModules(rawSource.getModules());
        final Path path = convertPath(projectDir, rawSource.getPath());
        return Source.builder() //
                .path(path) //
                .type(convertType(rawType)) //
                .modules(modules) //
                .advertise(rawSource.isAdvertised()) //
                .parentPom(parseParentPomProperty(rawSource.getParentPom())) //
                .releaseArtifacts(convertArtifacts(rawSource)) //
                .build();
    }

    private List<Path> convertArtifacts(final ProjectKeeperRawConfig.Source rawSource) {
        return Optional.ofNullable(rawSource.getArtifacts()) //
                .orElseGet(Collections::emptyList) //
                .stream() //
                .map(Path::of) //
                .toList();
    }

    private ParentPomRef parseParentPomProperty(final ProjectKeeperRawConfig.ParentPomRef rawParentPomRef) {
        if (rawParentPomRef == null) {
            return null;
        } else {
            return new ParentPomRef(rawParentPomRef.getGroupId(), rawParentPomRef.getArtifactId(),
                    rawParentPomRef.getVersion(), rawParentPomRef.getRelativePath());
        }
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
                    .message(INVALID_CONFIG_FILE + " Required property {{property name}} is missing.", propertyName)
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

    private SourceType convertType(final String rawType) {
        requireProperty(rawType, "sources/type");
        try {
            return SourceType.valueOf(rawType.toUpperCase(Locale.ROOT));
        } catch (final IllegalArgumentException exception) {
            final String supportedTypes = Arrays.stream(SourceType.values()).map(Enum::toString)
                    .map(String::toLowerCase).collect(Collectors.joining(", "));
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-84")
                    .message(INVALID_CONFIG_FILE + " Unsupported source type {{type}}.", rawType)
                    .mitigation("Please use one of the supported types: {{supported types|u}}.", supportedTypes)
                    .toString());
        }
    }
}
