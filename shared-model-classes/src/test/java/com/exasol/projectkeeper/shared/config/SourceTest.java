package com.exasol.projectkeeper.shared.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class SourceTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(Source.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(Source.class).verify();
    }

    @ParameterizedTest
    @CsvSource({ //
            "'', true", //
            "pom.xml, true", //
            "my-project/pom.xml, false", //
            "go.mod, true", //
            "subModule/go.mod, false", //
    })
    void testIsRoot(final Path path, final boolean expected) {
        final Source source = Source.builder().path(path).build();
        assertThat(source.isRoot(), is(expected));
    }
}
