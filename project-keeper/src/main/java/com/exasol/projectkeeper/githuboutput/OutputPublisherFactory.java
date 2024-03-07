package com.exasol.projectkeeper.githuboutput;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

class OutputPublisherFactory {
    private final Map<String, String> environment;

    OutputPublisherFactory(final Map<String, String> environment) {
        this.environment = environment;
    }

    OutputPublisher create() {
        return Optional.ofNullable(environment.get("GITHUB_OUTPUT")) //
                .map(Path::of) //
                .map(FileOutputPublisher::create) //
                .orElseGet(NullOutputPublisher::new);
    }
}
