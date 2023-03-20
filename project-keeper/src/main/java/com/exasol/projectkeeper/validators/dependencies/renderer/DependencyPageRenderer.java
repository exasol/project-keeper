package com.exasol.projectkeeper.validators.dependencies.renderer;

import static com.exasol.projectkeeper.ApStyleFormatter.capitalizeApStyle;
import static com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.shared.dependencies.*;
import com.exasol.projectkeeper.validators.dependencies.ProjectWithDependencies;

import net.steppschuh.markdowngenerator.table.Table;

/**
 * String renderer for a dependencies report.
 */
public class DependencyPageRenderer {
    private static final String NEWLINE = System.lineSeparator();

    /**
     * Convert a list of dependencies into a markdown dependency report.
     *
     * @param projectWithDependencies projects to report
     * @return rendered report
     */
    public String render(final List<ProjectWithDependencies> projectWithDependencies) {
        final var markdownReferenceBuilder = new MarkdownReferenceBuilder();
        final var reportBuilder = new StringBuilder();
        reportBuilder.append("<!-- @formatter:off -->" + NEWLINE);
        reportBuilder.append("# Dependencies" + NEWLINE);
        for (final ProjectWithDependencies project : projectWithDependencies) {
            final boolean isMultiSourceProject = projectWithDependencies.size() > 1;
            reportBuilder
                    .append(buildDependencySectionForProject(project, markdownReferenceBuilder, isMultiSourceProject));
        }
        reportBuilder.append(NEWLINE);
        reportBuilder.append(markdownReferenceBuilder.getReferences());
        return reportBuilder.toString();
    }

    private String buildDependencySectionForProject(final ProjectWithDependencies project,
            final MarkdownReferenceBuilder markdownRefBuilder, final boolean isMultiProject) {
        final var reportBuilder = new StringBuilder();
        if (isMultiProject) {
            reportBuilder.append(NEWLINE + makeHeadline(project.getProjectName(), 2) + NEWLINE);
        }
        final List<ProjectDependency> dependencies = project.getDependencies();
        final int headlineLevel = isMultiProject ? 3 : 2;
        reportBuilder.append(buildDependencySectionForScope(dependencies, COMPILE, markdownRefBuilder, headlineLevel));
        reportBuilder.append(buildDependencySectionForScope(dependencies, TEST, markdownRefBuilder, headlineLevel));
        reportBuilder.append(buildDependencySectionForScope(dependencies, RUNTIME, markdownRefBuilder, headlineLevel));
        reportBuilder.append(buildDependencySectionForScope(dependencies, PLUGIN, markdownRefBuilder, headlineLevel));
        return reportBuilder.toString();
    }

    private String buildDependencySectionForScope(final List<ProjectDependency> dependencies,
            final BaseDependency.Type type, final MarkdownReferenceBuilder markdownReferenceBuilder,
            final int headlineLevel) {
        final List<ProjectDependency> dependenciesOfThisScope = dependencies.stream()
                .filter(dependency -> dependency.getType().equals(type)).collect(Collectors.toList());
        if (dependenciesOfThisScope.isEmpty()) {
            return "";
        } else {
            final String heading = makeHeadline(type.name() + " Dependencies", headlineLevel);
            return NEWLINE + heading + NEWLINE + NEWLINE + buildTable(dependenciesOfThisScope, markdownReferenceBuilder)
                    + NEWLINE;
        }
    }

    private String makeHeadline(final String text, final int level) {
        return "#".repeat(level) + " " + capitalizeApStyle(text.toLowerCase(Locale.ROOT));
    }

    private String buildTable(final List<ProjectDependency> dependencies,
            final MarkdownReferenceBuilder markdownReferenceBuilder) {
        final var tableBuilder = new Table.Builder().withAlignments(Table.ALIGN_LEFT, Table.ALIGN_LEFT);
        tableBuilder.addRow("Dependency", "License");
        dependencies.forEach(dependency -> {
            final String name = renderDependencyName(dependency, markdownReferenceBuilder);
            final String licenseText = dependency.getLicenses().stream()
                    .map(license -> renderLicense(license, markdownReferenceBuilder)).collect(Collectors.joining("; "));
            tableBuilder.addRow(name, licenseText);
        });
        return tableBuilder.build().toString();
    }

    private String renderDependencyName(final ProjectDependency dependency,
            final MarkdownReferenceBuilder markdownReferenceBuilder) {
        return renderLink(dependency.getName(), dependency.getWebsiteUrl(), markdownReferenceBuilder);
    }

    private String renderLicense(final License license, final MarkdownReferenceBuilder markdownReferenceBuilder) {
        String name = license.getName();
        if (Workarounds.ALTERNATING_DEPENDENCIES.isActive()) {
            name = name.replace("The Apache Software License", "Apache License");
        }
        return renderLink(name, license.getUrl(), markdownReferenceBuilder);
    }

    private String renderLink(final String name, final String url,
            final MarkdownReferenceBuilder markdownReferenceBuilder) {
        if ((url == null) || url.isBlank()) {
            return name;
        } else {
            return "[" + name + "][" + markdownReferenceBuilder.getReferenceForUrl(url) + "]";
        }
    }
}
