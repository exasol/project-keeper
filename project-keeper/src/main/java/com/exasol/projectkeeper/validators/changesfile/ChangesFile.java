package com.exasol.projectkeeper.validators.changesfile;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Stream;

import com.exasol.errorreporting.ExaError;
import com.vdurmont.semver4j.Semver;

/**
 * This class represents a doc/changes/changes_x.x.x.md file.
 */
public final class ChangesFile {
    /** Headline of the dependency updates section. */
    public static final String DEPENDENCY_UPDATES_HEADING = "## Dependency Updates";
    /** Headline of the Summary section. */
    public static final String SUMMARY_HEADING = "## Summary";
    private final String projectName;
    private final Semver projectVersion;
    private final String releaseDate;
    private final String codeName;
    private final ChangesFileSection summarySection;
    private final List<ChangesFileSection> sections;
    private final ChangesFileSection dependencyChangeSection;

    private ChangesFile(final Builder builder) {
        this.projectName = builder.projectName;
        this.projectVersion = builder.projectVersion;
        this.releaseDate = builder.releaseDate;
        this.codeName = builder.codeName;
        this.summarySection = builder.summarySection;
        this.sections = List.copyOf(builder.sections);
        this.dependencyChangeSection = builder.dependencyChangeSection;
    }

    /**
     * Get the relative path of the changes file for the given version.
     * 
     * @param projectVersion project version
     * @return relative path of the changes file, e.g. {@code doc/changes/changes_1.2.3.md}
     */
    public static Path getPathForVersion(final String projectVersion) {
        return Path.of("doc", "changes", new ChangesFileName(projectVersion).filename());
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
        return builder() //
                .projectName(this.projectName)
                .projectVersion(this.projectVersion != null ? this.projectVersion.toString() : null)
                .releaseDate(this.releaseDate) //
                .codeName(this.codeName) //
                .summary(this.summarySection) //
                .sections(List.copyOf(this.sections)) //
                .dependencyChangeSection(this.dependencyChangeSection);
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
     * Get the code name of the release.
     * 
     * @return code name
     */
    public String getCodeName() {
        return codeName;
    }

    /**
     * Get the parsed release date for the first header line. If the date is not valid (e.g. {@code 2024-??-??}), this
     * will return an empty {@link Optional}.
     * 
     * @return release date
     */
    public Optional<LocalDate> getParsedReleaseDate() {
        try {
            return Optional.ofNullable(this.getReleaseDate()).map(LocalDate::parse);
        } catch (final DateTimeParseException exception) {
            return Optional.empty();
        }
    }

    /**
     * Get a list of sections (starting with a ## heading).
     *
     * @return list of sections
     */
    public List<ChangesFileSection> getSections() {
        return this.sections;
    }

    /**
     * Get the dependency change section.
     * 
     * @return dependency change section
     */
    public Optional<ChangesFileSection> getDependencyChangeSection() {
        return Optional.ofNullable(this.dependencyChangeSection);
    }

    /**
     * Get the summary section.
     *
     * @return summary section
     */
    public Optional<ChangesFileSection> getSummarySection() {
        return Optional.ofNullable(this.summarySection);
    }

    /**
     * Get all issues marked as fixed in this changes file.
     * 
     * @return all fixed issues
     */
    public List<FixedIssue> getFixedIssues() {
        return Stream.concat(summarySection != null ? Stream.of(summarySection) : Stream.empty(), sections.stream()) //
                .flatMap(ChangesFileSection::getFixedIssues) //
                .toList();
    }

    @Override
    public String toString() {
        return "ChangesFile [projectName=" + projectName + ", projectVersion=" + projectVersion + ", releaseDate="
                + releaseDate + ", codeName=" + codeName + ", summarySection=" + summarySection + ", sections="
                + sections + ", dependencyChangeSection=" + dependencyChangeSection + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectName, projectVersion, releaseDate, codeName, summarySection, sections,
                dependencyChangeSection);
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
                && Objects.equals(releaseDate, other.releaseDate) && Objects.equals(codeName, other.codeName)
                && Objects.equals(summarySection, other.summarySection) && Objects.equals(sections, other.sections)
                && Objects.equals(dependencyChangeSection, other.dependencyChangeSection);
    }

    /**
     * Builder for {@link ChangesFile}.
     */
    public static class Builder {

        private String projectName;
        private Semver projectVersion;
        private String releaseDate;
        private String codeName;
        private ChangesFileSection summarySection;
        private List<ChangesFileSection> sections = new ArrayList<>();
        private ChangesFileSection dependencyChangeSection;

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
            this.projectVersion = projectVersion != null ? new Semver(projectVersion) : null;
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
         * Set the code name of the release.
         *
         * @param codeName code name
         * @return self for fluent programming
         */
        public Builder codeName(final String codeName) {
            this.codeName = codeName != null && codeName.isBlank() ? null : codeName;
            return this;
        }

        /**
         * Add a section to the changes file.
         *
         * @param section section
         * @return self for fluent programming
         */
        public Builder addSection(final ChangesFileSection section) {
            this.sections.add(section);
            return this;
        }

        /**
         * Set the {@code Summary} section for the changes file.
         *
         * @param section section
         * @return self for fluent programming
         */
        public Builder summary(final ChangesFileSection section) {
            if (section == null) {
                this.summarySection = null;
                return this;
            }
            if (!section.getHeading().equals(SUMMARY_HEADING)) {
                throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-178").message(
                        "Dependency change section has invalid heading {{heading}}, expected {{expected heading}}",
                        section.getHeading(), SUMMARY_HEADING).ticketMitigation().toString());
            }
            this.summarySection = section;
            return this;
        }

        /**
         * Set all sections of the changes file.
         *
         * @param sections list of sections
         * @return self for fluent programming
         */
        public Builder sections(final List<ChangesFileSection> sections) {
            this.sections = List.copyOf(sections);
            return this;
        }

        /**
         * Add a an optional {@code Dependency Updates} section to the changes file.
         *
         * @param section section
         * @return self for fluent programming
         */
        public Builder dependencyChangeSection(final ChangesFileSection section) {
            if (section == null) {
                this.dependencyChangeSection = null;
                return this;
            }
            if (!section.getHeading().equals(DEPENDENCY_UPDATES_HEADING)) {
                throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-179").message(
                        "Dependency change section has invalid heading {{heading}}, expected {{expected heading}}",
                        section.getHeading(), DEPENDENCY_UPDATES_HEADING).ticketMitigation().toString());
            }
            this.dependencyChangeSection = section;
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
}
