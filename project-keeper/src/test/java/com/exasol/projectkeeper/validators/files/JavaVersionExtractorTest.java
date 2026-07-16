package com.exasol.projectkeeper.validators.files;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.projectkeeper.sources.*;

class JavaVersionExtractorTest {

    private static final String MVN_JAVA_VERSION = "21";

    @Test
    void noSources() {
        assertThat(extract(), contains(MVN_JAVA_VERSION));
    }

    @Test
    void nonJavaSource() {
        assertThat(extract(AnalyzedSourceImpl.builder().build()), contains(MVN_JAVA_VERSION));
    }

    @Test
    void javaSource() {
        assertThat(extract(mavenSource("11")), contains("11", MVN_JAVA_VERSION));
    }

    @Test
    void versionWithDot() {
        assertThat(extract(mavenSource("1.2")), contains("1.2", MVN_JAVA_VERSION));
    }

    @Test
    void versionWithTwoDot() {
        assertThat(extract(mavenSource("1.2.3")), contains("1.2.3", MVN_JAVA_VERSION));
    }

    @Test
    void sortedAscending() {
        assertThat(extract(mavenSource("21"), mavenSource(MVN_JAVA_VERSION), mavenSource("11")), contains("11", MVN_JAVA_VERSION));
    }

    @Test
    void removesDuplicates() {
        assertThat(extract(mavenSource(MVN_JAVA_VERSION), mavenSource(MVN_JAVA_VERSION), mavenSource("11")), contains("11", MVN_JAVA_VERSION));
    }

    @Test
    void sortsNumerically() {
        assertThat(extract(mavenSource("17.9"), mavenSource("8"), mavenSource("1.8"), mavenSource(MVN_JAVA_VERSION)),
                contains("1.8", "8", "17.9", MVN_JAVA_VERSION));
    }

    @Test
    void getNextVersionIgnoresOlderVersions() {
        assertThat(nextVersion(mavenSource("21"), mavenSource("11")), equalTo("25"));
    }

    @ParameterizedTest
    @CsvSource({ "8, 11", "9, 11", "11, 17", "12, 17", "17, 21", "17.1, 21", "21, 25", "22, 25", "24, 25",
            "25, 26", "26, 26", "27, 26" })
    void getNextVersion(final String currentVersion, final String nextVersion) {
        assertThat("Next version for " + currentVersion, nextVersion(mavenSource(currentVersion)), equalTo(nextVersion));
    }

    AnalyzedMavenSource mavenSource(final String javaVersion) {
        return AnalyzedMavenSource.builder().javaVersion(javaVersion).build();
    }

    List<String> extract(final AnalyzedSource... sources) {
        return new JavaVersionExtractor(asList(sources)).extractVersions();
    }

    String nextVersion(final AnalyzedSource... sources) {
        return new JavaVersionExtractor(asList(sources)).getNextVersion();
    }
}
