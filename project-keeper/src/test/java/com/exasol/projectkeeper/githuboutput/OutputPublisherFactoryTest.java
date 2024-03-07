package com.exasol.projectkeeper.githuboutput;

import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.util.Map;

import org.junit.jupiter.api.Test;

class OutputPublisherFactoryTest {

    @Test
    void nullOutputPublisher() {
        assertThat(new OutputPublisherFactory(emptyMap()).create(), instanceOf(NullOutputPublisher.class));
    }

    // [utest->dsn~verify-modes.output-parameters~1]
    @Test
    void fileOutputPublisher() {
        assertThat(new OutputPublisherFactory(Map.of("GITHUB_OUTPUT", "file")).create(),
                instanceOf(FileOutputPublisher.class));
    }
}
