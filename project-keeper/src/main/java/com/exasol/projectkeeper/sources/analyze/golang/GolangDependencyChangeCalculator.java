package com.exasol.projectkeeper.sources.analyze.golang;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChange;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;

class GolangDependencyChangeCalculator {

    static DependencyChangeReport calculateDepencencyChanges(final GolangServices golangServices, final Path projectDir,
            final Source source, final ProjectDependencies dependencies) {
        return new GolangDependencyChangeCalculator(golangServices, projectDir, source, dependencies).calculate();
    }

    static final Logger LOGGER = Logger.getLogger(GolangDependencyChangeCalculator.class.getName());
    private final GolangServices golangServices;
    private final Path projectDir;
    private final Source source;
    private final ProjectDependencies dependencies;
    private List<DependencyChange> changes;

    GolangDependencyChangeCalculator(final GolangServices golangServices, final Path projectDir, final Source source,
            final ProjectDependencies dependencies) {
        this.golangServices = golangServices;
        this.projectDir = projectDir;
        this.source = source;
        this.dependencies = dependencies;
    }

    private DependencyChangeReport calculate() {
        this.changes = this.golangServices.getDependencyChanges(this.projectDir, getRelativeModPath());
        final GolangDependencyType typeDetector = new GolangDependencyType(this.dependencies.getDependencies());
        return DependencyChangeReport.builder() //
                .mixed(this.changes, typeDetector::getType) //
                .build();
    }

    private Path getRelativeModPath() {
        if (this.source.getPath().isAbsolute()) {
            return this.projectDir.relativize(this.source.getPath());
        } else {
            return this.source.getPath();
        }
    }
}