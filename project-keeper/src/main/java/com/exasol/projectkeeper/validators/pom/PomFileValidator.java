package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.ProjectKeeperModule.DEFAULT;
import static com.exasol.projectkeeper.ProjectKeeperModule.MAVEN_CENTRAL;
import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;

import java.nio.file.Path;
import java.util.*;

import org.w3c.dom.Document;

import com.exasol.projectkeeper.*;
import com.exasol.projectkeeper.validators.finding.*;
import com.exasol.projectkeeper.validators.pom.dependencies.JacocoAgentDependencyValidator;
import com.exasol.projectkeeper.validators.pom.dependencies.LombokDependencyValidator;
import com.exasol.projectkeeper.validators.pom.plugin.*;
import com.exasol.projectkeeper.validators.pom.properties.PomPropertyValidator;

import lombok.Data;

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
    private final Path pomFilePath;
    private final JavaProjectCrawlerRunner javaProjectCrawlerRunner;

    /**
     * Create a new instance of {@link PomFileValidator}.
     *
     * @param enabledModules list of enables modules
     * @param pomFilePath    pom file to create the runner for.
     */
    public PomFileValidator(final Collection<ProjectKeeperModule> enabledModules, final Path pomFilePath,
            final JavaProjectCrawlerRunner javaProjectCrawlerRunner) {
        this.enabledModules = enabledModules;
        this.pomFilePath = pomFilePath;
        this.javaProjectCrawlerRunner = javaProjectCrawlerRunner;
    }

    @Override
    public List<ValidationFinding> validate() {
        final LoadedPom loadedPom = loadPom();
        final List<ValidationFinding> findings = new ArrayList<>();
        ALL_VALIDATORS.stream().filter(validator -> this.enabledModules.contains(validator.getModule()))
                .forEach(template -> template.validate(loadedPom.getPom(), this.enabledModules, findings::add));
        if (!findings.isEmpty()) {
            if (loadedPom.isHasParent()) {
                return new FixRemover().removeFixes(findings);
            } else {
                return List.of(getCompoundFinding(findings, loadedPom.getPom()));
            }
        } else {
            return Collections.emptyList();
        }
    }

    private LoadedPom loadPom() {
        final Document pom = new PomFileIO().parsePomFile(this.pomFilePath);
        final boolean hasParent = runXPath(pom, "/project/parent") != null;
        if (!hasParent) {
            return new LoadedPom(pom, false);
        } else {
            final String flatPomXml = this.javaProjectCrawlerRunner.getFlatPom(this.pomFilePath);
            final Document flatPom = new PomFileIO().parsePomFile(flatPomXml);
            return new LoadedPom(flatPom, true);
        }
    }

    private ValidationFinding getCompoundFinding(final List<ValidationFinding> findings, final Document pom) {
        return new ValidationFindingGroup(findings, () -> new PomFileIO().writePomFile(pom, this.pomFilePath));
    }

    @Data
    private static class LoadedPom {
        private final Document pom;
        private final boolean hasParent;
    }
}
