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
import com.exasol.projectkeeper.validators.files.RequiredFileValidator;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * Validator for the pom.xml file.
 */
//[impl->dsn~pom-file-validator~1]
public class PomFileValidator implements Validator {
    private final Path projectDirectory;
    final Collection<ProjectKeeperModule> enabledModules;
    private final Path pomFilePath;

    /**
     * Create a new instance of {@link PomFileValidator}.
     *
     * @param projectDirectory project directory
     * @param enabledModules   collection of enables modules
     * @param pomFilePath      pom file to create the runner for
     */
    public PomFileValidator(final Path projectDirectory, final Collection<ProjectKeeperModule> enabledModules,
            final Path pomFilePath) {
        this.projectDirectory = projectDirectory;
        this.enabledModules = enabledModules;
        this.pomFilePath = pomFilePath;
    }

    @Override
    public List<ValidationFinding> validate() {
        try {
            final Document pom = new PomFileIO().parsePomFile(this.pomFilePath);
            final String version = getRequiredTextValue(pom, "/project/version");
            final String artifactId = getRequiredTextValue(pom, "/project/artifactId") + "-generated-parent";
            final String groupId = getGroupId(pom);
            final List<ValidationFinding> findings = new ArrayList<>();
            final Path generatedPomPath = this.pomFilePath.getParent().resolve("pk_generated_parent.pom");
            findings.addAll(validateParentTag(pom, version, artifactId, groupId, generatedPomPath));
            findings.addAll(validateGeneratedPomFile(groupId, artifactId, version, generatedPomPath));
            findings.addAll(validateAssemblyPlugin(pom, this.projectDirectory.relativize(this.pomFilePath)));
            return findings;
        } catch (final InvalidPomException exception) {
            return List.of(SimpleValidationFinding.withMessage(exception.getMessage()).build());
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
        checkParentProperty(generatedPomPath, parentTag, "version", version).ifPresent(findings::add);
        checkParentProperty(generatedPomPath, parentTag, "relativePath",
                this.pomFilePath.getParent().relativize(generatedPomPath).toString()).ifPresent(findings::add);
        return findings;
    }

    private Optional<ValidationFinding> checkParentProperty(final Path generatedPomPath, final Node parentTag,
            final String property, final String expectedValue) {
        final Node node = runXPath(parentTag, property);
        if (node == null || !node.getTextContent().equals(expectedValue)) {
            return Optional.of(SimpleValidationFinding.withMessage(ExaError.messageBuilder("E-PK-CORE-104").message(
                    "Invalid pom file {{file}}: Invalid '/project/parent/{{property|uq}}'. Expected value is {{expected}}. The pom must declare {{generated parent}} as parent pom.",
                    this.projectDirectory.relativize(this.pomFilePath), property, expectedValue,
                    this.projectDirectory.relativize(generatedPomPath))
                    .mitigation("Check the project-keeper user guide if you need a parent pom.").toString()).build());
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
            new PomFileIO().writePomFile(pom, this.pomFilePath);
        };
    }

    private List<ValidationFinding> validateGeneratedPomFile(final String groupId, final String artifactId,
            final String version, final Path generatedPomPath) {
        final String generatedContent = new PomFileGenerator().generatePomContent(this.enabledModules, groupId,
                artifactId, version);
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
