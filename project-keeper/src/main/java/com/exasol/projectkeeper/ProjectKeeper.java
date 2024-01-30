package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.ApStyleFormatter.humanReadable;
import static com.exasol.projectkeeper.shared.config.SourceType.MAVEN;
import static com.exasol.projectkeeper.validators.finding.SimpleValidationFinding.blockers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ValidationPhase.Provision;
import com.exasol.projectkeeper.config.ProjectKeeperConfigReader;
import com.exasol.projectkeeper.dependencyupdate.DependencyUpdater;
import com.exasol.projectkeeper.shared.config.*;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.sources.SourceAnalyzer;
import com.exasol.projectkeeper.sources.analyze.generic.RepoNameReader;
import com.exasol.projectkeeper.validators.*;
import com.exasol.projectkeeper.validators.changelog.ChangelogFileValidator;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileValidator;
import com.exasol.projectkeeper.validators.dependencies.DependenciesValidator;
import com.exasol.projectkeeper.validators.files.LatestChangesFileValidator;
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
        this.repoName = RepoNameReader.getRepoName(projectDir);
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
        return new ProjectKeeper(logger, projectDir, mvnRepo, readConfig(projectDir), getOwnVersion());
    }

    private static String getOwnVersion() {
        final String packageVersion = ProjectKeeper.class.getPackage().getImplementationVersion();
        if (packageVersion != null) {
            return packageVersion;
        }
        final String versionFromProperty = System.getProperty("com.exasol.projectkeeper.ownVersion");
        if (versionFromProperty != null) {
            return versionFromProperty;
        }
        return "(unknownVersion)";
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

    private List<Function<ValidationPhase.Provision, ValidationPhase>> getValidationPhases() {
        return List.of(this::phase0, this::phase1, this::phase2, this::phase3);
    }

    /*
     * Phase 0 must run before the project file validation, because the project file validation depends on them.
     */
    private ValidationPhase phase0(final ValidationPhase.Provision provision) {
        return ValidationPhase.from(new LicenseFileValidator(this.projectDir));
    }

    /*
     * Phase 1 validates the build files like for example the pom.xml. In that phase analyzedSources is not yet
     * available.
     */
    private ValidationPhase phase1(final ValidationPhase.Provision provision) {
        final String licenseName = new LicenseNameReader().readLicenseName(this.projectDir);
        final List<Validator> validators = new ArrayList<>();
        for (final Source source : this.config.getSources()) {
            if (source.getType().equals(MAVEN)) {
                validators.add(new PomFileValidator(this.projectDir, source.getModules(), source.getPath(),
                        source.getParentPom(), new RepoInfo(this.repoName, licenseName)));
            } else {
                validators.add(OwnVersionValidator.forCli(this.ownVersion));
            }
        }
        return new ValidationPhase(null, validators);
    }

    /*
     * Phase 2 finally analyzes the sources and detects the version of the current project.
     */
    private ValidationPhase phase2(final ValidationPhase.Provision provision) {
        final List<Source> sources = this.config.getSources();
        final List<AnalyzedSource> analyzedSources = SourceAnalyzer.create(this.config, this.mvnRepo, this.ownVersion)
                .analyze(this.projectDir, sources);
        final String projectName = getProjectName(analyzedSources);
        final var brokenLinkReplacer = new BrokenLinkReplacer(this.config.getLinkReplacements());
        final String projectVersion = new ProjectVersionDetector().detectVersion(this.config, analyzedSources);
        final ProjectFilesValidator projectFilesValidator = ProjectFilesValidator.builder() //
                .projectDirectory(this.projectDir) //
                .analyzedSources(analyzedSources) //
                .logger(this.logger) //
                .projectKeeperVersion(this.ownVersion) //
                .hasNpmModule(hasSourceOfType(sources, SourceType.NPM)) //
                .ciBuildOptions(config.getCiBuildConfig()) //
                .build();
        final List<Validator> validators = List.of( //
                projectFilesValidator,
                new ReadmeFileValidator(this.projectDir, projectName, this.repoName, analyzedSources),
                new ChangesFileValidator(projectVersion, projectName, this.projectDir, analyzedSources),
                new DependenciesValidator(analyzedSources, this.projectDir, brokenLinkReplacer),
                new DeletedFilesValidator(this.projectDir), new GitignoreFileValidator(this.projectDir));
        return new ValidationPhase(new ValidationPhase.Provision(projectVersion), validators);
    }

    private boolean hasSourceOfType(final List<Source> sources, final SourceType type) {
        return sources.stream().map(Source::getType).anyMatch(type::equals);
    }

    /*
     * ChangelogFileValidator needs to be in phase 3 since it depends on the result of the ChangesFileValidator.
     */
    private ValidationPhase phase3(final ValidationPhase.Provision provision) {
        final List<Validator> validators = List.of(
                new LatestChangesFileValidator(this.projectDir, provision.projectVersion()),
                new ChangelogFileValidator(this.projectDir));
        return new ValidationPhase(provision, validators);
    }

    private String getProjectName(final List<AnalyzedSource> analyzedSources) {
        return humanReadable(analyzedSources.size() == 1 //
                ? analyzedSources.get(0).getProjectName()
                : this.repoName);
    }

    private static ProjectKeeperConfig readConfig(final Path projectDir) {
        return new ProjectKeeperConfigReader().readConfig(projectDir);
    }

    /**
     * Verify the project structure.
     *
     * <p>
     * PK interprets the validation as "successful" if there are no mandatory findings. Optional findings are ignored in
     * this place.
     * </p>
     *
     * @return {@code true} if project is valid
     */
    public boolean verify() {
        return runValidationPhases(this::handleVerifyFindings);
    }

    private boolean handleVerifyFindings(final List<ValidationFinding> findings) {
        final List<SimpleValidationFinding> findingsFlat = new FindingsUngrouper().ungroupFindings(findings);
        findingsFlat.forEach(this::reportFinding);
        final boolean hasFindingsWithFix = findingsFlat.stream().anyMatch(SimpleValidationFinding::hasFix);
        final boolean hasFindingsWithoutFix = findingsFlat.stream().anyMatch(finding -> !finding.hasFix());
        logValidationFailure(hasFindingsWithFix, hasFindingsWithoutFix);
        return blockers(findingsFlat).isEmpty();
    }

    private void reportFinding(final SimpleValidationFinding finding) {
        if (finding.isOptional()) {
            this.logger.warn(finding.getMessage());
        } else {
            this.logger.error(finding.getMessage());
        }
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
        Provision provision = null;
        for (final Function<Provision, ValidationPhase> phaseSupplier : getValidationPhases()) {
            final ValidationPhase phase = phaseSupplier.apply(provision);
            provision = phase.provision();
            final List<ValidationFinding> findings = runValidation(phase.validators());
            if (!phaseResultHandler.handlePhaseResult(findings)) {
                return false;
            }
        }
        return true;
    }

    private List<ValidationFinding> runValidation(final List<Validator> validators) {
        final List<ValidationFinding> findings = validators.stream().flatMap(validator -> validator.validate().stream())
                .collect(Collectors.toList());
        return new FindingFilter(this.config.getExcludes()).filterFindings(findings);
    }

    private boolean fixFindings(final List<ValidationFinding> findings) {
        final List<SimpleValidationFinding> unfixedFindings = new ArrayList<>(
                new FindingsFixer(this.logger).fixFindings(findings));

        for (final SimpleValidationFinding unfixedFinding : unfixedFindings) {
            this.logger.warn(ExaError.messageBuilder("W-PK-CORE-67")
                    .message("Could not auto-fix: {{finding message|u}}", unfixedFinding.getMessage()).toString());
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

    /**
     * Verify the project and return validation provisions.
     *
     * @return Validation provisions if the validation succeeded
     * @throws IllegalStateException if validation fails
     */
    private Provision getValidationProvision() {
        Provision provision = null;
        for (final Function<Provision, ValidationPhase> phaseSupplier : getValidationPhases()) {
            final ValidationPhase phase = phaseSupplier.apply(provision);
            provision = phase.provision();
            final List<ValidationFinding> findings = runValidation(phase.validators());
            if (!handleVerifyFindings(findings)) {
                throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-175")
                        .message("Validation failed, see log messages for details.")
                        .mitigation("Fix findings and try again.").toString());
            }
        }
        if (provision == null) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-176")
                    .message("Validation did not return required provision.").ticketMitigation().toString());
        }
        return provision;
    }

    /**
     * Update dependencies in the project.
     * 
     * @return {@code true} if the update was successful.
     */
    public boolean updateDependencies() {
        final Provision provision = getValidationProvision();
        return DependencyUpdater.create(logger, projectDir, provision.projectVersion()).updateDependencies();
    }
}
