package com.exasol.projectkeeper.sources.analyze.npm;

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
// [impl -> dsn~npm-changed-dependency~1]
public class NpmDependencyChanges {

    /**
     * Create a new instance.
     * 
     * @param current  current release
     * @param previous previous release or empty optional
     * @return {@link DependencyChangeReport}
     */
    public static DependencyChangeReport report(final PackageJson current, final Optional<PackageJson> previous) {
        return new NpmDependencyChanges(current, previous).getReport();
    }

    private final PackageJson current;
    private final Optional<PackageJson> previous;

    NpmDependencyChanges(final PackageJson packageJson, final Optional<PackageJson> previous) {
        this.current = packageJson;
        this.previous = previous;
    }

    DependencyChangeReport getReport() {
        final Builder builder = DependencyChangeReport.builder();
        for (final Type type : Type.values()) {
            builder.typed(type, getChanges(type));
        }
        return builder.build();
    }

    private List<DependencyChange> getChanges(final Type type) {
        return DependencyChanges.builder() //
                .from(this.previous.map(p -> p.getDependencies(type))) //
                .to(this.current.getDependencies(type)) //
                .build();
    }
}
