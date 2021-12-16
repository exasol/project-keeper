package com.exasol.projectkeeper.validators.changelog;

import static com.vdurmont.semver4j.Semver.SemverType.LOOSE;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.vdurmont.semver4j.Semver;

/**
 * This class generates the content for the changelog file.
 */
class ChangelogFileGenerator {
    private static final String NL = System.lineSeparator();

    /**
     * Generate the content for the changelog file.
     * 
     * @param versions list of project versions
     * @return content for the changelog file
     */
    String generate(final List<String> versions) {
        final List<String> sortedVersions = versions.stream().map(version -> new Semver(version, LOOSE))
                .sorted(Comparator.reverseOrder()).map(Semver::getValue).collect(Collectors.toList());
        final StringBuilder templateBuilder = new StringBuilder();
        templateBuilder.append("# Changes" + NL + NL);
        for (final String version : sortedVersions) {
            templateBuilder.append("* [" + version + "](changes_" + version + ".md)" + NL);
        }
        return templateBuilder.toString();
    }
}
