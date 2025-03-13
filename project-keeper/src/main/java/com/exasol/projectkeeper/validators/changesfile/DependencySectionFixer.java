package com.exasol.projectkeeper.validators.changesfile;

import java.util.List;
import java.util.Optional;

import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.changesfile.dependencies.DependencyChangeReportRenderer;

/**
 * This class fixes the dependency section of a {@link ChangesFile}.
 */
// [impl->dsn~dependency-section-in-changes_x.x.x.md-file-validator~1]
class DependencySectionFixer {
    private final List<AnalyzedSource> sources;

    /**
     * Create a new instance of {@link DependencySectionFixer}.
     *
     * @param sources source projects
     *
     */
    public DependencySectionFixer(final List<AnalyzedSource> sources) {
        this.sources = sources;
    }

    /**
     * Fix the dependency section of a changes file.
     *
     * @param changesFile changes file to fix
     * @return fixed changes file.
     */
    public ChangesFile fix(final ChangesFile changesFile) {
        final List<NamedDependencyChangeReport> reports = this.sources.stream().map(this::getDependencyChangesOfSource)
                .toList();
        final Optional<ChangesFileSection> dependencyChanges = new DependencyChangeReportRenderer().render(reports);
        return changesFile.toBuilder() //
                .dependencyChangeSection(dependencyChanges.orElse(null)).build();
    }

    private NamedDependencyChangeReport getDependencyChangesOfSource(final AnalyzedSource source) {
        return new NamedDependencyChangeReport(source.getProjectName(), source.getDependencyChanges());
    }
}
