package com.exasol.projectkeeper.validators.changesfile;

import static com.exasol.projectkeeper.validators.changesfile.ChangesFile.DEPENDENCY_UPDATES_HEADING;

import java.nio.file.Path;
import java.util.*;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.pom.MavenProjectFromFileReader;
import com.exasol.projectkeeper.validators.changesfile.dependencies.*;

/**
 * This class fixes the dependency section of a {@link ChangesFile}.
 */
class DependencySectionFixer {
    private final MavenProjectFromFileReader mavenModelReader;
    private final Path projectDirectory;
    private final Model currentMavenModel;

    /**
     * Create a new instance of {@link DependencySectionFixer}.
     *
     * @param mavenModelReader reader for maven model
     * @param projectDirectory projects root directory
     */
    public DependencySectionFixer(final MavenProjectFromFileReader mavenModelReader, final Path projectDirectory) {
        this.mavenModelReader = mavenModelReader;
        this.projectDirectory = projectDirectory;
        this.currentMavenModel = parseCurrentPomFile(projectDirectory.resolve("pom.xml"));
    }

    private Model parseCurrentPomFile(final Path pomFile) {
        try {
            return this.mavenModelReader.readProject(pomFile.toFile()).getModel();
        } catch (final MavenProjectFromFileReader.ReadFailedException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-42").message("Failed to parse current pom file.").toString(),
                    exception);
        }
    }

    /**
     * Fix the dependency section of a changes file.
     * 
     * @param changesFile changes file to fix
     * @return fixed changes file.
     */
    public ChangesFile fix(final ChangesFile changesFile) {
        final var oldModel = getOldModel();
        final DependencyChangeReport report = new DependencyChangeReportReader().read(oldModel, this.currentMavenModel);
        final List<String> renderedReport = new DependencyChangeReportRenderer().render(report);
        final List<ChangesFileSection> sections = new ArrayList<>(changesFile.getSections());
        removeDependencySection(sections);
        sections.add(new ChangesFileSection(renderedReport));
        return new ChangesFile(List.copyOf(changesFile.getHeaderSectionLines()), sections);
    }

    private void removeDependencySection(final List<ChangesFileSection> sections) {
        sections.removeIf(section -> section.getHeading().compareToIgnoreCase(DEPENDENCY_UPDATES_HEADING) == 0);
    }

    private Model getOldModel() {
        final Optional<String> lastReleasesPomFile = new LastReleasePomFileReader()
                .readLatestReleasesPomFile(this.projectDirectory, this.currentMavenModel.getVersion());
        if (lastReleasesPomFile.isPresent()) {
            return parseOldPomFile(lastReleasesPomFile.get());
        } else {
            final var emptyModel = new Model();
            final var build = new Build();
            emptyModel.setBuild(build);
            return emptyModel;
        }
    }

    private Model parseOldPomFile(final String pomFileContents) {
        try (final var temporaryPomFile = new TemporaryPomFile(pomFileContents)) {
            return this.mavenModelReader.readProject(temporaryPomFile.getPomFile().toFile()).getModel();
        } catch (final MavenProjectFromFileReader.ReadFailedException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-38")
                    .message("Failed to parse pom file of previous release.").toString(), exception);
        }
    }

}
