package com.exasol.projectkeeper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// [utest->dsn~excluding-files~1]
class ExcludedFilesMatcherTest {

    @ParameterizedTest
    @CsvSource({ //
            "src/test.json, true", //
            "src/other, false", //
            "doc/some_doc.json, true", //
            "doc/directory/test.json, false", //
            "excluded, true", //
    })
    void testExclusion(final String path, final boolean expectedResult) {
        final ExcludedFilesMatcher matcher = new ExcludedFilesMatcher(List.of("src/test.json", "doc/*", "/excluded"));
        assertThat(matcher.isFileExcluded(Path.of(path)), equalTo(expectedResult));
    }

}