package com.exasol.projectkeeper.sources.analyze.npm;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import com.exasol.projectkeeper.shared.config.Source;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.sources.AnalyzedSourceImpl;
import com.exasol.projectkeeper.sources.analyze.LanguageSpecificSourceAnalyzer;
import com.exasol.projectkeeper.sources.analyze.generic.CommandExecutor;
import com.exasol.projectkeeper.sources.analyze.generic.GitService;

/**
 * This class analyzes source projects using <a href="https://www.npmjs.com">Node Package Manager NPM</a>.
 */
public class NpmSourceAnalyzer implements LanguageSpecificSourceAnalyzer {

    private final NpmServices npmServices;

    /**
     * Create a new instance of {@link NpmSourceAnalyzer}
     */
    public NpmSourceAnalyzer() {
        this(new NpmServices(new CommandExecutor(), new GitService()));
    }

    NpmSourceAnalyzer(final NpmServices npmServices) {
        this.npmServices = npmServices;
    }

    @Override
    public List<AnalyzedSource> analyze(final Path projectDir, final List<Source> sources) {
        return sources.stream().map(source -> analyzeSource(projectDir, source)).collect(toList());
    }

    // [impl -> dsn~npm-project-version~1]
    private AnalyzedSource analyzeSource(final Path projectDir, final Source source) {
        final Path path = source.getPath();
        final PackageJson current = this.npmServices.readPackageJson(projectDir.resolve(path));
        final Optional<PackageJson> previous = this.npmServices.retrievePrevious(projectDir, current);
        return AnalyzedSourceImpl.builder() //
                .version(current.getVersion()) //
                .isRootProject(AnalyzedSourceImpl.isRoot(source)) //
                .advertise(source.isAdvertise()) //
                .modules(source.getModules()) //
                .path(path) //
                .projectName(AnalyzedSourceImpl.projectName(projectDir, source)) //
                .moduleName(current.getModuleName()) //
                .dependencies(this.npmServices.getDependencies(current)) //
                .dependencyChanges(NpmDependencyChanges.report(current, previous)) //
                .build();
    }

}