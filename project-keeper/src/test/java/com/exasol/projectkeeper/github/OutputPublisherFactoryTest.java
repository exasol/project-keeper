package com.exasol.projectkeeper.github;

import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.github.*;

class OutputPublisherFactoryTest {

    @Test
    void nullOutputPublisher() {
        assertThat(new OutputPublisherFactory(emptyMap()).create(), instanceOf(NullContentProvider.class));
    }

    // [utest->dsn~verify-modes.output-parameters~1]
    @Test
    void fileOutputPublisher(@TempDir final Path tempDir) {
        assertThat(new OutputPublisherFactory(Map.of("GITHUB_OUTPUT", tempDir.resolve("file").toString())).create(),
                instanceOf(FileContentProvider.class));
    }
}
