package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.SourceType;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.sources.AnalyzedGolangSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
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
        final boolean isRoot = projectDir.relativize(source.getPath()).equals(Path.of("go.mod"));
        final Path moduleDir = source.getPath().getParent();
        final ModuleInfo moduleInfo = this.golangServices.getModuleInfo(moduleDir);
        final String projectName = source.getPath().normalize().getParent().getFileName().toString();
        final ProjectDependencies dependencies = new ProjectDependencies(
                this.golangServices.getDependencies(moduleInfo, moduleDir));
        final DependencyChangeReport dependencyChanges = new DependencyChangeReport();
        dependencyChanges
                .setCompileDependencyChanges(this.golangServices.getDependencyChanges(projectDir, source.getPath()));
        dependencyChanges.setPluginDependencyChanges(emptyList());
        dependencyChanges.setRuntimeDependencyChanges(emptyList());
        dependencyChanges.setTestDependencyChanges(emptyList());
        return AnalyzedGolangSource.builder().version(null).isRootProject(isRoot).advertise(source.isAdvertise())
                .modules(source.getModules()).path(source.getPath()).projectName(projectName)
                .moduleName(moduleInfo.getModuleName()).dependencies(dependencies).dependencyChanges(dependencyChanges)
                .build();
    }
}
