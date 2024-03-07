package com.exasol.projectkeeper.githuboutput;

interface OutputPublisher extends AutoCloseable {
    void publish(String key, String value);

    @Override
    void close();
}
