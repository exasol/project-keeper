package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.validators.files.RequiredFileValidator.withContentEqualTo;
import static com.exasol.projectkeeper.validators.pom.XmlHelper.addTextElement;
import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;

import java.nio.file.Path;
import java.util.*;

import org.w3c.dom.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.validators.files.RequiredFileValidator;
import com.exasol.projectkeeper.validators.finding.*;

/**
 * Validator for the pom.xml file.
 */
//[impl->dsn~pom-file-validator~1]
public class PomFileValidator implements Validator {
    private static final String PARENT_POM_MITIGATION = "Check the project-keeper user guide if you need a parent pom.";
    private final Path projectDirectory;
    final Collection<ProjectKeeperModule> enabledModules;
    private final Path pomFilePath;
    private final ProjectKeeperConfig.ParentPomRef parentPomRef;

    /**
     * Create a new instance of {@link PomFileValidator}.
     *
     * @param projectDirectory project directory
     * @param enabledModules   collection of enables modules
     * @param pomFilePath      pom file to create the runner for
     * @param parentPomRef     reference to a parent pom or {@code null}
     */
    public PomFileValidator(final Path projectDirectory, final Collection<ProjectKeeperModule> enabledModules,
            final Path pomFilePath, final ProjectKeeperConfig.ParentPomRef parentPomRef) {
        this.projectDirectory = projectDirectory;
        this.enabledModules = enabledModules;
        this.pomFilePath = pomFilePath;
        this.parentPomRef = parentPomRef;
    }

    @Override
    public List<ValidationFinding> validate() {
        try {
            final Document pom = new PomFileIO().parsePomFile(this.pomFilePath);
            final String version = getProjectVersion(pom);
            final String artifactId = getRequiredTextValue(pom, "/project/artifactId") + "-generated-parent";
            final String groupId = getGroupId(pom);
            final List<ValidationFinding> findings = new ArrayList<>();
            final Path generatedPomPath = this.pomFilePath.getParent().resolve("pk_generated_parent.pom");
            findings.addAll(validateParentTag(pom, version, artifactId, groupId, generatedPomPath));
            findings.addAll(validateGeneratedPomFile(groupId, artifactId, version, generatedPomPath));
            if (this.enabledModules.contains(ProjectKeeperModule.JAR_ARTIFACT)) {
                findings.addAll(validateAssemblyPlugin(pom, this.projectDirectory.relativize(this.pomFilePath)));
            }
            return wrapFindingsWithGroupThatWritesPom(pom, findings);
        } catch (final InvalidPomException exception) {
            return List.of(SimpleValidationFinding.withMessage(exception.getMessage()).build());
        }
    }

    private List<ValidationFinding> wrapFindingsWithGroupThatWritesPom(final Document pom,
            final List<ValidationFinding> findings) {
        if (hasFindingWithFix(findings)) {
            return List.of(
                    new ValidationFindingGroup(findings, () -> new PomFileIO().writePomFile(pom, this.pomFilePath)));
        } else {
            return findings;
        }
    }

    private boolean hasFindingWithFix(final List<ValidationFinding> findings) {
        return findings.stream().anyMatch(finding -> //
        (finding instanceof ValidationFindingGroup) || //
                (finding instanceof SimpleValidationFinding && ((SimpleValidationFinding) finding).hasFix()));
    }

    private String getProjectVersion(final Document pom) throws InvalidPomException {
        final Node versionNode = runXPath(pom, "/project/version");
        if (versionNode != null) {
            return versionNode.getTextContent();
        } else if (this.parentPomRef != null && this.parentPomRef.getVersion() != null) {
            return this.parentPomRef.getVersion();
        } else {
            throw new InvalidPomException(ExaError.messageBuilder("E-PK-CORE-111")
                    .message("Invalid pom file {{file}}: Missing required property /project/version.",
                            this.projectDirectory.relativize(this.pomFilePath))
                    .mitigation("Please either set /project/version manually.").toString());
        }
    }

