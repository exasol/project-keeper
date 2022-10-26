package com.exasol.projectkeeper.sources.analyze.npm;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.List;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.sources.AnalyzedSourceImpl;
import com.exasol.projectkeeper.sources.analyze.LanguageSpecificSourceAnalyzer;
import com.exasol.projectkeeper.sources.analyze.generic.CommandExecutor;

public class NpmSourceAnalyzer implements LanguageSpecificSourceAnalyzer {

    private final NpmServices npmServices;

    /**
     * create a new instance of {@link NpmSourceAnalyzer}
     */
    public NpmSourceAnalyzer() {
        this(new NpmServices(new CommandExecutor()));
    }

    NpmSourceAnalyzer(final NpmServices npmServices) {
        this.npmServices = npmServices;
    }

    @Override
    public List<AnalyzedSource> analyze(final Path projectDir, final List<Source> sources) {
        return sources.stream().map(source -> analyzeSource(projectDir, source)).collect(toList());
    }

    private AnalyzedSource analyzeSource(final Path projectDir, final Source source) {
        final PackageJson packageJson = PackageJsonReader.read(projectDir);
        return AnalyzedSourceImpl.builder() //
                .version(packageJson.getVersion()) //
                .isRootProject(AnalyzedSourceImpl.isRoot(source)) //
                .advertise(source.isAdvertise()) //
                .modules(source.getModules()) //
                .path(source.getPath()) //
                .projectName(AnalyzedSourceImpl.projectName(projectDir, source)) //
                .moduleName(packageJson.getModuleName()) //
                .dependencies(this.npmServices.getDependencies(projectDir)) //
                .dependencyChanges(this.npmServices.getDependencyChangeReport(packageJson)) //
                .build();
    }

}