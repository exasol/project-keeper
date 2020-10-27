package com.exasol.projectkeeper.validators.pom;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.validators.pom.plugin.*;

/**
 * Validator for the pom.xml file. This class runs different {@link PomValidator}s.
 */
public class PomFileValidator implements Validator {
    public static final Collection<PomValidator> ALL_VALIDATORS = List.of(new VersionMavenPluginPomValidator(),
            new OssindexMavenPluginPomValidator(), new EnforcerMavenPluginPomValidator(),
            new AssemblyPluginPomValidator(), new ArtifactReferenceCheckerPluginPomValidator(),
            new SurefirePluginPomValidator(), new JacocoPluginPomValidator(), new FailsafePluginPomValidator());
    final Collection<ProjectKeeperModule> enabledModules;
    private final PomFileIO pomFile;

    /**
     * Create a new instance of {@link PomFileValidator}.
     *
     * @param enabledModules list of enables modules
     * @param pomFile        pom file to create the runner for.
     */
    public PomFileValidator(final Collection<ProjectKeeperModule> enabledModules, final PomFileIO pomFile) {
        this.enabledModules = enabledModules;
        this.pomFile = pomFile;
    }

    @Override
    public PomFileValidator validate(final Consumer<ValidationFinding> findingConsumer) {
        ALL_VALIDATORS.stream().filter(validator -> this.enabledModules.contains(validator.getModule())).forEach(
                template -> template.validate(this.pomFile.getContent(), this.enabledModules, findingConsumer));
        return this;
    }
}
