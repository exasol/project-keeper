package com.exasol.projectkeeper.github;

interface WorkflowOutput extends AutoCloseable {
    void publish(String key, String value);

    @Override
    void close();
}
