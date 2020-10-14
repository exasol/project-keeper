package com.exasol.projectkeeper.pom;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.apache.maven.plugin.logging.Log;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.pom.plugin.*;

/**
 * Runner for {@link PomValidator}.
 */
public class PomFileTemplateRunner {

    public static final Collection<PomValidator> ALL_VALIDATORS = List.of(new VersionMavenPluginPomValidator(),
            new OssindexMavenPluginPomValidator(), new EnforcerMavenPluginPomValidator(),
            new AssemblyPluginPomValidator(), new ArtifactReferenceCheckerPluginPomValidator(),
            new SurefirePluginPomValidator(), new JacocoPluginPomValidator(), new FailsafePluginPomValidator());
    private final PomFileIO pomFile;

    /**
     * Create a new instance of {@link PomFileTemplateRunner}.
     * 
     * @param pomFile pom file to create the runner for.
     */
    public PomFileTemplateRunner(final File pomFile) {
        this.pomFile = new PomFileIO(pomFile);
    }

    /**
     * Verify the pom file.
     * 
     * @param log            logger
     * @param enabledModules list of enabled modules
     * @return {@code true}, if verification was successful.
     */
    public boolean verify(final Log log, final Collection<ProjectKeeperModule> enabledModules) {
        final AtomicBoolean success = new AtomicBoolean(true);
        runValidations(enabledModules, finding -> {
            log.error(finding.getMessage());
            success.set(false);
        });
        return success.get();
    }

    /**
     * Fix the pom file.
     * 
     * @param log            logger
     * @param enabledModules list of enabled modules
     */
    public void fix(final Log log, final Collection<ProjectKeeperModule> enabledModules) {
        runValidations(enabledModules, finding -> finding.getFix().fixError());
        this.pomFile.writeChanges();
    }

    private void runValidations(final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<PomValidationFinding> findingConsumer) {
        ALL_VALIDATORS.stream().filter(validator -> enabledModules.contains(validator.getModule()))
                .forEach(template -> template.validate(this.pomFile.getContent(), enabledModules, findingConsumer));
    }
}
