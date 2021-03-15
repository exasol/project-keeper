package com.exasol.projectkeeper.validators.changesfile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents a doc/changes/changes_x.x.x.md file.
 */
public class ChangesFile {
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
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
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