    private List<ValidationFinding> validateAssemblyPlugin(final Node pom, final Path relativePomPath) {
        final Node finalNameProperty = runXPath(pom,
                "/project/build/plugins/plugin[artifactId/text()='maven-assembly-plugin']/configuration/finalName");
        if (finalNameProperty == null || finalNameProperty.getTextContent().isBlank()) {
            return List.of(SimpleValidationFinding.withMessage(ExaError.messageBuilder("E-PK-CORE-105").message(
                    "Invalid pom file {{file}}: Missing required property finalName property in maven-assembly-plugin.",
                    relativePomPath)
                    .mitigation("Use the following template and set finalName:\n" + "<plugin>\n"
                            + "    <artifactId>maven-assembly-plugin</artifactId>\n"
                            + "    <groupId>org.apache.maven.plugins</groupId>\n" + "    <configuration>\n"
                            + "        <finalName>NAME_OF_YOUR_JAR</finalName>\n" + "    </configuration>\n"
                            + "</plugin>")
                    .toString()).build());
        } else {
            return Collections.emptyList();
        }
    }

    private List<ValidationFinding> validateParentTag(final Document pom, final String version, final String artifactId,
            final String groupId, final Path generatedPomPath) {
        final Node parentTag = runXPath(pom, "/project/parent");
        if (parentTag == null) {
            return List.of(SimpleValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-CORE-103")
                            .message("Missing parent declaration in {{pom file}}",
                                    this.projectDirectory.relativize(this.pomFilePath))
                            .toString())
                    .andFix(getAddParentToPomFileFix(pom, version, artifactId, groupId, generatedPomPath)).build());
        } else {
            return validateExistingParentTag(version, artifactId, groupId, generatedPomPath, parentTag);
        }
    }

    private List<ValidationFinding> validateExistingParentTag(final String version, final String artifactId,
            final String groupId, final Path generatedPomPath, final Node parentTag) {
        final List<ValidationFinding> findings = new ArrayList<>();
        checkParentProperty(generatedPomPath, parentTag, "artifactId", artifactId).ifPresent(findings::add);
        checkParentProperty(generatedPomPath, parentTag, "groupId", groupId).ifPresent(findings::add);
        checkParentRelativePath(generatedPomPath, parentTag, this.pomFilePath.getParent().relativize(generatedPomPath))
                .ifPresent(findings::add);
        checkParentVersion(version, generatedPomPath, parentTag, !findings.isEmpty()).ifPresent(findings::add);
        return findings;
    }

    private Optional<SimpleValidationFinding> checkParentVersion(final String version, final Path generatedPomPath,
            final Node parentTag, final boolean hasOtherFindings) {
        final Node node = runXPath(parentTag, "version");
        if (node == null || !version.equals(node.getTextContent())) {
            final SimpleValidationFinding.Builder findingBuilder = SimpleValidationFinding.withMessage(ExaError
                    .messageBuilder("E-PK-CORE-118")
                    .message(
                            "Invalid pom file {{file}}: Invalid '/project/parent/version'. Expected value is {{expected}}. The pom must declare {{generated parent}} as parent pom.",
                            this.projectDirectory.relativize(this.pomFilePath), version,
                            this.projectDirectory.relativize(generatedPomPath))
                    .mitigation(PARENT_POM_MITIGATION).toString());
            if (!hasOtherFindings && node != null) {
                /*
                 * If there are no other findings we can automatically fix the version. Otherwise, better not since it
                 * could be the reference to a non generated parent pom.
                 */
                findingBuilder.andFix(log -> node.setTextContent(version));
            }
            final SimpleValidationFinding finding = findingBuilder.build();
            return Optional.of(finding);
        } else {
            return Optional.empty();
        }
    }

    private Optional<ValidationFinding> checkParentRelativePath(final Path generatedPomPath, final Node parentTag,
            final Path expectedValue) {
        final Node node = runXPath(parentTag, "relativePath");
        if (node == null || !comparePaths(expectedValue, Path.of(node.getTextContent()))) {
            return Optional.of(SimpleValidationFinding.withMessage(ExaError.messageBuilder("E-PK-CORE-112").message(
                    "Invalid pom file {{file}}: Invalid '/project/parent/relativePath'. Expected value is {{expected}}. The pom must declare {{generated parent}} as parent pom.",
                    this.projectDirectory.relativize(this.pomFilePath), expectedValue,
                    this.projectDirectory.relativize(generatedPomPath)).mitigation(PARENT_POM_MITIGATION).toString())
                    .build());
        } else {
            return Optional.empty();
        }
    }

    private boolean comparePaths(final Path expectedValue, final Path other) {
        return other.normalize().equals(expectedValue.normalize());
    }

    private Optional<ValidationFinding> checkParentProperty(final Path generatedPomPath, final Node parentTag,
            final String property, final String expectedValue) {
        final Node node = runXPath(parentTag, property);
        if (node == null || !node.getTextContent().equals(expectedValue)) {
            return Optional.of(SimpleValidationFinding.withMessage(ExaError.messageBuilder("E-PK-CORE-104").message(
                    "Invalid pom file {{file}}: Invalid '/project/parent/{{property|uq}}'. Expected value is {{expected}}. The pom must declare {{generated parent}} as parent pom.",
                    this.projectDirectory.relativize(this.pomFilePath), property, expectedValue,
                    this.projectDirectory.relativize(generatedPomPath)).mitigation(PARENT_POM_MITIGATION).toString())
                    .build());
        } else {
            return Optional.empty();
        }
    }

    private SimpleValidationFinding.Fix getAddParentToPomFileFix(final Document pom, final String version,
            final String artifactId, final String groupId, final Path generatedPomPath) {
        return log -> {
            final Node project = runXPath(pom, "/project");
            final Element parent = pom.createElement("parent");
            addTextElement(parent, "artifactId", artifactId);
            addTextElement(parent, "groupId", groupId);
            addTextElement(parent, "version", version);
            addTextElement(parent, "relativePath",
                    this.pomFilePath.getParent().relativize(generatedPomPath).toString());
            project.appendChild(parent);
        };
    }

    private List<ValidationFinding> validateGeneratedPomFile(final String groupId, final String artifactId,
            final String version, final Path generatedPomPath) {
        final String generatedContent = new PomFileGenerator().generatePomContent(this.enabledModules, groupId,
                artifactId, version, this.parentPomRef);
        return new RequiredFileValidator().validateFile(this.projectDirectory, generatedPomPath,
                withContentEqualTo(generatedContent));
    }

    private String getGroupId(final Document pom) throws InvalidPomException {
        final Node node = runXPath(pom, "/project/groupId");
        if (node != null) {
            return node.getTextContent();
        } else {
            final Node parentGroupIdNode = runXPath(pom, "/project/parent/groupId");
            if (parentGroupIdNode != null) {
                return parentGroupIdNode.getTextContent();
            } else {
                throw new InvalidPomException(ExaError.messageBuilder("E-PK-CORE-102")
                        .message("Invalid pom file {{file}}: Missing required property 'groupId'.",
                                this.projectDirectory.relativize(this.pomFilePath))
                        .mitigation("Please either set '/project/groupId' or '/project/parent/groupId'.").toString());
            }
        }
    }

    private String getRequiredTextValue(final Node pom, final String xPath) throws InvalidPomException {
        final Node node = runXPath(pom, xPath);
        if (node == null) {
            throw new InvalidPomException(ExaError.messageBuilder("E-PK-CORE-101")
                    .message("Invalid pom file {{file}}: Missing required property {{property}}.",
                            this.projectDirectory.relativize(this.pomFilePath), xPath)
                    .mitigation("Please set the property manually.").toString());
        }
        return node.getTextContent();
    }

    private static class InvalidPomException extends Exception {
        public InvalidPomException(final String message) {
            super(message);
        }
    }
}
