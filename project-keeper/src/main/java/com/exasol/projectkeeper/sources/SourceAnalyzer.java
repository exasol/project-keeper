package com.exasol.projectkeeper.sources;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.config.*;
import com.exasol.projectkeeper.sources.analyze.LanguageSpecificSourceAnalyzer;
import com.exasol.projectkeeper.sources.analyze.MavenSourceAnalyzer;
import com.exasol.projectkeeper.sources.analyze.golang.GolangSourceAnalyzer;
import com.exasol.projectkeeper.sources.analyze.npm.NpmSourceAnalyzer;

/**
 * This class analyzes source projects of any type by calling the relevant {@link LanguageSpecificSourceAnalyzer}.
 */
public class SourceAnalyzer {
    private final Map<SourceType, LanguageSpecificSourceAnalyzer> sourceAnalyzers;

    SourceAnalyzer(final Map<SourceType, LanguageSpecificSourceAnalyzer> sourceAnalyzers) {
        this.sourceAnalyzers = sourceAnalyzers;
    }

    /**
     * Creates a new instance of {@link SourceAnalyzer}, configuring the available
     * {@link LanguageSpecificSourceAnalyzer}s.
     *
     * @param config     project keeper configuration passed to the {@link LanguageSpecificSourceAnalyzer}s if necessary
     * @param mvnRepo    the path to the maven repository, may be {@code null} if the default should be used
     * @param ownVersion the version of this running project keeper instance
     * @return a new configured {@link SourceAnalyzer}
     */
    public static SourceAnalyzer create(final ProjectKeeperConfig config, final Path mvnRepo, final String ownVersion) {
        return new SourceAnalyzer(getLanguageSpecificSourceAnalyzers(config, mvnRepo, ownVersion));
    }

    private static Map<SourceType, LanguageSpecificSourceAnalyzer> getLanguageSpecificSourceAnalyzers(
            final ProjectKeeperConfig config, final Path mvnRepo, final String ownVersion) {
        return Map.of( //
                SourceType.MAVEN, new MavenSourceAnalyzer(mvnRepo, ownVersion), //
                SourceType.GOLANG, new GolangSourceAnalyzer(config), //
                SourceType.NPM, new NpmSourceAnalyzer());
    }

    /**
     * Analyze a list of source projects, potentially of mixed type (e.g. Maven and Golang).
     *
     * @param projectDir project directory
     * @param sources    configured sources
     * @return analyzed sources in the same order as the input
     */
    public List<AnalyzedSource> analyze(final Path projectDir, final List<Source> sources) {
        validateUniqueSourcePaths(sources);
        final Map<Path, AnalyzedSource> analyzedSources = analyzeSources(projectDir, sources);
        return fixOrder(sources, analyzedSources);
    }

    private Map<Path, AnalyzedSource> analyzeSources(final Path projectDir, final List<Source> sources) {
        final Map<SourceType, List<Source>> groupedSources = sources.stream().collect(groupingBy(Source::getType));
        final Map<Path, AnalyzedSource> analyzedSources = new HashMap<>(sources.size());
        for (final Entry<SourceType, List<Source>> entry : groupedSources.entrySet()) {
            final LanguageSpecificSourceAnalyzer analyzer = getAnalyzer(entry.getKey());
            analyzer.analyze(projectDir, entry.getValue())
                    .forEach(source -> analyzedSources.put(source.getPath(), source));
        }
        return analyzedSources;
    }

    private List<AnalyzedSource> fixOrder(final List<Source> sources, final Map<Path, AnalyzedSource> analyzedSources) {
        return sources.stream().map(source -> findSource(source, analyzedSources)).collect(toList());
    }

    private AnalyzedSource findSource(final Source source, final Map<Path, AnalyzedSource> analyzedSources) {
        final AnalyzedSource analyzedSource = analyzedSources.get(source.getPath());
        if (analyzedSource != null) {
            return analyzedSource;
        } else {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-150").message(
                    "Did not find analyzed source for path {{path}}, all analyzed sources: {{analyzed sources}}",
                    source.getPath(), analyzedSources).ticketMitigation().toString());
        }
    }

    private void validateUniqueSourcePaths(final List<Source> sources) {
        final Map<Path, Long> sourceFrequencies = sources.stream().map(Source::getPath)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        final List<Path> duplicatePaths = sourceFrequencies.entrySet().stream() //
                .filter(entry -> entry.getValue() > 1) //
                .map(Entry::getKey).collect(toList());
        if (!duplicatePaths.isEmpty()) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-149")
                    .message("Sources use duplicate paths {{duplicate paths|u}}.", duplicatePaths)
                    .mitigation("Make sure that sources in .project-keeper.yml have unique paths.").toString());
        }
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