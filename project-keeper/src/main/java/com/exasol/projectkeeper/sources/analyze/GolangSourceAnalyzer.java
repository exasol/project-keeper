package com.exasol.projectkeeper.sources.analyze;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.config.ProjectKeeperConfig.SourceType;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.sources.AnalyzedGolangSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.sources.analyze.golang.GolangServices;

public class GolangSourceAnalyzer implements LanguageSpecificSourceAnalyzer {

    private final GolangServices golangServices;

    public GolangSourceAnalyzer() {
        this(new GolangServices());
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
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-")
                    .message("Analyzing of {{type}} is not supported by " + GolangSourceAnalyzer.class.getName() + ".",
                            source.getType())
                    .ticketMitigation().toString());
        }
        final boolean isRoot = projectDir.relativize(source.getPath()).equals(Path.of("go.mod"));

        final String artifactId = this.golangServices.getModuleName(projectDir);
        final String projectName = source.getPath().getParent().getFileName().toString();
        final ProjectDependencies dependencies = new ProjectDependencies(
                this.golangServices.getDependencies(projectDir));
        final DependencyChangeReport dependencyChanges = null;
        return AnalyzedGolangSource.builder().version(null).isRootProject(isRoot).advertise(source.isAdvertise())
                .modules(source.getModules()).path(source.getPath()).projectName(projectName).artifactId(artifactId)
                .dependencies(dependencies).dependencyChanges(dependencyChanges).build();
    }
}
