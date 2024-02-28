package com.exasol.projectkeeper.validators.changesfile;

import static java.util.Arrays.asList;

import java.util.*;

/**
 * Section of a {@link ChangesFile}.
 * <p>
 * Each level two heading (##) starts a new section.
 * </p>
 */
public final class ChangesFileSection {

    private final String heading;
    private final List<String> content;

    private ChangesFileSection(final Builder builder) {
        this.heading = Objects.requireNonNull(builder.heading, "header");
        this.content = List.copyOf(builder.lines);
    }

    /**
     * Get the heading of this section
     * 
     * @return heading
     */
    public String getHeading() {
        return this.heading;
    }

    /**
     * Get the content of this section.
     * 
     * @return list of lines
     */
    public List<String> getContent() {
        return this.content;
    }

    @Override
    public int hashCode() {
        return Objects.hash(heading, content);
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
        final ChangesFileSection other = (ChangesFileSection) obj;
        return Objects.equals(heading, other.heading) && Objects.equals(content, other.content);
    }

    @Override
    public String toString() {
        final String text = heading + "\n" + String.join("\n", this.content);
        return text.replaceAll("(?m)\n{3,}", "\n\n");
    }

    /**
     * Create a new {@link Builder} for creating a {@link ChangesFileSection}.
     * 
     * @param heading the heading for the new section
     * @return a new builder
     */
    public static Builder builder(final String heading) {
        return new Builder(heading);
    }

    /**
     * Create a builder with the same content as this section.
     * 
     * @return preconfigured builder
     */
    public Builder toBuilder() {
        return builder(this.heading).addLines(this.content);
    }

    /**
     * A builder for creating {@link ChangesFileSection}s.
     */
    public static class Builder {
        private final String heading;
        private final List<String> lines = new ArrayList<>();

        private Builder(final String heading) {
            this.heading = heading;
        }

        /**
         * Add the given lines to the content of the new {@code ChangesFileSection}.
         * 
         * @param lines lines to add
         * @return {@code this} for fluent programming
         */
        public Builder addLines(final String... lines) {
            this.lines.addAll(asList(lines));
            return this;
        }

        /**
         * Add the given lines to the content of the new {@code ChangesFileSection}.
         * 
         * @param lines lines to add
         * @return {@code this} for fluent programming
         */
        public Builder addLines(final List<String> lines) {
            this.lines.addAll(lines);
            return this;
        }

        /**
         * Add the given line to the content of the new {@code ChangesFileSection}.
         * 
         * @param line line to add
         * @return {@code this} for fluent programming
         */
        public Builder addLine(final String line) {
            this.lines.add(line);
            return this;
        }

        /**
         * Build a new {@link ChangesFileSection}.
         * 
         * @return a new {@link ChangesFileSection}.
         */
        public ChangesFileSection build() {
            return new ChangesFileSection(this);
        }
    }
}
