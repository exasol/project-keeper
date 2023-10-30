package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.validators.files.RequiredFileValidator.fileExists;
import static com.exasol.projectkeeper.validators.files.RequiredFileValidator.withContentEqualTo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.files.RequiredFileValidator.ContentValidator;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * Validator for the projects file structure.
 */
// [impl->dsn~required-files-validator~1]
public class ProjectFilesValidator implements Validator {
    private final Path projectDirectory;
    private final List<AnalyzedSource> sources;
    private final Logger logger;
    private final String projectKeeperVersion;
    private final boolean hasNpmModule;
    private final String ciBuildRunnerOS;

    private ProjectFilesValidator(final Builder builder) {
        this.projectDirectory = Objects.requireNonNull(builder.projectDirectory, "projectDirectory");
        this.sources = Objects.requireNonNull(builder.sources, "sources");
        this.logger = Objects.requireNonNull(builder.logger, "logger");
        this.projectKeeperVersion = Objects.requireNonNull(builder.projectKeeperVersion, "projectKeeperVersion");
        this.hasNpmModule = builder.hasNpmModule;
        this.ciBuildRunnerOS = Objects.requireNonNull(builder.ciBuildRunnerOS, "ciBuildRunnerOS");
    }

    @Override
    public List<ValidationFinding> validate() {
        final List<ValidationFinding> findings = new ArrayList<>();
        final FileTemplatesFactory templatesFactory = new FileTemplatesFactory(this.logger, this.projectKeeperVersion,
                this.hasNpmModule, this.ciBuildRunnerOS);
        findings.addAll(validateTemplatesRelativeToRepo(templatesFactory));
        findings.addAll(validateTemplatesRelativeToSource(templatesFactory));
        return findings;
    }

    private List<ValidationFinding> validateTemplatesRelativeToSource(final FileTemplatesFactory templatesFactory) {
        final List<ValidationFinding> findings = new ArrayList<>();
        for (final AnalyzedSource source : this.sources) {
            final Path sourceDir = directoryOf(source.getPath());
            final List<FileTemplate> templates = templatesFactory.getTemplatesForSource(source);
            findings.addAll(runValidation(templates, sourceDir));
        }
        return findings;
    }

    private List<ValidationFinding> validateTemplatesRelativeToRepo(final FileTemplatesFactory templatesFactory) {
        final List<FileTemplate> templates = templatesFactory.getGlobalTemplates(this.sources);
        return runValidation(templates, this.projectDirectory);
    }

    private List<ValidationFinding> runValidation(final List<FileTemplate> templates, final Path relativeDirectory) {
        final List<ValidationFinding> findings = new ArrayList<>();
        for (final FileTemplate template : templates) {
            findings.addAll(validate(relativeDirectory, template));
        }
        return findings;
    }

    private Path directoryOf(final Path path) {
        if (Files.isDirectory(path)) {
            return path;
        } else {
            return path.getParent();
        }
    }

    private List<ValidationFinding> validate(final Path projectDirectory, final FileTemplate template) {
        final Path projectFile = projectDirectory.resolve(template.getPathInProject());
        final ContentValidator contentValidator = getContentValidator(template);
        return new RequiredFileValidator().validateFile(projectDirectory, projectFile, contentValidator);
    }

    private ContentValidator getContentValidator(final FileTemplate template) {
        switch (template.getValidation()) {
        case REQUIRE_EXACT:
            return withContentEqualTo(template.getContent());
        case REQUIRE_EXIST:
            return fileExists(template.getContent());
        default:
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-119")
                    .message("Unknown template type {{template type}}", template.getValidation()).ticketMitigation()
                    .toString());
        }
    }

    /**
     * @return Builder for a new instance of {@link ProjectFilesValidator}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for new instances of {@link ProjectFilesValidator}
     */
    public static final class Builder {
        private Path projectDirectory;
        private List<AnalyzedSource> sources;
        private Logger logger;
        private String projectKeeperVersion;
        private boolean hasNpmModule;
        private String ciBuildRunnerOS;

        private Builder() {
            // empty by intention
        }

        /**
         * @param projectDirectory project's root directory
         * @return {@code this} for fluent programming
         */
        public Builder projectDirectory(final Path projectDirectory) {
            this.projectDirectory = projectDirectory;
            return this;
        }

        /**
         * @param value list of analyzed sources
         * @return {@code this} for fluent programming
         */
        public Builder analyzedSources(final List<AnalyzedSource> value) {
            this.sources = value;
            return this;
        }

        /**
         * @param value logger to use for log messages
         * @return {@code this} for fluent programming
         */
        public Builder logger(final Logger value) {
            this.logger = value;
            return this;
        }

        /**
         * @param value the version of the currently running project keeper
         * @return {@code this} for fluent programming
         */
        public Builder projectKeeperVersion(final String value) {
            this.projectKeeperVersion = value;
            return this;
        }

        /**
         * @param value {@code true} if the current project contains an NPM module
         * @return {@code this} for fluent programming
         */
        public Builder hasNpmModule(final boolean value) {
            this.hasNpmModule = value;
            return this;
        }

        /**
         * 
         * @param ciBuildRunnerOS operating system to use for CI builds
         * @return {@code this} for fluent programming
         */
        public Builder ciBuildRunnerOS(final String ciBuildRunnerOS) {
            this.ciBuildRunnerOS = ciBuildRunnerOS;
            return this;
        }

        /**
         * @return new instance of {@link ProjectFilesValidator}
         */
        public ProjectFilesValidator build() {
            return new ProjectFilesValidator(this);
        }
    }
}
