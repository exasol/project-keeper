package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

import java.util.List;

/**
 * This class represents a parsed {@code go.mod} file.
 */
class GoModFile {

    private final String moduleName;
    private final String goVersion;
    private final List<GoModDependency> dependencies;

    GoModFile(final String moduleName, final String goVersion, final List<GoModDependency> dependencies) {
        this.moduleName = moduleName;
        this.goVersion = goVersion;
        this.dependencies = dependencies;
    }

    private GoModFile(final GoModFileParser parser) {
        this(parser.moduleName, parser.goVersion, parser.dependencies);
    }

    static GoModFile parse(final String content) {
        final GoModFileParser parser = new GoModFileParser();
        parser.parse(content);
        return new GoModFile(parser);
    }

    String getModuleName() {
        return this.moduleName;
    }

    String getGoVersion() {
        return this.goVersion;
    }

    List<GoModDependency> getDependencies() {
        return this.dependencies;
    }

    List<GoModDependency> getDirectDependencies() {
        return getDependencies().stream() //
                .filter(not(GoModDependency::isIndirect)) //
                .collect(toList());
    }

    static class GoModDependency {

        private final String name;
        private final String version;
        private final boolean indirect;

        GoModDependency(final String name, final String version, final boolean indirect) {
            this.name = name;
            this.version = version;
            this.indirect = indirect;
        }

        String getName() {
            return this.name;
        }

        String getVersion() {
            return this.version;
        }

        boolean isIndirect() {
            return this.indirect;
        }
    }
}
