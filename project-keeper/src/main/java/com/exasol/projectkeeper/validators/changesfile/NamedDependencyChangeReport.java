package com.exasol.projectkeeper.validators.changesfile;

import com.exasol.projectkeeper.validators.changesfile.dependencies.model.DependencyChangeReport;

import lombok.Data;

@Data
public class NamedDependencyChangeReport {
    private final String projectName;
    private final DependencyChangeReport report;
}
