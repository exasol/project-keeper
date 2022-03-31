package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

import java.util.List;

class GoModFile {

    private final GoModFileParser parser;

    public GoModFile(final GoModFileParser parser) {
        this.parser = parser;
    }

    static GoModFile parse(final String content) {
        final GoModFileParser parser = new GoModFileParser();
        parser.parse(content);
        return new GoModFile(parser);
    }

    public String getModuleName() {
        return this.parser.moduleName;
    }

    public String getGoVersion() {
        return this.parser.goVersion;
    }

    public List<GoModDependency> getDependencies() {
        return this.parser.dependencies;
    }

    public List<GoModDependency> getDirectDependencies() {
        return getDependencies().stream() //
                .filter(not(GoModDependency::isIndirect)) //
                .collect(toList());
    }

    public static class GoModDependency {

        private final String name;
        private final String version;
        private final boolean indirect;

        public GoModDependency(final String name, final String version, final boolean indirect) {
            this.name = name;
            this.version = version;
            this.indirect = indirect;
        }

        public String getName() {
            return this.name;
        }

        public String getVersion() {
            return this.version;
        }

        public boolean isIndirect() {
            return this.indirect;
        }
    }
}
