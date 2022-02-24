package com.exasol.projectkeeper.validators.changesfile;

import static com.exasol.projectkeeper.validators.changesfile.ChangesFile.DEPENDENCY_UPDATES_HEADING;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.JavaProjectCrawlerRunner;
import com.exasol.projectkeeper.shared.model.DependencyChangeReport;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.changesfile.dependencies.DependencyChangeReportRenderer;

/**
 * This class fixes the dependency section of a {@link ChangesFile}.
 */
//[impl->dsn~dependency-section-in-changes_x.x.x.md-file-validator~1]
class DependencySectionFixer {
    private final JavaProjectCrawlerRunner javaProjectCrawlerRunner;
    private final List<AnalyzedSource> sources;

    /**
     * Create a new instance of {@link DependencySectionFixer}.
     *
     * @param sources               source projects
     * @param mvnRepositoryOverride maven repository override. USe {@code null} for default
     * @param ownVersion            project keeper-version
     */
    public DependencySectionFixer(final List<AnalyzedSource> sources, final Path mvnRepositoryOverride,
            final String ownVersion) {
        this.sources = sources;
        this.javaProjectCrawlerRunner = new JavaProjectCrawlerRunner(mvnRepositoryOverride, ownVersion);
    }

    /**
     * Fix the dependency section of a changes file.
     * 
     * @param changesFile changes file to fix
     * @return fixed changes file.
     */
    public ChangesFile fix(final ChangesFile changesFile) {
        final List<NamedDependencyChangeReport> reports = this.sources.stream().map(this::getDependencyChangesOfSource)
                .collect(Collectors.toList());
        final List<String> renderedReport = new DependencyChangeReportRenderer().render(reports);
        final List<ChangesFileSection> sections = new ArrayList<>(changesFile.getSections());
        removeDependencySection(sections);
        sections.add(new ChangesFileSection(renderedReport));
        return new ChangesFile(List.copyOf(changesFile.getHeaderSectionLines()), sections);
    }

    private NamedDependencyChangeReport getDependencyChangesOfSource(final AnalyzedSource source) {
        if (source instanceof AnalyzedMavenSource) {
            final String projectName = ((AnalyzedMavenSource) source).getProjectName();
            final Path path = source.getPath();
            final DependencyChangeReport dependencyChanges = this.javaProjectCrawlerRunner.getDependencyChanges(path);
            return new NamedDependencyChangeReport(projectName, dependencyChanges);
        } else {
            throw new UnsupportedOperationException(ExaError.messageBuilder("E-PK-CORE-96")
                    .message("Analyzing dependency changes is not yet implemented for {{source type}}.",
                            source.getClass().getSimpleName())
                    .toString());
        }
    }

    private void removeDependencySection(final List<ChangesFileSection> sections) {
        sections.removeIf(section -> section.getHeading().compareToIgnoreCase(DEPENDENCY_UPDATES_HEADING) == 0);
    }
}
