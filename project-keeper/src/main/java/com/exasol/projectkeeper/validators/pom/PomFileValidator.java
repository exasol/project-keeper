package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.ProjectKeeperModule.DEFAULT;
import static com.exasol.projectkeeper.ProjectKeeperModule.MAVEN_CENTRAL;

import java.nio.file.Path;
import java.util.*;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFindingGroup;
import com.exasol.projectkeeper.validators.pom.dependencies.JacocoAgentDependencyValidator;
import com.exasol.projectkeeper.validators.pom.dependencies.LombokDependencyValidator;
import com.exasol.projectkeeper.validators.pom.plugin.*;
import com.exasol.projectkeeper.validators.pom.properties.PomPropertyValidator;

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
            new JarPluginValidator(), new LombokPluginValidator(),
            new PomPropertyValidator("/project/properties/project.build.sourceEncoding", "UTF-8", DEFAULT),
            new PomPropertyValidator("/project/properties/project.reporting.outputEncoding", "UTF-8", DEFAULT),
            new PomPropertyValidator("/project/properties/gpg.skip", "true", MAVEN_CENTRAL),
            new LombokDependencyValidator());
    final Collection<ProjectKeeperModule> enabledModules;
    private final PomFileIO pomFileIO;

    /**
     * Create a new instance of {@link PomFileValidator}.
     *
     * @param enabledModules list of enables modules
     * @param pomFile        pom file to create the runner for.
     */
    public PomFileValidator(final Collection<ProjectKeeperModule> enabledModules, final Path pomFile) {
        this.enabledModules = enabledModules;
        this.pomFileIO = new PomFileIO(pomFile);
    }

    @Override
    public List<ValidationFinding> validate() {
        final List<ValidationFinding> findings = new ArrayList<>();
        ALL_VALIDATORS.stream().filter(validator -> this.enabledModules.contains(validator.getModule())).forEach(
                template -> template.validate(this.pomFileIO.getContent(), this.enabledModules, findings::add));
        if (!findings.isEmpty()) {
            return List.of(getCompoundFinding(findings));
        } else {
            return Collections.emptyList();
        }
    }

    private ValidationFinding getCompoundFinding(final List<ValidationFinding> findings) {
        return new ValidationFindingGroup(findings, this.pomFileIO::writeChanges);
    }
}
