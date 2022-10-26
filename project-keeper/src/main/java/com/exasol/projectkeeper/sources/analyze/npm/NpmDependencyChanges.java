package com.exasol.projectkeeper.sources.analyze.npm;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChange;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport.Builder;
import com.exasol.projectkeeper.sources.analyze.generic.DependencyChanges;

/**
 * Services for handling NPM modules
 */
public class NpmDependencyChanges {

    private final PackageJson current;

    NpmDependencyChanges(final PackageJson packageJson) {
        this.current = packageJson;
    }

    DependencyChangeReport getReport() {
        final Optional<PackageJson> previous = this.current.previousRelease() //
                .fileContent(Paths.get(PackageJsonReader.FILENAME)) //
                .map(PackageJsonReader::readFull);
        final Builder builder = DependencyChangeReport.builder();
        for (final Type type : Type.values()) {
            builder.typed(type, getChanges(previous, type));
        }
        return builder.build();
    }

    private List<DependencyChange> getChanges(final Optional<PackageJson> previous, final Type type) {
        return DependencyChanges.builder() //
                .from(previous.map(p -> p.getDependencies(type))) //
                .to(this.current.getDependencies(type)) //
                .build();
    }
}
