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
        return heading + "\n" + String.join("\n", this.content);
    }

    public static Builder builder(final String heading) {
        return new Builder(heading);
    }

    public static class Builder {

        private final String heading;
        private final List<String> lines = new ArrayList<>();

        private Builder(final String heading) {
            this.heading = heading;
        }

        public Builder addLines(final String... lines) {
            this.lines.addAll(asList(lines));
            return this;
        }

        public Builder addLines(final List<String> lines) {
            this.lines.addAll(lines);
            return this;
        }

        public Builder addLine(final String line) {
            this.lines.add(line);
            return this;
        }

        public ChangesFileSection build() {
            return new ChangesFileSection(this);
        }
    }
}
