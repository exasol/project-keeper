package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.SourceType;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.sources.AnalyzedGolangSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.sources.analyze.LanguageSpecificSourceAnalyzer;

/**
 * This class analyzes Golang source projects.
 */
public class GolangSourceAnalyzer implements LanguageSpecificSourceAnalyzer {

    private static final Pattern GIT_URL = Pattern.compile(".*/([^/]*)\\.git$");
    private final GolangServices golangServices;

    /**
     * Create a new Golang analyzer using the given project keeper configuration.
     *
     * @param config the project keeper configuration
     */
    public GolangSourceAnalyzer(final ProjectKeeperConfig config) {
        this(new GolangServices(config));
    }

    GolangSourceAnalyzer(final GolangServices golangServices) {
        this.golangServices = golangServices;
    }

    @Override
    public List<AnalyzedSource> analyze(final Path projectDir, final List<Source> sources) {
        return sources.stream().map(source -> analyzeSource(projectDir, source)).collect(toList());
    }

    private AnalyzedSource analyzeSource(final Path projectDir, final Source source) {
        validateGolangSource(source);
        final Path absoluteSourceDir = getAbsoluteSourceDir(projectDir, source);
        final ModuleInfo moduleInfo = this.golangServices.getModuleInfo(absoluteSourceDir);
        final ProjectDependencies dependencies = GolangDependencyCalculator.calculateDependencies(this.golangServices,
                absoluteSourceDir, moduleInfo);
        return AnalyzedGolangSource.builder() //
                .version(this.golangServices.getProjectVersion()) //
                .isRootProject(isRootSource(source)) //
                .advertise(source.isAdvertise()) //
                .modules(source.getModules()) //
                .path(source.getPath()) //
                .projectName(getProjectName(projectDir, source)) //
                .moduleName(moduleInfo.getModuleName()) //
                .dependencies(dependencies) //
                .dependencyChanges(GolangDependencyChangeCalculator.calculateDepencencyChanges(this.golangServices,
                        projectDir, source, dependencies)) //
                .build();
    }

    private String getProjectName(final Path projectDir, final Source source) {
        if (isRootSource(source)) {
            return getProjectName(projectDir);
        } else {
            return getProjectName(source.getPath().getParent());
        }
    }

    private String getProjectName(final Path projectDir) {
        final SimpleProcess process = SimpleProcess.start(projectDir,
                Arrays.asList("git", "config", "--get", "remote.origin.url"));
        final String url = process.getOutputStreamContent().trim();
        final Matcher matcher = GIT_URL.matcher(url);
        if (!matcher.matches()) {
            return projectDir.getFileName().toString();
        }
        return matcher.group(1);
    }

    private boolean isRootSource(final Source source) {
        return source.getPath().getParent() == null;
    }

    private Path getAbsoluteSourceDir(final Path projectDir, final Source source) {
        final Path sourceDir = source.getPath().getParent();
        if (sourceDir != null) {
            return projectDir.resolve(sourceDir);
        } else {
            return projectDir;
        }
    }

    private void validateGolangSource(final Source source) {
        if (source.getType() != SourceType.GOLANG) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-130")
                    .message("Analyzing of {{type}} is not supported by GolangSourceAnalyzer", source.getType())
                    .ticketMitigation().toString());
        }
        if (!source.getPath().getFileName().equals(Path.of("go.mod"))) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-133")
                    .message("Invalid path {{path}} for go source.", source.getPath())
                    .mitigation("The path must point to a \"go.mod\" file.").toString());
        }
    }
}
