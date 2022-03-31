package com.exasol.projectkeeper.sources;

import static java.util.stream.Collectors.groupingBy;

import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.config.ProjectKeeperConfig.SourceType;
import com.exasol.projectkeeper.sources.analyze.*;

/**
 * This class analyzes source projects.
 */
public class SourceAnalyzer {
    private final Map<SourceType, LanguageSpecificSourceAnalyzer> sourceAnalyzers;

    private SourceAnalyzer(final Map<SourceType, LanguageSpecificSourceAnalyzer> sourceAnalyzers) {
        this.sourceAnalyzers = sourceAnalyzers;
    }

    public static SourceAnalyzer create(final Path mvnRepo, final String ownVersion) {
        return new SourceAnalyzer(getLanguageSpecificSourceAnalyzers(mvnRepo, ownVersion));
    }

    private static Map<SourceType, LanguageSpecificSourceAnalyzer> getLanguageSpecificSourceAnalyzers(
            final Path mvnRepo, final String ownVersion) {
        return Map.of( //
                SourceType.MAVEN, new MavenSourceAnalyzer(mvnRepo, ownVersion), //
                SourceType.GOLANG, new GolangSourceAnalyzer());
    }

    /**
     * Analyze a source project.
     *
     * @param projectDir project directory
     * @param sources    configured sources
     * @return analyzed sources
     */
    public List<AnalyzedSource> analyze(final Path projectDir, final List<ProjectKeeperConfig.Source> sources) {
        final Map<SourceType, List<Source>> groupedSources = sources.stream()
                .collect(groupingBy(ProjectKeeperConfig.Source::getType));

        final List<AnalyzedSource> analyzedSources = new ArrayList<>(sources.size());
        for (final Entry<SourceType, List<Source>> entry : groupedSources.entrySet()) {
            final LanguageSpecificSourceAnalyzer analyzer = getAnalyzer(entry.getKey());
            analyzedSources.addAll(analyzer.analyze(projectDir, entry.getValue()));
        }
        return analyzedSources;
    }

    private LanguageSpecificSourceAnalyzer getAnalyzer(final SourceType type) {
        final LanguageSpecificSourceAnalyzer analyzer = this.sourceAnalyzers.get(type);
        if (analyzer == null) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-131")
                    .message("No source analyzer found for source type {{source type}}.", type).ticketMitigation()
                    .toString());
        }
        return analyzer;
    }
}