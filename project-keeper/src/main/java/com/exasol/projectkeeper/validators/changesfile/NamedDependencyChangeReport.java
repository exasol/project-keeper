package com.exasol.projectkeeper.validators.changesfile;

import com.exasol.projectkeeper.shared.model.DependencyChangeReport;

import lombok.Data;

/**
 * This class combines a {@link DependencyChangeReport} with a source name.
 */
@Data
public class NamedDependencyChangeReport {
    private final String sourceName;
    private final DependencyChangeReport report;
}
