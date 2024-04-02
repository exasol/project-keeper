package com.exasol.projectkeeper.github;

/**
 * This {@link WorkflowOutput} is used by {@link OutputPublisherFactory} when environment variable {@code GITHUB_OUTPUT}
 * is not present. This class just logs published key/value pairs and does not actually publish them.
 */
class NullContentProvider implements WorkflowOutput {

    @Override
    public void publish(final String key, final String value) {
        // Ignore
    }

    @Override
    public void close() {
        // Ignore
    }
}
