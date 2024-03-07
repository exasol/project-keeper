package com.exasol.projectkeeper.githuboutput;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

// [utest->dsn~verify-modes.output-parameters~1]
class KeyValuePairFormatterTest {

    @ParameterizedTest(name = "Key ''{0}'' / value ''{1}'' formatted to ''{2}''")
    @CsvSource({ //
            "key, value, key=value", //
            "Key, Value, Key=Value", //
            "KEY, VALUE, KEY=VALUE", //
            "key space, value, key space=value", //
            "key, ' value ', 'key= value '", //
            "key, '', 'key='", //
            "key, '\tvalue\t', 'key=\tvalue\t'", //
            "key, 'value with multiple words', key=value with multiple words", //
            "key, 'value\nwith two lines', 'key<<EOF\nvalue\nwith two lines\nEOF'", //
            "key, 'value\n\nlines', 'key<<EOF\nvalue\n\nlines\nEOF'", //
            "key, 'value\n', 'key<<EOF\nvalue\n\nEOF'", //
            "key, '\nvalue', 'key<<EOF\n\nvalue\nEOF'", //
    })
    void format(final String key, final String value, final String expected) {
        assertThat(new KeyValuePairFormatter().format(key, value), equalTo(expected));
    }

    @ParameterizedTest(name = "Key ''{0}'' is illegal")
    @ValueSource(strings = { "", " key", "key ", "\tkey", "key\t", "\nkey", "key\n" })
    void illegalKeys(final String key) {
        final KeyValuePairFormatter formatter = new KeyValuePairFormatter();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> formatter.format(key, "some value"));
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-190: Key '" + key + "' is empty or contains leading or trailing whitespace."));
    }
}
