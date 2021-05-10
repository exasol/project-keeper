package com.exasol.projectkeeper.validators.dependencies.renderer;

import static com.exasol.projectkeeper.validators.dependencies.ProjectDependency.Type.*;

import java.util.List;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.validators.dependencies.License;
import com.exasol.projectkeeper.validators.dependencies.ProjectDependency;

import net.steppschuh.markdowngenerator.table.Table;

/**
 * String renderer for a dependencies report.
 */
public class DependencyPageRenderer {
    private static final String NEWLINE = System.lineSeparator();

    /**
     * Convert a list of dependencies into a markdown dependency report.
     * 
     * @param dependencies list of dependencies
     * @return rendered report
     */
    public String render(final List<ProjectDependency> dependencies) {
        final var markdownReferenceBuilder = new MarkdownReferenceBuilder();
        final var reportBuilder = new StringBuilder();
        reportBuilder.append("<!-- @formatter:off -->" + NEWLINE);
        reportBuilder.append("# Dependencies" + NEWLINE);
        reportBuilder.append(buildDependencySectionForScope(dependencies, COMPILE, markdownReferenceBuilder));
        reportBuilder.append(buildDependencySectionForScope(dependencies, TEST, markdownReferenceBuilder));
        reportBuilder.append(buildDependencySectionForScope(dependencies, RUNTIME, markdownReferenceBuilder));
        reportBuilder.append(buildDependencySectionForScope(dependencies, PLUGIN, markdownReferenceBuilder));
        reportBuilder.append(NEWLINE);
        reportBuilder.append(markdownReferenceBuilder.getReferences());
        return reportBuilder.toString();
    }

    private String buildDependencySectionForScope(final List<ProjectDependency> dependencies,
            final ProjectDependency.Type type, final MarkdownReferenceBuilder markdownReferenceBuilder) {
        final List<ProjectDependency> dependenciesOfThisScope = dependencies.stream()
                .filter(dependency -> dependency.getType().equals(type)).collect(Collectors.toList());
        if (dependenciesOfThisScope.isEmpty()) {
            return "";
        } else {
            final String heading = "## " + capitalizeFirstLetter(type.name()) + " Dependencies";
            return NEWLINE + heading + NEWLINE + NEWLINE + buildTable(dependenciesOfThisScope, markdownReferenceBuilder)
                    + NEWLINE;
        }
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
        return renderLink(license.getName(), license.getUrl(), markdownReferenceBuilder);
    }

    private String renderLink(final String name, final String url,
            final MarkdownReferenceBuilder markdownReferenceBuilder) {
        if (url == null || url.isBlank()) {
            return name;
        } else {
            return "[" + name + "][" + markdownReferenceBuilder.getReferenceForUrl(name, url) + "]";
        }
    }

    private String capitalizeFirstLetter(final String string) {
        final var lowerCase = string.toLowerCase();
        final var firstLetter = lowerCase.substring(0, 1);
        return firstLetter.toUpperCase() + lowerCase.substring(1);
    }
}
