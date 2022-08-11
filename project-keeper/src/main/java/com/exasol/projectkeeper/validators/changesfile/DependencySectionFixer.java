package com.exasol.projectkeeper.validators.changesfile;

import static com.exasol.projectkeeper.validators.changesfile.ChangesFile.DEPENDENCY_UPDATES_HEADING;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.changesfile.dependencies.DependencyChangeReportRenderer;

/**
 * This class fixes the dependency section of a {@link ChangesFile}.
 */
//[impl->dsn~dependency-section-in-changes_x.x.x.md-file-validator~1]
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
                .collect(Collectors.toList());
        final List<String> renderedReport = new DependencyChangeReportRenderer().render(reports);
        final List<ChangesFileSection> sections = new ArrayList<>(changesFile.getSections());
        removeDependencySection(sections);
        if (!renderedReport.isEmpty()) {
            sections.add(new ChangesFileSection(renderedReport));
        }
        return new ChangesFile(List.copyOf(changesFile.getHeaderSectionLines()), sections);
    }

    private NamedDependencyChangeReport getDependencyChangesOfSource(final AnalyzedSource source) {
        return new NamedDependencyChangeReport(source.getProjectName(), source.getDependencyChanges());
    }

    private void removeDependencySection(final List<ChangesFileSection> sections) {
        sections.removeIf(section -> section.getHeading().compareToIgnoreCase(DEPENDENCY_UPDATES_HEADING) == 0);
    }
}
