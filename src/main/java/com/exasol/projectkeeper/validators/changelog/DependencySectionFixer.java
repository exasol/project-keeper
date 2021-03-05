package com.exasol.projectkeeper.validators.changelog;

import static com.exasol.projectkeeper.validators.changelog.ChangelogFile.DEPENDENCY_UPDATES_HEADLINE;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelBuildingException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.validators.changelog.dependencies.*;

/**
 * This class fixes the dependency section of a {@link ChangelogFile}.
 */
class DependencySectionFixer {
    private final Path projectDirectory;
    private final Model currentMavenModel;

    /**
     * Create a new instance of {@link DependencySectionFixer}.
     * 
     * @param projectDirectory projects root directory
     */
    public DependencySectionFixer(final Path projectDirectory) {
        this.projectDirectory = projectDirectory;
        this.currentMavenModel = parseCurrentPomFile(projectDirectory.resolve("pom.xml"));
    }

    private static Model parseCurrentPomFile(final Path pomFile) {
        try (final FileReader reader = new FileReader(pomFile.toFile())) {
            return new MavenModelReader().readModel(reader);
        } catch (final XmlPullParserException | IOException | ModelBuildingException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-42").message("Failed to parse current pom file.").toString(),
                    exception);
        }
    }

    /**
     * Fix the dependency section of a changelog file.
     * 
     * @param changelogFile changelog file to fix
     * @return fixed changelog file.
     */
    public ChangelogFile fix(final ChangelogFile changelogFile) {
        final Model oldModel = getOldModel();
        final DependencyChangeReport report = new DependencyChangeReportReader().read(oldModel, this.currentMavenModel);
        final List<String> renderedReport = new DependencyChangeReportRenderer().render(report);
        final List<ChangelogSection> sections = new ArrayList<>(changelogFile.getSections());
        removeDependencySection(sections);
        sections.add(new ChangelogSection(renderedReport));
        return new ChangelogFile(List.copyOf(changelogFile.getHeader()), sections);
    }

    private void removeDependencySection(final List<ChangelogSection> sections) {
        sections.removeIf(section -> section.getHeadline().compareToIgnoreCase(DEPENDENCY_UPDATES_HEADLINE) == 0);
    }

    private Model getOldModel() {
        final Optional<String> lastReleasesPomFile = new LastReleasePomFileReader()
                .readLatestReleasesPomFile(this.projectDirectory);
        if (lastReleasesPomFile.isPresent()) {
            return parseOldPomFile(lastReleasesPomFile.get());
        } else {
            final Model emptyModel = new Model();
            final Build build = new Build();
            emptyModel.setBuild(build);
            return emptyModel;
        }
    }

    private Model parseOldPomFile(final String pomFile) {
        try (final StringReader reader = new StringReader(pomFile)) {
            return new MavenModelReader().readModel(reader);
        } catch (final XmlPullParserException | IOException | ModelBuildingException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-38")
                    .message("Failed to parse pom file of previous release.").toString(), exception);
        }
    }
}
