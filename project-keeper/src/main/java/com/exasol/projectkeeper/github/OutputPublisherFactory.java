package com.exasol.projectkeeper.github;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

/**
 * This factory creates new {@link WorkflowOutput}. If environment variable {@code GITHUB_OUTPUT} is present, the
 * created publisher will append to the referenced file. If the environment variable is not present, the publisher won't
 * publish anything.
 */
class OutputPublisherFactory {
    private final Map<String, String> environment;

    OutputPublisherFactory(final Map<String, String> environment) {
        this.environment = environment;
    }

    WorkflowOutput create() {
        return Optional.ofNullable(environment.get("GITHUB_OUTPUT")) //
                .map(Path::of) //
                .map(FileContentProvider::create) //
                .orElseGet(NullContentProvider::new);
    }
}
