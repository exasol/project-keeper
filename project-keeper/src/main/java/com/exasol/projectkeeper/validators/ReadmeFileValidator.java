package com.exasol.projectkeeper.validators;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperModule.MAVEN_CENTRAL;
import static com.exasol.projectkeeper.shared.config.ProjectKeeperModule.NATIVE_IMAGE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * This is a {@link Validator} for the {@code README.md} file.
 */
// [impl->dsn~readme-validator~1]
public class ReadmeFileValidator extends AbstractFileContentValidator {
    private static final String NL = System.lineSeparator();
    private static final String NATIVE_IMAGE_DEV_GUIDE = "https://github.com/exasol/project-keeper/blob/main/doc/user_guide/preparing_a_project_for_native_image_builds.md";
    private final String projectName;
    private final String repoName;
    private final List<AnalyzedSource> sources;
    private final boolean hasNativeImageSource;

    /**
     * Create a new instance of {@link ReadmeFileValidator}.
     * 
     * @param projectDirectory project's root directory
     * @param projectName      project name
     * @param repoName         name of the repository
     * @param sources          analyzed sources projects
     */
    public ReadmeFileValidator(final Path projectDirectory, final String projectName, final String repoName,
            final List<AnalyzedSource> sources) {
        super(projectDirectory, Path.of("README.md"));
        this.projectName = projectName;
        this.repoName = repoName;
        this.sources = sources;
        this.hasNativeImageSource = sources.stream().anyMatch(source -> source.getModules().contains(NATIVE_IMAGE));
    }

    @Override
    protected List<ValidationFinding> validateContent(final String content) {
        final List<ValidationFinding> findings = new ArrayList<>();
        if (!content.contains("](dependencies.md)")) {
            findings.add(SimpleValidationFinding.withMessage(ExaError.messageBuilder("E-PK-CORE-61")
                    .message("The project's README.md does not reference the dependencies.md file.'")
                    .mitigation("Please add a link like '[Dependencies](dependencies.md)' to the README.md.")
                    .toString()).build());
        }
        if (!content.contains("](doc/changes/changelog.md)")) {
            findings.add(SimpleValidationFinding.withMessage(ExaError.messageBuilder("E-PK-CORE-64")
                    .message("The project's README.md does not reference the changelog.md file.'")
                    .mitigation("Please add a link like '[Changelog](doc/changes/changelog.md)' to the changelog.md.")
                    .toString()).build());
        }
        if (this.hasNativeImageSource && !content.contains(NATIVE_IMAGE_DEV_GUIDE)) {
            findings.add(SimpleValidationFinding.withMessage(ExaError.messageBuilder("E-PK-CORE-144")
                    .message("The project's README.md does not reference the developer guide for native images.'")
                    .mitigation("Please add the link: '[Developer guide for native-image projects]("
                            + NATIVE_IMAGE_DEV_GUIDE + ")'.")
                    .toString()).build());
        }
        if (!content.contains(getBadges())) {
            findings.add(SimpleValidationFinding.withMessage(ExaError.messageBuilder("E-PK-CORE-62")
                    .message("The project's README.md does not contain a valid badges block.")
                    .mitigation("Please add or replace the following badges: \n{{badge}}.", getBadges()).toString())
                    .build());
        }
        return findings;
    }

    @Override
    protected String getTemplate() {
        String template = "# " + this.projectName + NL + NL + //
                getBadges() + NL + NL + //
                "## Additional Information" + NL + NL + //
                "* [Changelog](doc/changes/changelog.md)" + NL + //
                "* [Dependencies](dependencies.md)";
        if (this.hasNativeImageSource) {
            template += NL + "* [Developer guide for native-image projects](" + NATIVE_IMAGE_DEV_GUIDE + ")";
        }
        return template;
    }

    /**
     * Build the markdown content containing the badges for Build, Maven Central (if enabled) and Sonar.
     * 
     * @return makrdown content
     */
    public String getBadges() {
        String badges = getBuildBadge() + NL;
        final String mavenCentralBadges = getDeploymentBadges();
        if (!mavenCentralBadges.isBlank()) {
            badges += mavenCentralBadges + NL;
        }
        badges += getSonarCloudBadges();
        return badges;
    }

