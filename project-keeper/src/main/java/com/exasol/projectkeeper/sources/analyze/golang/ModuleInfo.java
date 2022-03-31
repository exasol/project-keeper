package com.exasol.projectkeeper.sources.analyze.golang;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModuleInfo {
    private final String moduleName;
    private final List<Dependency> dependencies;

    @Getter
    @Builder
    public static class Dependency {
        private final String moduleName;
        private final String version;

    }
}
