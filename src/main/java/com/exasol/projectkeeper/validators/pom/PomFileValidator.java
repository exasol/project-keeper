package com.exasol.projectkeeper.validators.pom;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.*;
import com.exasol.projectkeeper.validators.pom.dependencies.JacocoAgentDependencyValidator;
import com.exasol.projectkeeper.validators.pom.plugin.*;

/**
 * Validator for the pom.xml file. This class runs different {@link PomValidator}s.
 */
public class PomFileValidator implements Validator {
    /** List of all validators. */
    public static final Collection<PomValidator> ALL_VALIDATORS = List.of(new VersionMavenPluginPomValidator(),
            new OssindexMavenPluginPomValidator(), new EnforcerMavenPluginPomValidator(),
            new AssemblyPluginPomValidator(), new ArtifactReferenceCheckerPluginPomValidator(),
            new SurefirePluginPomValidator(), new JacocoPluginPomValidator(), new FailsafePluginPomValidator(),
            new GpgPluginValidator(), new DeployPluginValidator(), new NexusStagingPluginValidator(),
            new JacocoAgentDependencyValidator(), new DependencyPluginValidator(), new SourcePluginValidator(),
            new JavadocPluginValidator(), new ErrorCodeCrawlerPluginValidator(), new ReproducibleBuildPluginValidator(),
            new JarPluginValidator());
    final Collection<ProjectKeeperModule> enabledModules;
    private final Collection<String> excludedPlugins;
    private final PomFileIO pomFileIO;

    /**
     * Create a new instance of {@link PomFileValidator}.
     *
     * @param enabledModules  list of enables modules
     * @param excludedPlugins list of plugins (group_id:artifact_id) to exclude
     * @param pomFile         pom file to create the runner for.
     */
    public PomFileValidator(final Collection<ProjectKeeperModule> enabledModules,
            final Collection<String> excludedPlugins, final File pomFile) {
        this.enabledModules = enabledModules;
        this.excludedPlugins = excludedPlugins;
        this.pomFileIO = new PomFileIO(pomFile);
    }

    @Override
    public List<ValidationFinding> validate() {
        final List<ValidationFinding> findings = new ArrayList<>();
        ALL_VALIDATORS.stream().filter(validator -> this.enabledModules.contains(validator.getModule()))
                .filter(validator -> !validator.isExcluded(this.excludedPlugins)).forEach(
                        template -> template.validate(this.pomFileIO.getContent(), this.enabledModules, findings::add));
        if (!findings.isEmpty()) {
            return List.of(getCompoundFinding(findings));
        } else {
            return Collections.emptyList();
        }
    }

    private ValidationFinding getCompoundFinding(final List<ValidationFinding> findings) {
        return ValidationFinding.withMessage(ExaError.messageBuilder("E-PK-45")
                .message("Pom file is invalid:\n{{pom findings|uq}}", concatFindingMessages(findings)).toString())
                .andFix(getFix(findings)).build();
    }

    private String concatFindingMessages(final List<ValidationFinding> findings) {
        return findings.stream().map(ValidationFinding::getMessage).collect(Collectors.joining("\n"));
    }

    private ValidationFinding.Fix getFix(final List<ValidationFinding> findings) {
        return log -> {
            findings.forEach(finding -> finding.getFix().fixError(log));
            this.pomFileIO.writeChanges();
        };
    }
}