    private String getSonarCloudBadges() {
        final StringBuilder badges = new StringBuilder();
        for (final AnalyzedSource source : this.sources) {
            if (source instanceof final AnalyzedMavenSource mavenSource) {
                getSonarCloudBadgeForMavenSource(badges, mavenSource);
            }
        }
        return badges.toString();
    }

    private void getSonarCloudBadgeForMavenSource(final StringBuilder badges, final AnalyzedMavenSource source) {
        if (source.isRootProject()) {
            final String artifactId = source.getArtifactId();
            badges.append(getSonarCloudBadgesForArtifactId(artifactId));
        }
    }

    private String getSonarCloudBadgesForArtifactId(final String artifactId) {
        return NL + //
                sonarCloudBadge("Quality Gate Status", "alert_status", artifactId) + NL + NL + //
                sonarCloudBadge("Security Rating", "security_rating", artifactId) + NL + //
                sonarCloudBadge("Reliability Rating", "reliability_rating", artifactId) + NL + //
                sonarCloudBadge("Maintainability Rating", "sqale_rating", artifactId) + NL + //
                sonarCloudBadge("Technical Debt", "sqale_index", artifactId) + NL + NL + //
                sonarCloudBadge("Code Smells", "code_smells", artifactId) + NL + //
                sonarCloudBadge("Coverage", "coverage", artifactId) + NL + //
                sonarCloudBadge("Duplicated Lines (%)", "duplicated_lines_density", artifactId) + NL + //
                sonarCloudBadge("Lines of Code", "ncloc", artifactId);
    }

    /**
     * Get badges for platforms like maven-central
     * 
     * @return markdown code
     */
    private String getDeploymentBadges() {
        final List<AnalyzedSource> mavenCentralSources = this.sources.stream()
                .filter(source -> source.getModules().contains(MAVEN_CENTRAL) && source.isAdvertised())
                .toList();
        final boolean hasMultipleModules = mavenCentralSources.size() > 1;
        return mavenCentralSources.stream().map(source -> getDeploymentBadge(source, hasMultipleModules))
                .collect(Collectors.joining(", "));
    }

    private String sonarCloudBadge(final String name, final String metric, final String artifactId) {
        return getBadge(name, "https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3A" + artifactId
                + "&metric=" + metric, "https://sonarcloud.io/dashboard?id=com.exasol%3A" + artifactId);
    }

    private String getBuildBadge() {
        return getBadge("Build Status",
                "https://github.com/exasol/" + this.repoName + "/actions/workflows/ci-build.yml/badge.svg",
                "https://github.com/exasol/" + this.repoName + "/actions/workflows/ci-build.yml");
    }

    private String getDeploymentBadge(final AnalyzedSource source, final boolean withProjectName) {
        if (source instanceof final AnalyzedMavenSource mavenSource) {
            return getMavenCentralBadge(withProjectName, mavenSource);
        } else {
            throw new UnsupportedOperationException(ExaError.messageBuilder("E-PK-CORE-92")
                    .message("Project keeper does not know how to build a badge for source of type {{source type}}.",
                            source.getClass().getName())
                    .ticketMitigation().toString());
        }
    }

    private String getMavenCentralBadge(final boolean withProjectName, final AnalyzedMavenSource mavenSource) {
        final String badge = getBadge("Maven Central &ndash; " + mavenSource.getProjectName(),
                "https://img.shields.io/maven-central/v/com.exasol/" + mavenSource.getArtifactId(),
                "https://search.maven.org/artifact/com.exasol/" + mavenSource.getArtifactId());
        if (withProjectName) {
            return mavenSource.getProjectName() + ": " + badge;
        } else {
            return badge;
        }
    }

    private String getBadge(final String name, final String image, final String link) {
        return String.format("[![%s](%s)](%s)", name, image, link);
    }
}
