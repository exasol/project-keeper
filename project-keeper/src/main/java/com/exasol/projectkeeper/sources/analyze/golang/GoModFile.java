package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.function.Predicate.not;

import java.util.List;
import java.util.Objects;

import com.exasol.projectkeeper.shared.dependencies.VersionedDependency;

/**
 * This class represents a parsed {@code go.mod} file.
 */
final class GoModFile {
    private final String moduleName;
    private final String goVersion;
    private final List<VersionedDependency> dependencies;

    GoModFile(final String moduleName, final String goVersion, final List<VersionedDependency> dependencies) {
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

    List<VersionedDependency> getDirectDependencies() {
        return this.dependencies.stream() //
                .filter(not(VersionedDependency::isIndirect)) //
                .toList();
    }

    String getModuleName() {
        return moduleName;
    }

    String getGoVersion() {
        return goVersion;
    }

    List<VersionedDependency> getDependencies() {
        return dependencies;
    }

    @Override
    public String toString() {
        return "GoModFile [moduleName=" + moduleName + ", goVersion=" + goVersion + ", dependencies=" + dependencies
                + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleName, goVersion, dependencies);
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
        final GoModFile other = (GoModFile) obj;
        return Objects.equals(moduleName, other.moduleName) && Objects.equals(goVersion, other.goVersion)
                && Objects.equals(dependencies, other.dependencies);
    }
}
