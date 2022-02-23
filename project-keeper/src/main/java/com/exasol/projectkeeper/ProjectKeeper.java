package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.config.ProjectKeeperConfig.SourceType.MAVEN;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.config.ProjectKeeperConfigReader;
import com.exasol.projectkeeper.shared.repository.GitRepository;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.sources.SourceAnalyzer;
import com.exasol.projectkeeper.validators.*;
import com.exasol.projectkeeper.validators.changelog.ChangelogFileValidator;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileValidator;
import com.exasol.projectkeeper.validators.dependencies.DependenciesValidator;
import com.exasol.projectkeeper.validators.files.ProjectFilesValidator;
import com.exasol.projectkeeper.validators.finding.*;
import com.exasol.projectkeeper.validators.pom.PomFileValidator;

/**
 * This is the entry-point class of project-keeper-core.
 */
public class ProjectKeeper {
    private static final String INVALID_STRUCTURE_MESSAGE = "This projects structure does not conform with the template.";
    private final List<Validator> validators;
    private final Logger logger;
    private final List<String> excludes;

    private ProjectKeeper(final Logger logger, final Path projectDir, final String projectName, final String artifactId,
            final String projectVersion, final Path mvnRepo, final ProjectKeeperConfig config,
            final String ownVersion) {
        this.logger = logger;
        this.excludes = config.getExcludes();
        final GitRepository gitRepository = new GitRepository(projectDir);
        final var brokenLinkReplacer = new BrokenLinkReplacer(config.getLinkReplacements());
        final List<ProjectKeeperConfig.Source> sources = config.getSources();
        final List<AnalyzedSource> analyzedSources = new SourceAnalyzer().analyze(projectDir, sources);
        this.validators = new ArrayList<>(List.of(new ProjectFilesValidator(projectDir, analyzedSources, logger),
                new ReadmeFileValidator(projectDir, projectName,
                        gitRepository.getRepoNameFromRemote().orElse(artifactId), analyzedSources)));
        this.validators.addAll(getValidatorsPerSource(projectDir, sources));
        this.validators
                .addAll(List.of(new LicenseFileValidator(projectDir),
                        new ChangesFileValidator(projectVersion, projectName, projectDir, mvnRepo, ownVersion,
                                analyzedSources),
                        new ChangelogFileValidator(projectDir),
                        new DependenciesValidator(analyzedSources, projectDir, brokenLinkReplacer, mvnRepo, ownVersion),
                        new DeletedFilesValidator(projectDir), new GitignoreFileValidator(projectDir)));
    }

    /**
     * Create a new instance of {@link ProjectKeeper}.
     * 
     * @param logger         logger
     * @param projectDir     project directory
     * @param projectName    name of the project
     * @param artifactId     artifact-id
     * @param projectVersion project version
     * @param mvnRepo        maven repository (null if unknown)
     * @return built {@link ProjectKeeper}
     */
    public static ProjectKeeper createProjectKeeper(final Logger logger, final Path projectDir,
            final String projectName, final String artifactId, final String projectVersion, final Path mvnRepo) {
        final String ownVersion = ProjectKeeper.class.getPackage().getImplementationVersion();
        return new ProjectKeeper(logger, projectDir, projectName, artifactId, projectVersion, mvnRepo,
                readConfig(projectDir), ownVersion);
    }

    /**
     * Create a new instance of {@link ProjectKeeper}.
     * <p>
     * This factory method is for testing only!
     * </p>
     *
     * @param logger         logger
     * @param projectDir     project directory
     * @param projectName    name of the project
     * @param artifactId     artifact-id
     * @param projectVersion project version
     * @param mvnRepo        maven repository (null if unknown)
     * @param ownVersion     version of project-keeper
     * @return built {@link ProjectKeeper}
     */
    static ProjectKeeper createProjectKeeper(final Logger logger, final Path projectDir, final String projectName,
            final String artifactId, final String projectVersion, final Path mvnRepo, final String ownVersion) {
        return new ProjectKeeper(logger, projectDir, projectName, artifactId, projectVersion, mvnRepo,
                readConfig(projectDir), ownVersion);
    }

