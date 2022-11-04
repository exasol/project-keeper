package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.validators.files.RequiredFileValidator.withContentEqualTo;
import static com.exasol.projectkeeper.validators.pom.XmlHelper.addTextElement;
import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import org.w3c.dom.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.RepoInfo;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.config.ProjectKeeperConfigReader;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.validators.OwnVersionValidator;
import com.exasol.projectkeeper.validators.files.RequiredFileValidator;
import com.exasol.projectkeeper.validators.finding.*;
import com.exasol.projectkeeper.validators.pom.io.PomFileReader;
import com.exasol.projectkeeper.validators.pom.io.PomFileWriter;
import com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper;

/**
 * Validator for the pom.xml file.
 */
// [impl->dsn~pom-file-validator~1]
public class PomFileValidator implements Validator {

    private static final String PARENT_POM_MITIGATION = "Check the project-keeper user guide if you need a parent pom.";
    private static final String MUST_DECLARE_GENERATED_PARENT = " The pom must declare {{generated parent}} as parent pom.";

    @SuppressWarnings("java:S1075") // not customizable
    static class XPath {
        static final String PROJECT = "/project";
        /**
         * Xpath to find version of PK maven plugin in POM file.
         *
         * <p>
         * Used only internally and in tests.
         * </p>
         */
        static final String PROJECT_KEEPER_VERSION = "/project/build/plugins/plugin" //
                + "[groupId = 'com.exasol' and artifactId = 'project-keeper-maven-plugin'" //
                + "]/version";
        static final String VERSION = "/project/version";
        static final String ARTIFACT_ID = "/project/artifactId";
        static final String PARENT = "/project/parent";
        static final String PARENT_VERSION = "/project/parent/version";
        static final String GROUP_ID = "/project/groupId";
        static final String PARENT_GROUP_ID = "/project/parent/groupId";
        static final String PARENT_RELATIVE_PATH = "/project/parent/relativePath";
        static final String FINAL_NAME = "/project/build/plugins/plugin" //
                + "[artifactId/text() = 'maven-assembly-plugin']/configuration/finalName";
        static final String DESCRIPTION = "/project/description";

        private XPath() {
            // only static usage
        }
    }

    private final Path projectDirectory;
    final Collection<ProjectKeeperModule> enabledModules;
    private final Path pomFilePath;
    private final ProjectKeeperConfig.ParentPomRef parentPomRef;
    private final RepoInfo repoInfo;

    /**
     * Create a new instance of {@link PomFileValidator}.
     *
     * @param projectDirectory project directory
     * @param enabledModules   collection of enables modules
     * @param pomFilePath      pom file to create the runner for
     * @param parentPomRef     reference to a parent pom or {@code null}
     * @param repoInfo         information about the repository
     */
    public PomFileValidator(final Path projectDirectory, final Collection<ProjectKeeperModule> enabledModules,
            final Path pomFilePath, final ProjectKeeperConfig.ParentPomRef parentPomRef, final RepoInfo repoInfo) {
        this.projectDirectory = projectDirectory;
        this.enabledModules = enabledModules;
        this.pomFilePath = pomFilePath;
        this.parentPomRef = parentPomRef;
        this.repoInfo = repoInfo;
    }

    @Override
    public List<ValidationFinding> validate() {
        try {
            final Document pom = PomFileReader.parse(this.pomFilePath);
            final String version = getProjectVersion(pom);
            final String artifactId = getRequiredTextValue(pom, XPath.ARTIFACT_ID) + "-generated-parent";
            final String groupId = getGroupId(pom);
            final List<ValidationFinding> findings = new ArrayList<>();
            validateGroupId(groupId).ifPresent(findings::add);
            validateUrlTag(pom).ifPresent(findings::add);
            findings.addAll(validateOwnVersion(pom));
            final Path generatedPomPath = this.pomFilePath.getParent().resolve("pk_generated_parent.pom");
            findings.addAll(validateParentTag(pom, version, artifactId, groupId, generatedPomPath));
            findings.addAll(validateGeneratedPomFile(groupId, artifactId, version, generatedPomPath));
            validationDescriptionExists(pom).ifPresent(findings::add);
            if (this.enabledModules.contains(ProjectKeeperModule.JAR_ARTIFACT)) {
                findings.addAll(validateAssemblyPlugin(pom, this.projectDirectory.relativize(this.pomFilePath)));
            }
            return wrapFindingsWithGroupThatWritesPom(pom, findings);
        } catch (final InvalidPomException exception) {
            return List.of(SimpleValidationFinding.withMessage(exception.getMessage()).build());
        }
    }

    private List<ValidationFinding> validateOwnVersion(final Document pom) {
        final Node node = runXPath(pom, XPath.PROJECT_KEEPER_VERSION);
        if (node == null) {
            return List.of(SimpleValidationFinding.withMessage(ExaError.messageBuilder("W-PK-CORE-151") //
                    .message("Pom file {{file}} contains no reference to project-keeper-maven-plugin.",
                            this.pomFilePath) //
                    .toString()) //
                    .optional(true) //
                    .build());
        }

        final OwnVersionValidator.Updater updater = version -> (log -> node.setTextContent(version));
        return OwnVersionValidator.forMavenPlugin(node.getTextContent(), updater).validate();
    }

