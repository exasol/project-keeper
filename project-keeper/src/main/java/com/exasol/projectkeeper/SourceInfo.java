package com.exasol.projectkeeper;

import lombok.Data;

@Data
public class SourceInfo {
    private final String name;
    private final String version;
    private final AggregationType aggregationType;

    private enum AggregationType {
        /** Simple project. Not aggregated or aggregator. */
        STANDALONE,
        /** This project is run by a different project's build. */
        AGGREGATED,
        /** This project runs other project during its build. */
        AGGREGATOR
    }
}
