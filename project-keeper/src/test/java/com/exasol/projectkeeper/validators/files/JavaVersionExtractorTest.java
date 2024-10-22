package com.exasol.projectkeeper.validators.files;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.sources.*;

class JavaVersionExtractorTest {

    @Test
    void noSources() {
        assertThat(extract(), contains("17"));
    }

    @Test
    void nonJavaSource() {
        assertThat(extract(AnalyzedSourceImpl.builder().build()), contains("17"));
    }

    @Test
    void javaSource() {
        assertThat(extract(mavenSource("11")), contains("11", "17"));
    }

    @Test
    void sortedAscending() {
        assertThat(extract(mavenSource("21"), mavenSource("17"), mavenSource("11")), contains("11", "17", "21"));
    }

    @Test
    void removesDuplicates() {
        assertThat(extract(mavenSource("17"), mavenSource("17"), mavenSource("11")), contains("11", "17"));
    }

    @Test
    void sortsNumerically() {
        assertThat(extract(mavenSource("17.9"), mavenSource("8"), mavenSource("1.8"), mavenSource("17")),
                contains("1.8", "8", "17", "17.9"));
    }

    AnalyzedMavenSource mavenSource(final String javaVersion) {
        return AnalyzedMavenSource.builder().javaVersion(javaVersion).build();
    }

    List<String> extract(final AnalyzedSource... sources) {
        return new JavaVersionExtractor(asList(sources)).extractVersions();
    }
}