    private Optional<SimpleValidationFinding> validateUrlTag(final Document document) {
        final String expectedUrl = "https://github.com/exasol/" + this.repoInfo.getRepoName() + "/";
        return validateTagContent(document, XPath.PROJECT, "url", expectedUrl);
    }

    private Optional<SimpleValidationFinding> validateTagContent(final Document document, final String parentXPath,
            final String tagName, final String expectedValue) {
        final Node parent = runXPath(document, parentXPath);
        final Node node = runXPath(parent, tagName);
        if (node == null) {
            return Optional.of(SimpleValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-CORE-123")
                            .message("Invalid pom file {{file}}: Missing required property {{xpath}}.",
                                    this.projectDirectory.relativize(this.pomFilePath), parentXPath + "/" + tagName)
                            .mitigation("The expected value is {{expected value}}.", expectedValue).toString())
                    .andFix(log -> addTextElement(parent, tagName, expectedValue)).build());
        } else if (!node.getTextContent().equals(expectedValue)) {
            return Optional.of(SimpleValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-CORE-122")
                            .message(
                                    "Invalid pom file {{file}}: Invalid value {{actual value}} for property {{xpath}}.",
                                    this.projectDirectory.relativize(this.pomFilePath), node.getTextContent(),
                                    parentXPath + "/" + tagName)
                            .mitigation("The expected value is {{expected value}}.", expectedValue).toString())
                    .andFix(log -> node.setTextContent(expectedValue)).build());
        } else {
            return Optional.empty();
        }
    }

    private Optional<SimpleValidationFinding> validateGroupId(final String groupId) {
        if (!groupId.equals("com.exasol")) {
            return Optional.of(SimpleValidationFinding.withMessage(ExaError.messageBuilder("E-PK-CORE-121")
                    .message("Invalid pom file {{file}}: Invalid groupId {{groupId}}.",
                            this.projectDirectory.relativize(this.pomFilePath), groupId)
                    .mitigation("Manually set the groupId to 'com.exasol'.").toString()).build());
        } else {
            return Optional.empty();
        }
    }

    private Optional<ValidationFinding> validationDescriptionExists(final Document document) {
        if (runXPath(document, XPath.DESCRIPTION) == null) {
            return Optional.of(SimpleValidationFinding.withMessage(ExaError.messageBuilder("E-PK-CORE-120")
                    .message("Invalid pom file {{file}}: Missing required property {{property|u}}.",
                            this.projectDirectory.relativize(this.pomFilePath), XPath.DESCRIPTION)
                    .mitigation("Please manually add a description.").toString()).build());
        } else {
            return Optional.empty();
        }
    }

    private List<ValidationFinding> wrapFindingsWithGroupThatWritesPom(final Document pom,
            final List<ValidationFinding> findings) {
        if (hasFindingWithFix(findings)) {
            return List.of(new ValidationFindingGroup(findings, () -> PomFileWriter.writeFile(pom, this.pomFilePath)));
        } else {
            return findings;
        }
    }

    private boolean hasFindingWithFix(final List<ValidationFinding> findings) {
        return findings.stream().anyMatch(finding -> //
        (finding instanceof ValidationFindingGroup) || //
                ((finding instanceof SimpleValidationFinding) && ((SimpleValidationFinding) finding).hasFix()));
    }

    private String getProjectVersion(final Document pom) throws InvalidPomException {
        return Stream.of( //
                versionFrom(runXPath(pom, XPath.VERSION)), //
                versionFrom(this.parentPomRef), //
                versionFrom(runXPath(pom, XPath.PARENT_VERSION)))//
                .filter(Objects::nonNull) //
                .findFirst() //
                .orElseThrow(() -> new InvalidPomException(ExaError.messageBuilder("E-PK-CORE-111")
                        .message("Failed to detect project version.")
                        .mitigation("Please either set {{xpath1|u}} or {{xpath2}} in file {{pom file|u}}",
                                XPath.VERSION, XPath.PARENT_VERSION, this.projectDirectory.relativize(this.pomFilePath))
                        .mitigation("or add version to file {{configuration file|u}}.",
                                ProjectKeeperConfigReader.CONFIG_FILE_NAME)
                        .toString()));
    }

    private String versionFrom(final Node node) {
        return node == null ? null : node.getTextContent();
    }

    private String versionFrom(final ProjectKeeperConfig.ParentPomRef ref) {
        return ref == null ? null : ref.getVersion();
    }

    private List<ValidationFinding> validateAssemblyPlugin(final Node pom, final Path relativePomPath) {
        final Node finalNameProperty = runXPath(pom, XPath.FINAL_NAME);
        if ((finalNameProperty == null) || finalNameProperty.getTextContent().isBlank()) {
            return List.of(SimpleValidationFinding.withMessage(ExaError.messageBuilder("E-PK-CORE-105").message(
                    "Invalid pom file {{file}}: Missing required property finalName property in maven-assembly-plugin.",
                    relativePomPath).mitigation(
                            "Use the following template and set finalName:\n" //
                                    + "<plugin>\n" //
                                    + "    <artifactId>maven-assembly-plugin</artifactId>\n"
                                    + "    <groupId>org.apache.maven.plugins</groupId>\n" //
                                    + "    <configuration>\n" //
                                    + "        <finalName>NAME_OF_YOUR_JAR</finalName>\n" //
                                    + "    </configuration>\n" //
                                    + "</plugin>")
                    .toString()).build());
        } else {
            return Collections.emptyList();
        }
    }

    private List<ValidationFinding> validateParentTag(final Document pom, final String version, final String artifactId,
            final String groupId, final Path generatedPomPath) {
        final Node parentTag = runXPath(pom, XPath.PARENT);
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
        if ((node == null) || !version.equals(node.getTextContent())) {
            final SimpleValidationFinding.Builder findingBuilder = SimpleValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-CORE-118")
                            .message("Invalid pom file {{file}}: Invalid {{parent version xpath|u}}." //
                                    + " Expected value is {{expected}}." //
                                    + MUST_DECLARE_GENERATED_PARENT, //
                                    this.projectDirectory.relativize(this.pomFilePath), //
                                    XPath.PARENT_VERSION, //
                                    version, //
                                    this.projectDirectory.relativize(generatedPomPath))
                            .mitigation(PARENT_POM_MITIGATION).toString());
            if (!hasOtherFindings && (node != null)) {
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
        if ((node == null) || !comparePaths(expectedValue, Path.of(node.getTextContent()))) {
            return Optional.of(SimpleValidationFinding.withMessage(ExaError.messageBuilder("E-PK-CORE-112").message(
                    "Invalid pom file {{file}}: Invalid {{parent relative path|u}}. Expected value is {{expected}}."
                            + MUST_DECLARE_GENERATED_PARENT, //
                    this.projectDirectory.relativize(this.pomFilePath), //
                    XPath.PARENT_RELATIVE_PATH, //
                    expectedValue, //
                    this.projectDirectory.relativize(generatedPomPath)) //
                    .mitigation(PARENT_POM_MITIGATION).toString()).build());
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
        if ((node == null) || !node.getTextContent().equals(expectedValue)) {
            return Optional.of(SimpleValidationFinding.withMessage(ExaError.messageBuilder("E-PK-CORE-104")
                    .message("Invalid pom file {{file}}: Invalid {{xpath|u}}." //
                            + " Expected value is {{expected}}." //
                            + MUST_DECLARE_GENERATED_PARENT, //
                            this.projectDirectory.relativize(this.pomFilePath), //
                            XPath.PARENT + "/" + property, //
                            expectedValue, //
                            this.projectDirectory.relativize(generatedPomPath))
                    .mitigation(PARENT_POM_MITIGATION).toString()).build());
        } else {
            return Optional.empty();
        }
    }

    private SimpleValidationFinding.Fix getAddParentToPomFileFix(final Document pom, final String version,
            final String artifactId, final String groupId, final Path generatedPomPath) {
        return log -> {
            final Node project = runXPath(pom, XPath.PROJECT);
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
                artifactId, version, this.parentPomRef, this.repoInfo);
        return new RequiredFileValidator().validateFile(this.projectDirectory, generatedPomPath,
                withContentEqualTo(generatedContent));
    }

    private String getGroupId(final Document pom) throws InvalidPomException {
        final Node node = runXPath(pom, XPath.GROUP_ID);
        if (node != null) {
            return node.getTextContent();
        } else {
            final Node parentGroupIdNode = runXPath(pom, XPath.PARENT_GROUP_ID);
            if (parentGroupIdNode != null) {
                return parentGroupIdNode.getTextContent();
            } else {
                throw new InvalidPomException(ExaError.messageBuilder("E-PK-CORE-102")
                        .message("Invalid pom file {{file}}: Missing required property 'groupId'.",
                                this.projectDirectory.relativize(this.pomFilePath))
                        .mitigation("Please either set {{groupId|u}} or {{parent groupId|u}}.", XPath.GROUP_ID,
                                XPath.PARENT_GROUP_ID)
                        .toString());
            }
        }
    }

    private String getRequiredTextValue(final Node pom, final String xPath) throws InvalidPomException {
        final Node node = XPathErrorHandlingWrapper.runXPath(pom, xPath);
        if (node == null) {
            throw new InvalidPomException(ExaError.messageBuilder("E-PK-CORE-101")
                    .message("Invalid pom file {{file}}: Missing required property {{property|u}}.",
                            this.projectDirectory.relativize(this.pomFilePath), xPath)
                    .mitigation("Please set the property manually.").toString());
        }
        return node.getTextContent();
    }

    private static class InvalidPomException extends Exception {
        private static final long serialVersionUID = 6050603678193596784L;

        public InvalidPomException(final String message) {
            super(message);
        }
    }

}
