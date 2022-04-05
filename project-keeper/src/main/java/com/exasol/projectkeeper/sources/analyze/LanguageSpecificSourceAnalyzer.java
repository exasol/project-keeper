package com.exasol.projectkeeper.sources.analyze;

import java.nio.file.Path;
import java.util.List;

import com.exasol.projectkeeper.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.sources.AnalyzedSource;

/**
 * Implement this interface to analyze a certain type of source project, e.g. Maven or Golang.
 */
public interface LanguageSpecificSourceAnalyzer {

    /**
     * Analyze the given source projects located in the common project root directory.
     * 
     * @param projectRootDir the common root directory for the sources
     * @param sources        the sources to analyze
     * @return analyzed sources
     */
    List<AnalyzedSource> analyze(Path projectRootDir, List<Source> sources);
}
