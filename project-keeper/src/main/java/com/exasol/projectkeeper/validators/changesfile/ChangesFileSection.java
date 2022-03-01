package com.exasol.projectkeeper.validators.changesfile;

import java.util.List;
import java.util.Objects;

import com.exasol.errorreporting.ExaError;

/**
 * Section of a {@link ChangesFile}.
 * <p>
 * Each level two heading (##) starts a new section.
 * </p>
 */
public class ChangesFileSection {
    private final List<String> content;

    /**
     * Create a new instance of {@link ChangesFileSection}.
     * 
     * @param content lines
     */
    public ChangesFileSection(final List<String> content) {
        if (content.isEmpty()) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-36")
                    .message("changes file sections must not be empty.").ticketMitigation().toString());
        }
        this.content = content;
    }

    /**
     * Get the heading of this section
     * 
     * @return heading
     */
    public String getHeading() {
        return this.content.get(0);
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
    public boolean equals(final Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        final ChangesFileSection that = (ChangesFileSection) other;
        return Objects.equals(this.content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.content);
    }

    @Override
    public String toString() {
        return String.join("\n", this.content);
    }
}
