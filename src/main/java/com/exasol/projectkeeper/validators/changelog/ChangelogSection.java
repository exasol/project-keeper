package com.exasol.projectkeeper.validators.changelog;

import java.util.List;
import java.util.Objects;

import com.exasol.errorreporting.ExaError;

/**
 * Section of a {@link ChangelogFile}.
 * <p>
 * Each level two headline (##) starts a new section.
 * </p>
 */
public class ChangelogSection {
    private final List<String> content;

    /**
     * Create a new instance of {@link ChangelogSection}.
     * 
     * @param content lines
     */
    public ChangelogSection(final List<String> content) {
        if (content.isEmpty()) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-36")
                    .message("Changelog sections must not be empty.").ticketMitigation().toString());
        }
        this.content = content;
    }

    /**
     * Get the headline of this section
     * 
     * @return headline
     */
    public String getHeadline() {
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
        final ChangelogSection that = (ChangelogSection) other;
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
