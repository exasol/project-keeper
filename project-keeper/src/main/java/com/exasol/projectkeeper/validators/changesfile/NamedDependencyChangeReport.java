package com.exasol.projectkeeper.validators.changesfile;

import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;

/**
 * This class combines a {@link DependencyChangeReport} with a source name.
 * 
 * @param sourceName source name
 * @param report     report
 */
public record NamedDependencyChangeReport(String sourceName, DependencyChangeReport report) {

    /**
     * Get the source name.
     * 
     * @return source name
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * Get the report.
     * 
     * @return report
     */
    public DependencyChangeReport getReport() {
        return report;
    }
}
