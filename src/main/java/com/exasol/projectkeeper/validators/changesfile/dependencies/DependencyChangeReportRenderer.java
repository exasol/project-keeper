package com.exasol.projectkeeper.validators.changesfile.dependencies;

import java.util.ArrayList;
import java.util.List;

import com.exasol.projectkeeper.validators.changesfile.ChangesFile;

/**
 * String renderer for {@link DependencyChangeReport}.
 */
public class DependencyChangeReportRenderer {

    /**
     * Render a {@link DependencyChangeReport} to string.
     * 
     * @param report report to render
     * @return rendered report as a list of lines
     */
    public List<String> render(final DependencyChangeReport report) {
        final List<String> lines = new ArrayList<>();
        lines.add(ChangesFile.DEPENDENCY_UPDATES_HEADING);
        addDependencyChanges("### Compile Dependency Updates", report.getCompileDependencyChanges(), lines);
        addDependencyChanges("### Runtime Dependency Updates", report.getRuntimeDependencyChanges(), lines);
        addDependencyChanges("### Test Dependency Updates", report.getTestDependencyChanges(), lines);
        addDependencyChanges("### Plugin Dependency Updates", report.getPluginDependencyChanges(), lines);
        return lines;
    }

    private void addDependencyChanges(final String heading, final List<DependencyChange> dependencyChanges,
            final List<String> lines) {
        if (!dependencyChanges.isEmpty()) {
            lines.add("");
            lines.add(heading);
            lines.add("");
            final DependencyChangeRenderer dependencyChangeRenderer = new DependencyChangeRenderer();
            for (final DependencyChange dependencyChange : dependencyChanges) {
                lines.add(dependencyChangeRenderer.render(dependencyChange));
            }
        }
    }
}
