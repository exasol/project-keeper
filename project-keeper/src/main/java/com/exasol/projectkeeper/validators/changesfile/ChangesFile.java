package com.exasol.projectkeeper.validators.changesfile;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
public final class ChangesFile {
    /** Headline of the dependency updates section. */
    public static final String DEPENDENCY_UPDATES_HEADING = "## Dependency Updates";
    private final String projectName;
    private final Semver projectVersion;
    private final String releaseDate;
    private final List<String> headerSectionLines;
    private final List<ChangesFileSection> sections;

    private ChangesFile(final Builder builder) {
        this.projectName = Objects.requireNonNull(builder.projectName, "projectName");
        this.projectVersion = Objects.requireNonNull(builder.projectVersion, "projectVersion");
        this.releaseDate = Objects.requireNonNull(builder.releaseDate, "releaseDate");
        this.headerSectionLines = List.copyOf(Objects.requireNonNull(builder.header, "header"));
        this.sections = List.copyOf(Objects.requireNonNull(builder.sections, "sections"));
    }

    /**
     * Get the relative path of the changes file for the given version.
     * 
     * @param projectVersion project version
     * @return relative path of the changes file, e.g. {@code doc/changes/changes_1.2.3.md}
     */
    public static Path getPathForVersion(final String projectVersion) {
        return Path.of("doc", "changes", new Filename(projectVersion).filename());
    }

    /**
     * Filename of a changes file, e.g. {@code changes_1.2.3.md}.
     */
    public static final class Filename implements Comparable<Filename> {
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
     * Get a builder configured with the this ChangesFile. This is useful for creating a copy and modify some parts of
     * this object.
     * 
     * @return a preconfigured builder
     */
    public Builder toBuilder() {
        return builder().projectName(this.projectName).projectVersion(this.projectVersion.toString())
                .releaseDate(this.releaseDate).setHeader(this.headerSectionLines).sections(this.sections);
    }

    /**
     * Get the project name for the first header line, e.g. {@code Project Keeper}.
     * 
     * @return project name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Get the project version for the first header line, e.g. {@code 1.2.3}.
     * 
     * @return project version
     */
    public Semver getProjectVersion() {
        return projectVersion;
    }

    /**
     * Get the release date for the first header line, e.g. {@code 2024-01-29} or {@code 2024-??-??}.
     * 
     * @return release date
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Get the parsed release date for the first header line. If the date is not valid (e.g. {@code 2024-??-??}), this
     * will return an empty {@link Optional}.
     * 
     * @return release date
     */
    public Optional<LocalDate> getParsedReleaseDate() {
        try {
            return Optional.of(LocalDate.parse(this.getReleaseDate()));
        } catch (final DateTimeParseException exception) {
            return Optional.empty();
        }
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
        final ChangesFile other = (ChangesFile) obj;
        return Objects.equals(projectName, other.projectName) && Objects.equals(projectVersion, other.projectVersion)
                && Objects.equals(releaseDate, other.releaseDate)
                && Objects.equals(headerSectionLines, other.headerSectionLines)
                && Objects.equals(sections, other.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectName, projectVersion, releaseDate, headerSectionLines, sections);
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
        private String projectName;
        private Semver projectVersion;
        private String releaseDate;
        private List<ChangesFileSection> sections = new ArrayList<>();
        private List<String> header = Collections.emptyList();

        private Builder() {
            // private constructor to hide public default
        }

        /**
         * Set the project name for the first header line, e.g. {@code Project Keeper}.
         *
         * @param projectName project name
         * @return self for fluent programming
         */
        public Builder projectName(final String projectName) {
            this.projectName = projectName;
            return this;
        }

        /**
         * Set the project version for the first header line, e.g. {@code 1.2.3}.
         *
         * @param projectVersion project version
         * @return self for fluent programming
         */
        public Builder projectVersion(final String projectVersion) {
            this.projectVersion = new Semver(projectVersion);
            return this;
        }

        /**
         * Set the release date for the first header line, e.g. {@code 2024-01-29} or {@code 2024-??-??}.
         *
         * @param releaseDate release date
         * @return self for fluent programming
         */
        public Builder releaseDate(final String releaseDate) {
            this.releaseDate = releaseDate;
            return this;
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
         * Set all sections of the changes file.
         *
         * @param lines list of sections
         * @return self for fluent programming
         */
        public Builder sections(final List<ChangesFileSection> sections) {
            this.sections = List.copyOf(sections);
            return this;
        }

        /**
         * Build the {@link ChangesFile}.
         *
         * @return built {@link ChangesFile}
         */
        public ChangesFile build() {
            return new ChangesFile(this);
        }
    }

    public static String getDependencyUpdatesHeading() {
        return DEPENDENCY_UPDATES_HEADING;
    }
}
