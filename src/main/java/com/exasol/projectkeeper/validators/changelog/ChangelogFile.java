package com.exasol.projectkeeper.validators.changelog;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents a doc/changes/changes_x.x.x.md file.
 */
public class ChangelogFile {
    public static final String DEPENDENCY_UPDATES_HEADLINE = "## Dependency Updates";
    private final List<String> headerLines;
    private final List<ChangelogSection> sections;

    /**
     * Create a new instance of {@link ChangelogFile}.
     * 
     * @param headerLines header of the changelog file
     * @param sections    sections of the changelog file
     */
    public ChangelogFile(final List<String> headerLines, final List<ChangelogSection> sections) {
        this.headerLines = headerLines;
        this.sections = sections;
    }

    /**
     * Get a {@link ChangelogFile} builder.
     * 
     * @return builder for {@link ChangelogFile}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get the changelog file header.
     *
     * @return list of lines of the header
     */
    public List<String> getHeaderLines() {
        return this.headerLines;
    }

    /**
     * Get the headline of the file.
     *
     * @return headline
     */
    public String getHeadline() {
        return this.headerLines.get(0);
    }

    /**
     * Get a list of sections (starting with a ## headline).
     *
     * @return list of sections
     */
    public List<ChangelogSection> getSections() {
        return this.sections;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        final ChangelogFile that = (ChangelogFile) other;
        return Objects.equals(this.headerLines, that.headerLines) && Objects.equals(this.sections, that.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.headerLines, this.sections);
    }

    @Override
    public String toString() {
        return String.join("\n", this.headerLines) + "\n"
                + this.sections.stream().map(ChangelogSection::toString).collect(Collectors.joining("\n"));
    }

    /**
     * Builder for {@link ChangelogFile}.
     */
    public static class Builder {
        private final List<ChangelogSection> sections = new ArrayList<>();
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
            this.sections.add(new ChangelogSection(lines));
            return this;
        }

        /**
         * Build the {@link ChangelogFile}.
         *
         * @return built {@link ChangelogFile}
         */
        public ChangelogFile build() {
            return new ChangelogFile(this.header, this.sections);
        }
    }
}
