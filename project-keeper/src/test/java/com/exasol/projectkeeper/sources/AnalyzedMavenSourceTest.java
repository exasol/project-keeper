package com.exasol.projectkeeper.sources;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class AnalyzedMavenSourceTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(AnalyzedMavenSource.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(AnalyzedMavenSource.class).verify();
    }

    @Test
    void testDefaultJavaVersion() {
        final AnalyzedMavenSource source = AnalyzedMavenSource.builder().build();
        assertThat(source.getJavaVersion(), equalTo("11"));
    }

    @Test
    void testJavaVersionNullDoesNotOverwriteDefault() {
        final AnalyzedMavenSource source = AnalyzedMavenSource.builder().javaVersion(null).build();
        assertThat(source.getJavaVersion(), equalTo("11"));
    }

    @Test
    void testCustomJavaVersion() {
        final AnalyzedMavenSource source = AnalyzedMavenSource.builder().javaVersion("custom").build();
        assertThat(source.getJavaVersion(), equalTo("custom"));
    }
}
