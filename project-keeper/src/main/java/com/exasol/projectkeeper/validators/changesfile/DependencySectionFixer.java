package com.exasol.projectkeeper.validators.changesfile;

import static com.exasol.projectkeeper.validators.changesfile.ChangesFile.DEPENDENCY_UPDATES_HEADING;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.exasol.projectkeeper.JavaProjectCrawlerRunner;
import com.exasol.projectkeeper.validators.changesfile.dependencies.DependencyChangeReportRenderer;
import com.exasol.projectkeeper.validators.changesfile.dependencies.model.DependencyChangeReport;

/**
 * This class fixes the dependency section of a {@link ChangesFile}.
 */
//[impl->dsn~dependency-section-in-changes_x.x.x.md-file-validator~1]
class DependencySectionFixer {
    private final DependencyChangeReport dependencyChangeReport;

    /**
     * Create a new instance of {@link DependencySectionFixer}.
     *
     * @param projectDirectory projects root directory
     */
    public DependencySectionFixer(final Path projectDirectory) {
        dependencyChangeReport = new JavaProjectCrawlerRunner(projectDirectory.resolve("pom.xml"))
                .getDependencyChanges();
    }

    /**
     * Fix the dependency section of a changes file.
     * 
     * @param changesFile changes file to fix
     * @return fixed changes file.
     */
    public ChangesFile fix(final ChangesFile changesFile) {
        final List<String> renderedReport = new DependencyChangeReportRenderer().render(dependencyChangeReport);
        final List<ChangesFileSection> sections = new ArrayList<>(changesFile.getSections());
        removeDependencySection(sections);
        sections.add(new ChangesFileSection(renderedReport));
        return new ChangesFile(List.copyOf(changesFile.getHeaderSectionLines()), sections);
    }

    private void removeDependencySection(final List<ChangesFileSection> sections) {
        sections.removeIf(section -> section.getHeading().compareToIgnoreCase(DEPENDENCY_UPDATES_HEADING) == 0);
    }
}
