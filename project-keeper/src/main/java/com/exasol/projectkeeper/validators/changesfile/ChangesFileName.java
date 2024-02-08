package com.exasol.projectkeeper.validators.changesfile;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exasol.projectkeeper.mavenrepo.Version;
import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.Semver.SemverType;

/**
 * Filename of a changes file, e.g. {@code changes_1.2.3.md}.
 */
public final class ChangesFileName implements Comparable<ChangesFileName> {
    /** Regular expression to identify valid names of changes files and to extract version number. **/
    public static final Pattern PATTERN = Pattern.compile("changes_(" + Version.PATTERN.pattern() + ")\\.md");

    /**
     * @param path path to create a {@link ChangesFileName} for
     * @return If path matches regular expression for valid changes filenames then an {@link Optional} containing a new
     *         instance of {@link ChangesFileName}, otherwise {@code Optional.empty()}.
     */
    public static Optional<ChangesFileName> from(final Path path) {
        final String filename = path.getFileName().toString();
        final Matcher matcher = PATTERN.matcher(filename);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new ChangesFileName(matcher.replaceFirst("$1")));
    }

    private final Semver version;

    /**
     * Create a new instance of {@link ChangesFileName}.
     *
     * @param version version to use for new instance
     */
    public ChangesFileName(final String version) {
        this.version = new Semver(version, SemverType.LOOSE);
    }

    /**
     * @return filename of the current {@link ChangesFileName} as string
     */
    public String filename() {
        return "changes_" + this.version + ".md";
    }

    @Override
    public int compareTo(final ChangesFileName o) {
        return this.version.compareTo(o.version);
    }

    /**
     * @return version number contained in the filename of current {@link ChangesFileName}
     */
    public String version() {
        return this.version.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.version);
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
        final ChangesFileName other = (ChangesFileName) obj;
        return Objects.equals(this.version, other.version);
    }
}
