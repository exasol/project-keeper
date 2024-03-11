package com.exasol.projectkeeper.githuboutput;

interface WorkflowOutput extends AutoCloseable {
    void publish(String key, String value);

    @Override
    void close();
}
