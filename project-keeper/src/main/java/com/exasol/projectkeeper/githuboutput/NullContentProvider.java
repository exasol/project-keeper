package com.exasol.projectkeeper.githuboutput;

import java.util.logging.Logger;

/**
 * This {@link WorkflowOutput} is used by {@link OutputPublisherFactory} when environment variable {@code GITHUB_OUTPUT}
 * is not present. This class just logs published key/value pairs and does not actually publish them.
 */
class NullContentProvider implements WorkflowOutput {
    private static final Logger LOG = Logger.getLogger(NullContentProvider.class.getName());

    @Override
    public void publish(final String key, final String value) {
        LOG.fine(() -> "Publishing key/value pair '" + key + "' = '" + value + "'");
    }

    @Override
    public void close() {
        // Ignore
    }
}
