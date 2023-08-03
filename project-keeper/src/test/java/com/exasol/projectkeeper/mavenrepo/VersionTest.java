package com.exasol.projectkeeper.mavenrepo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.projectkeeper.mavenrepo.Version.UnsupportedVersionFormatException;

import nl.jqno.equalsverifier.EqualsVerifier;

//[utest->dsn~verify-own-version~1]
class VersionTest {

    @ParameterizedTest(name = "Version(\"{0}\")")
    @CsvSource(value = { "0", "0.1", "02.03", "000.111.222" })
    void validVersionStrings(final String version) throws UnsupportedVersionFormatException {
        final Version testee = new Version(version);
        assertThat(testee, notNullValue());
        assertThat(testee.toString(), equalTo(version));
    }

    @ParameterizedTest(name = "Version(\"{0}\")")
    @CsvSource(value = { "''", "aa", "1.2.c", "1 2", ".1", "000.111.222." })
    void invalidVersionStrings(final String version) throws UnsupportedVersionFormatException {
        assertThrows(UnsupportedVersionFormatException.class, () -> new Version(version));
    }

    @ParameterizedTest(name = "Version(\"{0}\") < Version(\"{1}\") ")
    @CsvSource(delimiter = '<', value = { "0 < 1", "0 < 0.1", "1.2.0 < 1.2.2", "0.1 < 0.2" })
    void versionLessThan(final String a, final String b) throws UnsupportedVersionFormatException {
        assertThat(new Version(a).isGreaterOrEqualThan(new Version(b)), is(false));
        assertThat(new Version(b).isGreaterOrEqualThan(new Version(a)), is(true));
        assertThat(new Version(a), lessThan(new Version(b)));
        assertThat(new Version(b), greaterThan(new Version(a)));
    }

    @ParameterizedTest(name = "Version(\"{0}\") = Version(\"{1}\") ")
    @CsvSource(delimiter = '=', value = { "0 = 0", "1.1 = 01.1", "1.2.3 = 01.02.03" })
    void versionEqual(final String a, final String b) throws UnsupportedVersionFormatException {
        assertThat(new Version(a).compareTo(new Version(b)), is(0));
    }

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(Version.class).verify();
    }
}
