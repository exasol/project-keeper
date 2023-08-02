package com.exasol.projectkeeper.validators.changesfile;

import java.util.Objects;

import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;

/**
 * This class combines a {@link DependencyChangeReport} with a source name.
 */
public final class NamedDependencyChangeReport {
    private final String sourceName;
    private final DependencyChangeReport report;

    /**
     * Create a new instance.
     * 
     * @param sourceName source name
     * @param report     report
     */
    public NamedDependencyChangeReport(final String sourceName, final DependencyChangeReport report) {
        this.sourceName = sourceName;
        this.report = report;
    }

    /** @return source name */
    public String getSourceName() {
        return sourceName;
    }

    /** @return report */
    public DependencyChangeReport getReport() {
        return report;
    }

    @Override
    public String toString() {
        return "NamedDependencyChangeReport [sourceName=" + sourceName + ", report=" + report + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceName, report);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NamedDependencyChangeReport other = (NamedDependencyChangeReport) obj;
        return Objects.equals(sourceName, other.sourceName) && Objects.equals(report, other.report);
    }
}
