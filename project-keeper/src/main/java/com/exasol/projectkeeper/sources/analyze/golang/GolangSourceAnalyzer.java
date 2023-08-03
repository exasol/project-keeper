package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.config.*;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.sources.AnalyzedSourceImpl;
import com.exasol.projectkeeper.sources.analyze.LanguageSpecificSourceAnalyzer;

/**
 * This class analyzes Golang source projects.
 */
public class GolangSourceAnalyzer implements LanguageSpecificSourceAnalyzer {
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
        final GoModule moduleInfo = this.golangServices.getModuleInfo(absoluteSourceDir);
        final ProjectDependencies dependencies = GolangDependencyCalculator.calculateDependencies(this.golangServices,
                absoluteSourceDir, moduleInfo);
        return AnalyzedSourceImpl.builder() //
                .version(this.golangServices.getProjectVersion()) //
                .isRootProject(AnalyzedSourceImpl.isRoot(source)) //
                .advertise(source.isAdvertised()) //
                .modules(source.getModules()) //
                .path(source.getPath()) //
                .projectName(AnalyzedSourceImpl.projectName(projectDir, source)) //
                .moduleName(moduleInfo.getName()) //
                .dependencies(dependencies) //
                .dependencyChanges(GolangDependencyChangeCalculator.calculateDepencencyChanges(this.golangServices,
                        projectDir, source, dependencies)) //
                .build();
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
