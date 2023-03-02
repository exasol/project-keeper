package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.validators.files.RequiredFileValidator.fileExists;
import static com.exasol.projectkeeper.validators.files.RequiredFileValidator.withContentEqualTo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.files.RequiredFileValidator.ContentValidator;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * Validator for the projects file structure.
 */
//[impl->dsn~required-files-validator~1]
public class ProjectFilesValidator implements Validator {
    private Path projectDirectory;
    private List<AnalyzedSource> sources;
    private Logger logger;
    private String projectKeeperVersion;
    public boolean hasNpmModule;

//	private ProjectFilesValidator(Builder builder) {
//		this.projectDirectory = builder.projectDirectory;
//		this.sources = builder.sources;
//		this.logger = builder.logger;
//		this.ownVersion = builder.ownVersion;
//	}

    /**
     * Crwate a new instance of {@link ProjectFilesValidator}.
     *
     * @param projectDirectory project's root directory
     * @param sources          list of sources
     * @param logger           logger
     * @param ownVersion       the version of the currently running project keeper
     */
//    public ProjectFilesValidator(final Path projectDirectory, final List<AnalyzedSource> sources, final Logger logger,
//            final String ownVersion) {
//        this.projectDirectory = projectDirectory;
//        this.sources = sources;
//        this.logger = logger;
//        this.ownVersion = ownVersion;
//    }

    /**
     * Crwate a new instance of {@link ProjectFilesValidator}.
     */
    ProjectFilesValidator() {
    }

    @Override
    public List<ValidationFinding> validate() {
        final List<ValidationFinding> findings = new ArrayList<>();
        final FileTemplatesFactory templatesFactory = new FileTemplatesFactory(this.logger, this.projectKeeperVersion, true);
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
     *
     * @author chku
     *
     */
    public static final class Builder {
        private final ProjectFilesValidator validator = new ProjectFilesValidator();

        public Builder projectDirectory(final Path projectDirectory) {
            this.validator.projectDirectory = projectDirectory;
            return this;
        }

        public Builder analyzedSources(final List<AnalyzedSource> value) {
            this.validator.sources = value;
            return this;
        }

        public Builder logger(final Logger value) {
            this.validator.logger = value;
            return this;
        }

        public Builder projectKeeperVersion(final String value) {
            this.validator.projectKeeperVersion = value;
            return this;
        }

        public Builder hasNpmModule(final boolean value) {
            this.validator.hasNpmModule = value;
            return this;
        }

        public ProjectFilesValidator build() {
            return this.validator;
        }
    }
}
