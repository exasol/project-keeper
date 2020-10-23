package com.exasol.projectkeeper.validators.pom;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.validators.pom.plugin.*;

/**
 * Runner for {@link PomValidator}.
 */
public class PomFileValidationRunner implements Validator {
    public static final Collection<PomValidator> ALL_VALIDATORS = List.of(new VersionMavenPluginPomValidator(),
            new OssindexMavenPluginPomValidator(), new EnforcerMavenPluginPomValidator(),
            new AssemblyPluginPomValidator(), new ArtifactReferenceCheckerPluginPomValidator(),
            new SurefirePluginPomValidator(), new JacocoPluginPomValidator(), new FailsafePluginPomValidator());
    final Collection<ProjectKeeperModule> enabledModules;
    private final PomFileIO pomFile;

    /**
     * Create a new instance of {@link PomFileValidationRunner}.
     *
     * @param enabledModules list of enables modules
     * @param pomFile        pom file to create the runner for.
     */
    public PomFileValidationRunner(final Collection<ProjectKeeperModule> enabledModules, final File pomFile) {
        this.enabledModules = enabledModules;
        this.pomFile = new PomFileIO(pomFile);
    }

    @Override
    public PomFileValidationRunner validate(final Consumer<ValidationFinding> findingConsumer) {
        ALL_VALIDATORS.stream().filter(validator -> this.enabledModules.contains(validator.getModule())).forEach(
                template -> template.validate(this.pomFile.getContent(), this.enabledModules, findingConsumer));
        return this;
    }

    @Override
    public PomFileValidationRunner flush() {
        this.pomFile.writeChanges();
        return this;
    }
}
