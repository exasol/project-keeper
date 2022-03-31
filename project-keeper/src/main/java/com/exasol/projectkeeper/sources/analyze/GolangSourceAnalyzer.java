package com.exasol.projectkeeper.sources.analyze;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.config.ProjectKeeperConfig.SourceType;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.sources.AnalyzedGolangSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.sources.analyze.golang.GolangServices;
import com.exasol.projectkeeper.sources.analyze.golang.ModuleInfo;

public class GolangSourceAnalyzer implements LanguageSpecificSourceAnalyzer {

    private final GolangServices golangServices;

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

        final boolean isRoot = projectDir.relativize(source.getPath()).equals(Path.of("go.mod"));

        final Path moduleDir = source.getPath().getParent();
        final ModuleInfo moduleInfo = this.golangServices.getModuleInfo(moduleDir);
        final String projectName = source.getPath().getParent().getFileName().toString();
        final ProjectDependencies dependencies = new ProjectDependencies(
                this.golangServices.getDependencies(moduleInfo, moduleDir));
        final DependencyChangeReport dependencyChanges = new DependencyChangeReport();
        dependencyChanges
                .setCompileDependencyChanges(this.golangServices.getDependencyChanges(projectDir, source.getPath()));
        return AnalyzedGolangSource.builder().version(null).isRootProject(isRoot).advertise(source.isAdvertise())
                .modules(source.getModules()).path(source.getPath()).projectName(projectName)
                .artifactId(moduleInfo.getModuleName()).dependencies(dependencies).dependencyChanges(dependencyChanges)
                .build();
    }
}
