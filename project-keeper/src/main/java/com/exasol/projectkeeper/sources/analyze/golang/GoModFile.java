package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.exasol.projectkeeper.shared.dependencies.VersionedDependency;

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
    private final List<VersionedDependency> dependencies;

    private GoModFile(final GoModFileParser parser) {
        this(parser.moduleName, parser.goVersion, parser.dependencies);
    }

    static GoModFile parse(final String content) {
        final GoModFileParser parser = new GoModFileParser();
        parser.parse(content);
        return new GoModFile(parser);
    }

    List<VersionedDependency> getDirectDependencies() {
        return this.dependencies.stream() //
                .filter(not(VersionedDependency::isIndirect)) //
                .collect(toList());
    }
}
