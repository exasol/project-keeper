package com.exasol.projectkeeper.validators.changesfile.dependencies;

import static com.exasol.projectkeeper.ApStyleFormatter.capitalizeApStyle;

import java.util.ArrayList;
import java.util.List;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChange;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.changesfile.NamedDependencyChangeReport;

/**
 * String renderer for {@link DependencyChangeReport}.
 */
public class DependencyChangeReportRenderer {

    /**
     * Render a {@link DependencyChangeReport} to string.
     *
     * @param reports reports to render
     * @return rendered report as a list of lines
     */
    public List<String> render(final List<NamedDependencyChangeReport> reports) {
        final List<String> content = renderContent(reports);
        final List<String> lines = new ArrayList<>();
        if (content.isEmpty()) {
            return lines;
        }
        lines.add(ChangesFile.DEPENDENCY_UPDATES_HEADING);
        lines.addAll(content);
        return lines;
    }

    private List<String> renderContent(final List<NamedDependencyChangeReport> reports) {
        final List<String> content = new ArrayList<>();
        final boolean isMultiReports = reports.size() > 1;
        for (final NamedDependencyChangeReport report : reports) {
            content.addAll(renderProject(report, isMultiReports));
        }
        return content;
    }

    private List<String> renderProject(final NamedDependencyChangeReport namedReport, final boolean isMultiReports) {
        final List<String> lines = new ArrayList<>();
        final DependencyChangeReport report = namedReport.getReport();
        final String headlinePrefix = isMultiReports ? "#### " : "### ";

        for (final Type type : Type.values()) {
            lines.addAll(renderDependencyChanges(headlinePrefix + type.getHeader(), report.getChanges(type)));
        }
        if (!lines.isEmpty() && isMultiReports) {
            lines.addAll(0, List.of("", "### " + capitalizeApStyle(namedReport.getSourceName())));
        }
        return lines;
    }

    private List<String> renderDependencyChanges(final String heading, final List<DependencyChange> dependencyChanges) {
        final List<String> lines = new ArrayList<>();
        if (!dependencyChanges.isEmpty()) {
            lines.add("");
            lines.add(heading);
            lines.add("");
            final var dependencyChangeRenderer = new DependencyChangeRenderer();
            for (final DependencyChange dependencyChange : dependencyChanges) {
                lines.add(dependencyChangeRenderer.render(dependencyChange));
            }
        }
        return lines;
    }
}
