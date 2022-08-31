package com.exasol.projectkeeper.validators.changesfile;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.mavenrepo.Version;
import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.Semver.SemverType;

/**
 * This class represents a doc/changes/changes_x.x.x.md file.
 */
public class ChangesFile {
    /** Headline of the dependency updates section. */
    public static final String DEPENDENCY_UPDATES_HEADING = "## Dependency Updates";
    private final List<String> headerSectionLines;
    private final List<ChangesFileSection> sections;

    /**
     * Create a new instance of {@link ChangesFile}.
     *
     * @param headerLines lines of the changes file until the first level section
     * @param sections    sections of the changes file
     */
    public ChangesFile(final List<String> headerLines, final List<ChangesFileSection> sections) {
        this.headerSectionLines = headerLines;
        this.sections = sections;
    }

    /**
     * Filename of a changes file, e.g. "changes_1.2.3.md".
     */
    public static class Filename implements Comparable<Filename> {
        /** Regular expression to identify valid names of changes files and to extract version number. **/
        public static final Pattern PATTERN = Pattern.compile("changes_(" + Version.PATTERN.pattern() + ")\\.md");

        /**
         * @param path path to create a {@link Filename} for
         * @return If path matches regular expression for valid changes filenames then an {@link Optional} containing a
         *         new instance of {@link Filename}, otherwise {@code Optional.empty()}.
         */
        public static Optional<Filename> from(final Path path) {
            final String filename = path.getFileName().toString();
            final Matcher matcher = PATTERN.matcher(filename);
            if (!matcher.matches()) {
                return Optional.empty();
            }
            return Optional.of(new Filename(matcher.replaceFirst("$1")));
        }

        private final Semver version;

        /**
         * Create a new instance of {@link ChangesFile.Filename}.
         *
         * @param version version to use for new instance
         */
        public Filename(final String version) {
            this.version = new Semver(version, SemverType.LOOSE);
        }

        /**
         * @return filename of the current {@link ChangesFile.Filename} as string
         */
        public String filename() {
            return "changes_" + this.version + ".md";
        }

        @Override
        public int compareTo(final Filename o) {
            return this.version.compareTo(o.version);
        }

        /**
         * @return version number contained in the filename of current {@link ChangesFile.Filename}
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
            final Filename other = (Filename) obj;
            return Objects.equals(this.version, other.version);
        }
    }

    /**
     * Get a {@link ChangesFile} builder.
     *
     * @return builder for {@link ChangesFile}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get the header of the changes section.
     * <p>
     * The header includes all lines until the first section {@code ##} starts.
     * </p>
     *
     * @return list of lines of the header
     */
    public List<String> getHeaderSectionLines() {
        return this.headerSectionLines;
    }

    /**
     * Get the heading of the file.
     *
     * @return heading (1. line)
     */
    public String getHeading() {
        return this.headerSectionLines.get(0);
    }

    /**
     * Get a list of sections (starting with a ## heading).
     *
     * @return list of sections
     */
    public List<ChangesFileSection> getSections() {
        return this.sections;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if ((other == null) || (getClass() != other.getClass())) {
            return false;
        }
        final ChangesFile that = (ChangesFile) other;
        return Objects.equals(this.headerSectionLines, that.headerSectionLines)
                && Objects.equals(this.sections, that.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.headerSectionLines, this.sections);
    }

    @Override
    public String toString() {
        return String.join("\n", this.headerSectionLines) + "\n"
                + this.sections.stream().map(ChangesFileSection::toString).collect(Collectors.joining("\n"));
    }

    /**
     * Builder for {@link ChangesFile}.
     */
    public static class Builder {
        private final List<ChangesFileSection> sections = new ArrayList<>();
        private List<String> header = Collections.emptyList();

        private Builder() {
            // private constructor to hide public default
        }

        /**
         * Set the header of the changes file.
         *
         * @param header list of lines
         * @return self for fluent programming
         */
        public Builder setHeader(final List<String> header) {
            this.header = header;
            return this;
        }

        /**
         * Add a section to the changes file.
         *
         * @param lines list of lines
         * @return self for fluent programming
         */
        public Builder addSection(final List<String> lines) {
            this.sections.add(new ChangesFileSection(lines));
            return this;
        }

        /**
         * Build the {@link ChangesFile}.
         *
         * @return built {@link ChangesFile}
         */
        public ChangesFile build() {
            return new ChangesFile(this.header, this.sections);
        }
    }
}
