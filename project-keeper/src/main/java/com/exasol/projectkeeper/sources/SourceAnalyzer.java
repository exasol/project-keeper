package com.exasol.projectkeeper.sources;

import static com.exasol.projectkeeper.config.ProjectKeeperConfig.SourceType.MAVEN;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;

/**
 * This class analyzes source projects.
 */
public class SourceAnalyzer {

    /**
     * Analyze a source project.
     * 
     * @param projectDir project directory
     * @param sources    configured sources
     * @return analyzed sources
     */
    public List<AnalyzedSource> analyze(final Path projectDir, final List<ProjectKeeperConfig.Source> sources) {
        final List<AnalyzedSource> result = new ArrayList<>();
        for (final ProjectKeeperConfig.Source source : sources) {
            result.add(analyzeSource(projectDir, source));
        }
        return result;
    }

    private AnalyzedSource analyzeSource(final Path projectDir, final ProjectKeeperConfig.Source source) {
        if (MAVEN.equals(source.getType())) {
            final Model model = readMavenModel(source);
            model.getArtifactId();
            final boolean isRoot = projectDir.relativize(source.getPath()).equals(Path.of("pom.xml"));
            return new AnalyzedMavenSource(source.getPath(), source.getModules(), source.isAdvertise(),
                    model.getArtifactId(), model.getName(), isRoot);
        } else {
            throw new UnsupportedOperationException(ExaError.messageBuilder("F-PK-CORE-93")
                    .message("Analyzing of {{type}}} is not supported yet. ", source.getType()).ticketMitigation()
                    .toString());
        }
    }

    private Model readMavenModel(final ProjectKeeperConfig.Source source) {
        try (final FileInputStream fileInputStream = new FileInputStream(source.getPath().toFile())) {
            return new MavenXpp3Reader().read(fileInputStream);
        } catch (final XmlPullParserException | IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-94").message("Failed to analyze maven source.").toString(),
                    exception);
        }
    }
}