package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.ApStyleFormatter.capitalizeApStyle;
import static com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.SourceType.MAVEN;
import static com.exasol.projectkeeper.validators.finding.SimpleValidationFinding.blockers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.config.ProjectKeeperConfigReader;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
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
    private final Logger logger;
    private final Path projectDir;
    private final Path mvnRepo;
    private final ProjectKeeperConfig config;
    private final String ownVersion;
    private final String repoName;

    private ProjectKeeper(final Logger logger, final Path projectDir, final Path mvnRepo,
            final ProjectKeeperConfig config, final String ownVersion) {
        this.logger = logger;
        this.projectDir = projectDir;
        this.mvnRepo = mvnRepo;
        this.config = config;
        this.ownVersion = ownVersion;
        this.repoName = getRepoName(projectDir);
    }

    /**
     * Create a new instance of {@link ProjectKeeper}.
     *
     * @param logger     logger
     * @param projectDir project directory
     * @param mvnRepo    maven repository (null if unknown)
     * @return built {@link ProjectKeeper}
     */
    public static ProjectKeeper createProjectKeeper(final Logger logger, final Path projectDir, final Path mvnRepo) {
        final String ownVersion = ProjectKeeper.class.getPackage().getImplementationVersion();
        return new ProjectKeeper(logger, projectDir, mvnRepo, readConfig(projectDir), ownVersion);
    }

    /**
     * Create a new instance of {@link ProjectKeeper}.
     * <p>
     * This factory method is for testing only!
     * </p>
     *
     * @param logger     logger
     * @param projectDir project directory
     * @param mvnRepo    maven repository (null if unknown)
     * @param ownVersion version of project-keeper
     * @return built {@link ProjectKeeper}
     */
    static ProjectKeeper createProjectKeeper(final Logger logger, final Path projectDir, final Path mvnRepo,
            final String ownVersion) {
        return new ProjectKeeper(logger, projectDir, mvnRepo, readConfig(projectDir), ownVersion);
    }

    private String getRepoName(final Path projectDir) {
        try (final GitRepository gitRepository = GitRepository.open(projectDir)) {
            return gitRepository.getRepoNameFromRemote().orElseGet(() -> getProjectDirName(projectDir));
        }
    }

    private String getProjectDirName(final Path projectDir) {
        return projectDir.getFileName().toString();
    }

    private List<Supplier<List<Validator>>> getValidatorChain() {
        return List.of(this::getPhase0Validators, this::getPhase1Validators, this::getPhase2Validators,
                this::getPhase3Validators);
    }

    /**
     * Get the validators for the 0. validation phase.
     * <p>
     * These validators must run before the project file validation, because the project file validation depends on
     * them.
     * </p>
     *
     * @return validators.
     */
    private List<Validator> getPhase0Validators() {
        return List.of(new LicenseFileValidator(this.projectDir));
    }

    /**
     * Get the validators for the 1. validation phase.
     * <p>
     * Validators in phase 1 validate the build files like for example the pom.xml. In that phase analyzedSources is not
     * yet available.
     * </p>
     *
     * @return validators.
     */
    private List<Validator> getPhase1Validators() {
        final String licenseName = new LicenseNameReader().readLicenseName(this.projectDir);
        final List<Validator> result = new ArrayList<>();
        for (final ProjectKeeperConfig.Source source : this.config.getSources()) {
            if (source.getType().equals(MAVEN)) {
                result.add(new PomFileValidator(this.projectDir, source.getModules(), source.getPath(),
                        source.getParentPom(), new RepoInfo(this.repoName, licenseName)));
            } else {
                result.add(OwnVersionValidator.forCli(this.ownVersion));
            }
        }
        return result;
    }

    private List<Validator> getPhase2Validators() {
        final List<AnalyzedSource> analyzedSources = SourceAnalyzer.create(this.config, this.mvnRepo, this.ownVersion)
                .analyze(this.projectDir, this.config.getSources());
        final String projectName = getProjectName(analyzedSources);
        final var brokenLinkReplacer = new BrokenLinkReplacer(this.config.getLinkReplacements());
        final String projectVersion = new ProjectVersionDetector().detectVersion(this.config, analyzedSources);
        return List.of(new ProjectFilesValidator(this.projectDir, analyzedSources, this.logger),
                new ReadmeFileValidator(this.projectDir, projectName, this.repoName, analyzedSources),
                new ChangesFileValidator(projectVersion, projectName, this.projectDir, analyzedSources),
                new DependenciesValidator(analyzedSources, this.projectDir, brokenLinkReplacer),
                new DeletedFilesValidator(this.projectDir), new GitignoreFileValidator(this.projectDir));
    }

    private String getProjectName(final List<AnalyzedSource> analyzedSources) {
        final String projectName;
        if (analyzedSources.size() == 1) {
            projectName = analyzedSources.get(0).getProjectName();
        } else {
            projectName = capitalizeApStyle(this.repoName.replace("-", " ").replace("_", " "));
        }
        return projectName;
    }

    private List<Validator> getPhase3Validators() {
        /*
         * The {@link ChangelogFileValidator} needs to be in phase 3 since it's dependant of the result of the {@link
         * ChangesFileValidator}.
         */
        return List.of(new ChangelogFileValidator(this.projectDir));
    }

    private static ProjectKeeperConfig readConfig(final Path projectDir) {
        return new ProjectKeeperConfigReader().readConfig(projectDir);
    }

    /**
     * Verify the project structure.
     *
     * <p>
     * PK interprets the a validation as "successful" if there are no mandatory findings. Optional findings are ignored
     * in this place.
     * </p>
     *
     * @return {@code true} if project is valid
     */
    public boolean verify() {
        return runValidationPhases(this::handleVerifyFindings);
    }

    private boolean handleVerifyFindings(final List<ValidationFinding> findings) {
        final List<SimpleValidationFinding> findingsFlat = new FindingsUngrouper().ungroupFindings(findings);
        findingsFlat.forEach(finding -> this.logger.error(finding.getMessage()));
        final boolean hasFindingsWithFix = findingsFlat.stream().anyMatch(SimpleValidationFinding::hasFix);
        final boolean hasFindingsWithoutFix = findingsFlat.stream().anyMatch(finding -> !finding.hasFix());
        logValidationFailure(hasFindingsWithFix, hasFindingsWithoutFix);
        return blockers(findingsFlat).isEmpty();
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

    private List<ValidationFinding> runValidation(final List<Validator> validators) {
        final List<ValidationFinding> findings = validators.stream().flatMap(validator -> validator.validate().stream())
                .collect(Collectors.toList());
        return new FindingFilter(this.config.getExcludes()).filterFindings(findings);
    }

    /**
     * Fix all project findings.
     *
     * @return {@code true} if all mandatory findings could be fixed
     */
    public boolean fix() {
        return runValidationPhases(this::fixFindings);
    }

    /**
     * Run the validation phase handler.
     *
     * @param phaseResultHandler function List<ValidationFinding> -> boolean that is called after each phase. If the
     *                           function returns {@code false} this method aborts the execution and returns
     *                           {@code false}.
     * @return {@code true} if all {@link PhaseResultHandler} returned {@code true}. {@code false otherwise}
     */
    private boolean runValidationPhases(final PhaseResultHandler phaseResultHandler) {
        for (final Supplier<List<Validator>> phaseSupplier : getValidatorChain()) {
            final List<Validator> validators = phaseSupplier.get();
            final List<ValidationFinding> findings = runValidation(validators);
            if (!phaseResultHandler.handlePhaseResult(findings)) {
                return false;
            }
        }
        return true;
    }

    private boolean fixFindings(final List<ValidationFinding> findings) {
        final List<SimpleValidationFinding> unfixedFindings = new ArrayList<>(
                new FindingsFixer(this.logger).fixFindings(findings));

        for (final SimpleValidationFinding unfixedFinding : unfixedFindings) {
            this.logger.warn(ExaError.messageBuilder("W-PK-CORE-67")
                    .message("Could not auto-fix: {{finding message|uq}}", unfixedFinding.getMessage()).toString());
        }

        if (blockers(unfixedFindings).isEmpty()) {
            return true;
        } else {
            this.logger.error(ExaError.messageBuilder("E-PK-CORE-65").message(
                    "PK could not fix all of the findings automatically. There are findings that you need to fix by hand.")
                    .toString());
            return false;
        }
    }

    @FunctionalInterface
    private interface PhaseResultHandler {
        /**
         * Handler for the result of a validation phase.
         *
         * @param findings list of findings
         * @return {@code false} if the validation should stop after this phase.
         */
        boolean handlePhaseResult(final List<ValidationFinding> findings);
    }
}
