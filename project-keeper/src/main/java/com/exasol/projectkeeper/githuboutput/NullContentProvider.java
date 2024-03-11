package com.exasol.projectkeeper.githuboutput;

import java.util.logging.Logger;

class NullContentProvider implements OutputPublisher {
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
