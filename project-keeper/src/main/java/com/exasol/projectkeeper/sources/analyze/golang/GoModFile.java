package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents a parsed {@code go.mod} file.
 */
@AllArgsConstructor
@Data
class GoModFile {
    private final String moduleName;
    private final String goVersion;
    private final List<GoModDependency> dependencies;

    private GoModFile(final GoModFileParser parser) {
        this(parser.moduleName, parser.goVersion, parser.dependencies);
    }

    static GoModFile parse(final String content) {
        final GoModFileParser parser = new GoModFileParser();
        parser.parse(content);
        return new GoModFile(parser);
    }

    List<GoModDependency> getDirectDependencies() {
        return getDependencies().stream() //
                .filter(not(GoModDependency::isIndirect)) //
                .collect(toList());
    }

    @AllArgsConstructor
    @Data
    static class GoModDependency {
        private final String name;
        private final String version;
        private final boolean indirect;
    }
}
