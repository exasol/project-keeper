package com.exasol.projectkeeper.sources.analyze;

import java.nio.file.Path;
import java.util.List;

import com.exasol.projectkeeper.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.sources.AnalyzedSource;

public interface LanguageSpecificSourceAnalyzer {

    List<AnalyzedSource> analyze(Path projectDir, List<Source> sources);

}
