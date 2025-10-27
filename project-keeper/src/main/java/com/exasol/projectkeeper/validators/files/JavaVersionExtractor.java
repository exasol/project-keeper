package com.exasol.projectkeeper.validators.files;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toSet;

import java.util.*;

import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;

class JavaVersionExtractor {

    private static final String MAVEN_BUILD_JAVA_VERSION = "17";
    private final List<AnalyzedSource> sources;

    JavaVersionExtractor(final List<AnalyzedSource> sources) {
        this.sources = sources;
    }

    List<String> extractVersions() {
        final Set<String> javaVersions = new HashSet<>();
        javaVersions.addAll(getSourceJavaVersions());
        javaVersions.add(MAVEN_BUILD_JAVA_VERSION);
        return javaVersions.stream() //
                .sorted(comparing(JavaVersionExtractor::parseVersion)) //
                .toList();
    }

    private static double parseVersion(final String version) {
        final int firstDotIndex = version.indexOf('.');
        if (firstDotIndex < 0) {
            return Integer.parseInt(version);
        }
        final int secondDotIndex = version.indexOf('.', firstDotIndex + 1);
        if (secondDotIndex <= 0) {
            return Double.parseDouble(version);
        }
        return Double.parseDouble(version.substring(0, secondDotIndex));
    }

    private Set<String> getSourceJavaVersions() {
        if (sources.isEmpty()) {
            return Set.of(MAVEN_BUILD_JAVA_VERSION);
        }
        return sources.stream()
                .filter(AnalyzedMavenSource.class::isInstance)
                .map(AnalyzedMavenSource.class::cast)
                .map(AnalyzedMavenSource::getJavaVersion)
                .collect(toSet());
    }

    String getNextVersion() {
        final double current = getCurrentLatestVersion();
        if (current < 11) {
            return "11";
        }
        if (current < 17) {
            return "17";
        }
        if (current < 21) {
            return "21";
        }
        return "25";
    }

    private double getCurrentLatestVersion() {
        return getSourceJavaVersions().stream() //
                .map(JavaVersionExtractor::parseVersion) //
                .reduce(JavaVersionExtractor::takeLast) //
                .orElseThrow(() -> new IllegalStateException(
                        "No latest java version found in source java versions " + getSourceJavaVersions()));
    }

    private static double takeLast(final double first, final double second) {
        return second;
    }
}