    private static ProjectKeeperConfig readConfig(final Path projectDir) {
        return new ProjectKeeperConfigReader().readConfig(projectDir);
    }

    private List<Validator> getValidatorsPerSource(final Path projectDir,
            final List<ProjectKeeperConfig.Source> sources) {
        final List<Validator> result = new ArrayList<>();
        for (final ProjectKeeperConfig.Source source : sources) {
            if (source.getType().equals(MAVEN)) {
                result.add(
                        new PomFileValidator(projectDir, source.getModules(), source.getPath(), source.getParentPom()));
            }
        }
        return result;
    }

    /**
     * Verify the project structure.
     *
     * @return {@code true} if project is valid
     */
    public boolean verify() {
        final List<ValidationFinding> findings = runValidation();
        final List<SimpleValidationFinding> findingsFlat = new FindingsUngrouper().ungroupFindings(findings);
        findingsFlat.forEach(finding -> this.logger.error(finding.getMessage()));
        final boolean hasFindingsWithFix = findingsFlat.stream().anyMatch(SimpleValidationFinding::hasFix);
        final boolean hasFindingsWithoutFix = findingsFlat.stream().anyMatch(finding -> !finding.hasFix());
        logValidationFailure(hasFindingsWithFix, hasFindingsWithoutFix);
        return findings.isEmpty();
    }

    private void logValidationFailure(final boolean hasFindingsWithFix, final boolean hasFindingsWithoutFix) {
        if (hasFindingsWithoutFix && hasFindingsWithFix) {
            this.logger.error(ExaError.messageBuilder("E-PK-CORE-24").message(INVALID_STRUCTURE_MESSAGE).mitigation(
                    "You can automatically fix some of the issues by running mvn project-keeper:fix but some also need to be fixed manually.")
                    .toString());
        } else if (hasFindingsWithFix) {
            this.logger.error(ExaError.messageBuilder("E-PK-CORE-6").message(INVALID_STRUCTURE_MESSAGE)
                    .mitigation("Run mvn project-keeper:fix to fix the issues automatically.").toString());

        } else if (hasFindingsWithoutFix) {
            this.logger.error(ExaError.messageBuilder("E-PK-CORE-25").message(INVALID_STRUCTURE_MESSAGE)
                    .mitigation("Please fix it manually.").toString());
        }
    }

    private List<ValidationFinding> runValidation() {
        final List<ValidationFinding> findings = this.validators.stream()
                .flatMap(validator -> validator.validate().stream()).collect(Collectors.toList());
        return new FindingFilter(this.excludes).filterFindings(findings);
    }

    /**
     * Fix all project findings.
     * 
     * @return {@code true} if all findings could be fixed
     */
    public boolean fix() {
        final FindingFilter filter = new FindingFilter(this.excludes);
        final List<SimpleValidationFinding> unfixedFindings = new ArrayList<>();
        for (final Validator validator : this.validators) {
            // it's important to run fixes after each validator since they can influence the next validator.
            final List<ValidationFinding> findings = filter.filterFindings(validator.validate());
            unfixedFindings.addAll(new FindingsFixer(this.logger).fixFindings(findings));
        }
        for (final SimpleValidationFinding unfixedFinding : unfixedFindings) {
            this.logger.warn(ExaError.messageBuilder("W-PK-CORE-67")
                    .message("Could not auto-fix: {{finding message|uq}}", unfixedFinding.getMessage()).toString());
        }
        if (!unfixedFindings.isEmpty()) {
            this.logger.error(ExaError.messageBuilder("E-PK-CORE-65").message(
                    "PK could not fix all of the findings automatically. There are findings that you need to fix by hand.")
                    .toString());
            return false;
        } else {
            return true;
        }
    }
}
