package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

class ResourceReaderTest {
    private static final String TEST_RESOURCE = "resource-reader-test-resource.txt";

    @Test
    void readFromResource() {
        assertThat(testee().readFromResource(TEST_RESOURCE),
                equalTo("first line" + System.lineSeparator() + "second line" + System.lineSeparator()));
    }

    @Test
    void readFromResourceFailsForMissingResource() {
        final ResourceReader reader = testee();
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> reader.readFromResource("missing-resource.txt"));
        assertThat(exception.getMessage(), equalTo(
                "F-PK-CORE-213: Template not found for resource name 'missing-resource.txt'."
                        + " This is an internal error that should not happen."
                        + " Please report it by opening a GitHub issue."));
    }

    @Test
    void readFromResourceFailsWhenReadingResourceFails() {
        final ResourceReader reader = new FailingResourceReader();
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> reader.readFromResource(TEST_RESOURCE));
        assertThat(exception.getMessage(), equalTo(
                "F-PK-CORE-57: Failed to read template from resource 'resource-reader-test-resource.txt'."
                        + " This is an internal error that should not happen."
                        + " Please report it by opening a GitHub issue."));
        assertThat(exception.getCause(), instanceOf(IOException.class));
    }

    private ResourceReader testee() {
        return new ResourceReader();
    }

    private static class FailingResourceReader extends ResourceReader {
        @Override
        InputStream getResourceAsStream(final String resourceName) {
            return new InputStream() {
                @Override
                public int read() throws IOException {
                    throw new IOException("simulated failure");
                }
            };
        }
    }
}
