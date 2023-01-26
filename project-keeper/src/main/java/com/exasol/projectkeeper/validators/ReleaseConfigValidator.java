package com.exasol.projectkeeper.validators;

import static java.util.Collections.emptyList;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.config.ProjectKeeperConfigReader;
import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * This class validate release configuration in file {@code release_config.yml}.
 */
public class ReleaseConfigValidator implements Validator {

    static final String RELEASE_CONFIG = "release_config.yml";
    static final String RELEASE_MAVEN = "Maven";
    private static final String PK_CONFIG = ProjectKeeperConfigReader.CONFIG_FILE_NAME;
    private static final String PK_MAVEN = ProjectKeeperModule.MAVEN_CENTRAL.toString().toLowerCase();

    private final List<AnalyzedSource> analyzedSources;
    private final Path releaseConfig;

    /**
     * Create a new instance of {@link ReleaseConfigValidator}.
     *
     * @param projectDirectory project's root directory
     * @param analyzedSources  list of analyzed sources
     */
    public ReleaseConfigValidator(final Path projectDirectory, final List<AnalyzedSource> analyzedSources) {
        this.releaseConfig = projectDirectory.resolve(RELEASE_CONFIG);
        this.analyzedSources = analyzedSources;
    }

    @Override
    public List<ValidationFinding> validate() {
        if (!Files.exists(this.releaseConfig)) {
            return emptyList();
        }
        if (hasSourceWithMavenCentralModule() && !isReleasedToMavenCentral()) {
            return findings(ExaError.messageBuilder("E-PK-CORE-165") //
                    .message("At least one source uses project-keeper module {{pk module}}" //
                            + " but releases are not published to Maven Central.") //
                    .mitigation("Either add release platform {{platform}} to file {{release config}}") //
                    .mitigation("or remove PK module {{pk module}}" //
                            + " from all sources in file {{pk config}}.") //
                    .parameter("release config", RELEASE_CONFIG) //
                    .parameter("platform", RELEASE_MAVEN) //
                    .parameter("pk config", PK_CONFIG) //
                    .parameter("pk module", PK_MAVEN) //
                    .toString());
        }
        if (isReleasedToMavenCentral() && !hasSourceWithMavenCentralModule()) {
            return findings(ExaError.messageBuilder("E-PK-CORE-166") //
                    .message("Releases are configured for publication to Maven Central" //
                            + " but no source uses project-keeper module {{pk module}}.") //
                    .mitigation("Either remove platform {{platform}} from file {{release config}}") //
                    .mitigation("or add PK module {{pk module}} to at least" //
                            + " one of the sources in file {{pk config}}.") //
                    .parameter("release config", RELEASE_CONFIG) //
                    .parameter("platform", RELEASE_MAVEN) //
                    .parameter("pk config", PK_CONFIG) //
                    .parameter("pk module", PK_MAVEN) //
                    .toString());
        }
        return emptyList();
    }

    private List<ValidationFinding> findings(final String message) {
        return List.of(SimpleValidationFinding.withMessage(message).build());
    }

    private boolean hasSourceWithMavenCentralModule() {
        return this.analyzedSources.stream().anyMatch(this::isMaven);
    }

    private boolean isMaven(final AnalyzedSource source) {
        return (source instanceof AnalyzedMavenSource)
                && ((AnalyzedMavenSource) source).getModules().contains(ProjectKeeperModule.MAVEN_CENTRAL);
    }

    private boolean isReleasedToMavenCentral() {
        final Map<String, Object> config = readReleaseConfig();
        if (config == null) {
            return false;
        }
        final Object platforms = config.get("release-platforms");
        if (platforms == null) {
            return false;
        }
        @SuppressWarnings("unchecked")
        final List<String> names = (List<String>) platforms;
        return names.stream() //
                .anyMatch("Maven"::equalsIgnoreCase);
    }

    private Map<String, Object> readReleaseConfig() {
        try (InputStream stream = Files.newInputStream(this.releaseConfig)) {
            return new Yaml().load(stream);
        } catch (final YAMLException | IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-167") //
                    .message("Failed to read file {{file}}", this.releaseConfig) //
                    .toString(), exception);
        }
    }

}
