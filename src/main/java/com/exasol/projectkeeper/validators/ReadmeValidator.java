package com.exasol.projectkeeper.validators;

import static com.exasol.projectkeeper.ProjectKeeperModule.MAVEN_CENTRAL;

import java.nio.file.Path;
import java.util.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.*;

/**
 * This is a {@link Validator} for the README.md.
 */
// [impl->dsn~readme-validator~1]
public class ReadmeValidator extends AbstractFileStringValidator {
    private static final String NL = System.lineSeparator();
    private final String projectName;
    private final String artifactId;
    private final String repoName;
    private final Collection<ProjectKeeperModule> enabledModules;

    /**
     * Create a new instance of {@link ReadmeValidator}.
     * 
     * @param projectDirectory project's root directory
     * @param projectName      project name
     * @param artifactId       artifact id of the maven artifact
     * @param repoName         name of the repository
     * @param enabledModules   list of enable modules
     */
    public ReadmeValidator(final Path projectDirectory, final String projectName, final String artifactId,
            final String repoName, final Collection<ProjectKeeperModule> enabledModules) {
        super(projectDirectory, Path.of("README.md"));
        this.projectName = projectName;
        this.artifactId = artifactId;
        this.repoName = repoName;
        this.enabledModules = enabledModules;
    }

    @Override
    protected List<ValidationFinding> validateContent(final String content) {
        final List<ValidationFinding> findings = new ArrayList<>();
        if (!content.contains("](dependencies.md)")) {
            findings.add(ValidationFinding.withMessage(ExaError.messageBuilder("E-PK-61")
                    .message("The project's README.md does not reference the dependencies.md file.'")
                    .mitigation("Please add a link like '[Dependencies](dependencies.md)' to the README.md.")
                    .toString()).build());
        }
        if (!content.contains("](doc/changes/changelog.md)")) {
            findings.add(ValidationFinding.withMessage(ExaError.messageBuilder("E-PK-64")
                    .message("The project's README.md does not reference the changelog.md file.'")
                    .mitigation("Please add a link like '[Changelog](doc/changes/changelog.md)' to the changelog.md.")
                    .toString()).build());
        }
        if (!content.contains(getBadges())) {
            findings.add(ValidationFinding.withMessage(ExaError.messageBuilder("E-PK-62")
                    .message("The project's README.md does not contain a valid badges block.")
                    .mitigation("Please add or replace the following badges: \n{{badge}}.", getBadges()).toString())
                    .build());
        }
        return findings;
    }

    @Override
    protected String getTemplate() {
        return "# " + this.projectName + NL + NL + //
                getBadges() + NL + NL + //
                "## Additional Information" + NL + NL + //
                "* [Changelog](doc/changes/changelog.md)" + NL + //
                "* [Dependencies](dependencies.md)";
    }

    public String getBadges() {
        String badges = getBuildBadge() + NL;
        if (this.enabledModules.contains(MAVEN_CENTRAL)) {
            badges += getMavenCentralBadge() + NL;
        }
        badges += NL + //
                sonarCloudBadge("Quality Gate Status", "alert_status") + NL + NL + //
                sonarCloudBadge("Security Rating", "security_rating") + NL + //
                sonarCloudBadge("Reliability Rating", "reliability_rating") + NL + //
                sonarCloudBadge("Maintainability Rating", "sqale_rating") + NL + //
                sonarCloudBadge("Technical Debt", "sqale_index") + NL + NL + //
                sonarCloudBadge("Code Smells", "code_smells") + NL + //
                sonarCloudBadge("Coverage", "coverage") + NL + //
                sonarCloudBadge("Duplicated Lines (%)", "duplicated_lines_density") + NL + //
                sonarCloudBadge("Lines of Code", "ncloc");
        return badges;
    }

    private String sonarCloudBadge(final String name, final String metric) {
        return getBadge(name, "https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3A" + this.artifactId
                + "&metric=" + metric, "https://sonarcloud.io/dashboard?id=com.exasol%3A" + this.artifactId);
    }

    private String getBuildBadge() {
        return getBadge("Build Status",
                "https://github.com/exasol/" + this.repoName + "/actions/workflows/ci-build.yml/badge.svg",
                "https://github.com/exasol/" + this.repoName + "/actions/workflows/ci-build.yml");
    }

    private String getMavenCentralBadge() {
        return getBadge("Maven Central", "https://img.shields.io/maven-central/v/com.exasol/" + this.artifactId,
                "https://search.maven.org/artifact/com.exasol/" + this.artifactId);
    }

    private String getBadge(final String name, final String image, final String link) {
        return String.format("[![%s](%s)](%s)", name, image, link);
    }
}
