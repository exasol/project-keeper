package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.validators.FindingMatcher.hasFindingWithMessage;
import static com.exasol.projectkeeper.validators.FindingMatcher.hasFindingWithMessageMatchingRegex;
import static com.exasol.projectkeeper.validators.pom.XmlPattern.element;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.RepoInfo;
import com.exasol.projectkeeper.mavenrepo.Version;
import com.exasol.projectkeeper.shared.config.ParentPomRef;
import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.test.TestMavenModel;
import com.exasol.projectkeeper.validators.finding.FindingsFixer;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;
import com.exasol.projectkeeper.validators.pom.io.PomFileReader;
import com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper;

//[utest->dsn~pom-file-validator~1]
class PomFileValidatorTest {
    @TempDir
    Path tempDir;

    @Test
    void testValidation() throws IOException {
        getTestModel().writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator(null);
        assertAll(
                () -> assertThat(result,
                        hasFindingWithMessage("E-PK-CORE-103: Missing parent declaration in 'pom.xml'")),
                () -> assertThat(result,
                        hasFindingWithMessage("E-PK-CORE-17: Missing required file: 'pk_generated_parent.pom'")),
                () -> assertThat(result, hasFindingWithMessage(
                        "E-PK-CORE-123: Invalid pom file 'pom.xml': Missing required property '/project/url'. The expected value is 'https://github.com/exasol/my-repo/'.")),
                () -> assertThat(result, hasFindingWithMessage(
                        "E-PK-CORE-105: Invalid pom file 'pom.xml': Missing required property finalName property in maven-assembly-plugin. Use the following template and set finalName:\n<plugin>\n"
                                + "    <artifactId>maven-assembly-plugin</artifactId>\n"
                                + "    <groupId>org.apache.maven.plugins</groupId>\n" //
                                + "    <configuration>\n" //
                                + "        <finalName>NAME_OF_YOUR_JAR</finalName>\n" //
                                + "    </configuration>\n" //
                                + "</plugin>"))//
        );
    }

    @Test
    void testMissingDescription() throws IOException {
        final TestMavenModel model = getTestModel();
        model.setDescription(null);
        model.writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, hasFindingWithMessage(
                "E-PK-CORE-120: Invalid pom file 'pom.xml': Missing required property /project/description. Please manually add a description."));
    }

    @Test
    void testWrongGroupId() throws IOException {
        final TestMavenModel pom = getTestModel();
        pom.setGroupId("com.other");
        pom.writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, hasFindingWithMessage(
                "E-PK-CORE-121: Invalid pom file 'pom.xml': Invalid groupId 'com.other'. Manually set the groupId to 'com.exasol'."));
    }

    @Test
    void testOutdatedUrl() throws IOException {
        final TestMavenModel pom = getTestModel();
        pom.setUrl("https://other.de");
        pom.writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, hasFindingWithMessage(
                "E-PK-CORE-122: Invalid pom file 'pom.xml': Invalid value 'https://other.de' for property '/project/url'. The expected value is 'https://github.com/exasol/my-repo/'."));
    }

    @Test
    void testFixOutdatedUrl() throws IOException, XmlPullParserException {
        final TestMavenModel pom = getTestModel();
        pom.setUrl("https://other.de");
        pom.configureAssemblyPluginFinalName();
        pom.writeAsPomToProject(this.tempDir);
        runFix(null);
        assertThat(readModel(this.tempDir.resolve("pom.xml")).getUrl(), equalTo("https://github.com/exasol/my-repo/"));
    }

    @Test
    void testFixMissingUrl() throws IOException, XmlPullParserException {
        final TestMavenModel pom = getTestModel();
        pom.configureAssemblyPluginFinalName();
        pom.writeAsPomToProject(this.tempDir);
        runFix(null);
        assertThat(readModel(this.tempDir.resolve("pom.xml")).getUrl(), equalTo("https://github.com/exasol/my-repo/"));
    }

    private void runFix(final ParentPomRef parentPomRef) {
        new FindingsFixer(mock(Logger.class)).fixFindings(runValidator(parentPomRef));
    }

    @Test
    void testMissingVersion() {
        getTestModel().withVersion(null).writeAsPomToProject(this.tempDir);
        assertThat(runValidator(null),
                hasFindingWithMessageMatchingRegex("(?s)E-PK-CORE-111: Failed to detect project version.*"));
    }

    @Test
    void testMissingVersionButParentPomRef() {
        getTestModel().withVersion(null).withParentVersion("2.3.4").writeAsPomToProject(this.tempDir);
        runFix(new ParentPomRef("com.example", "my-parent", "1.2.3", null));
        final Model pom = readModel(this.tempDir.resolve("pk_generated_parent.pom"));
        assertThat(pom.getVersion(), equalTo("1.2.3"));
    }

    @Test
    void testMissingVersionButFromParent() {
        getTestModel().withVersion(null).withParentVersion("2.3.4").writeAsPomToProject(this.tempDir);
        assertThat(runValidator(null),
                not(hasFindingWithMessageMatchingRegex("(?s)E-PK-CORE-111: Failed to detect project version.*")));
    }

    private Model readModel(final Path file) {
        return new PomFileIO().readPom(file);
    }

    @Test
    void testMissingGroupId() throws IOException {
        final TestMavenModel testMavenModel = getTestModel();
        testMavenModel.setGroupId(null);
        testMavenModel.writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, hasFindingWithMessage("E-PK-CORE-102: Invalid pom file 'pom.xml':" //
                + " Missing required property 'groupId'." //
                + " Please either set /project/groupId or /project/parent/groupId."));
    }

    private List<ValidationFinding> runValidator(final ParentPomRef parentPomRef) {
        final PomFileValidator validator = new PomFileValidator(this.tempDir,
                List.of(ProjectKeeperModule.DEFAULT, ProjectKeeperModule.JAR_ARTIFACT), this.tempDir.resolve("pom.xml"),
                parentPomRef, new RepoInfo("my-repo", "My License"));
        return validator.validate();
    }

    @Test
    void testFixParentVersion() throws IOException, XmlPullParserException {
        final Parent parentWithWrongVersion = new Parent();
        parentWithWrongVersion.setArtifactId("my-test-project-generated-parent");
        parentWithWrongVersion.setGroupId(TestMavenModel.PROJECT_GROUP_ID);
        parentWithWrongVersion.setVersion("3.2.0");
        parentWithWrongVersion.setRelativePath("./pk_generated_parent.pom");
        final TestMavenModel model = getTestModel();
        model.setParent(parentWithWrongVersion);
        model.writeAsPomToProject(this.tempDir);
        runFix(null);
        assertThat(readModel(this.tempDir.resolve("pom.xml")).getParent().getVersion(),
                Matchers.equalTo(TestMavenModel.PROJECT_VERSION));
    }

    @Test
    void testAddParentFix() throws IOException {
        getTestModel().writeAsPomToProject(this.tempDir);
        runFix(null);
        final String expected = XmlPattern.element("parent").children( //
                element("artifactId", "my-test-project-generated-parent"), //
                element("groupId", "com.exasol"), //
                element("version", "0.1.0"), //
                element("relativePath", "pk_generated_parent.pom")) //
                .build();
        assertThat(Files.readString(this.tempDir.resolve("pom.xml")), Matchers.containsString(expected));
    }

    @Test
    void testGeneratedPomFileFix() throws IOException {
        getTestModel().writeAsPomToProject(this.tempDir);
        runFix(null);
        final String expected = ("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
                + "<!--@formatter:off--><!--This file is auto-generated by project-keeper. All changes will be overwritten.--><project")
                .replace("\n", System.lineSeparator());
        assertThat(Files.readString(this.tempDir.resolve("pk_generated_parent.pom")), Matchers.startsWith(expected));
    }

    @Test
    void testInvalidParentTag() throws IOException {
        getTestModel().writeAsPomToProject(this.tempDir);
        runFix(null);
        final Path pom = this.tempDir.resolve("pom.xml");
        final String pomContent = Files.readString(pom);
        final String invalidPomContent = pomContent.replace("<artifactId>my-test-project-generated-parent</artifactId>",
                "<artifactId>other</artifactId>");
        Files.writeString(pom, invalidPomContent);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, hasFindingWithMessage("E-PK-CORE-104: Invalid pom file 'pom.xml':" //
                + " Invalid /project/parent/artifactId." //
                + " Expected value is 'my-test-project-generated-parent'."
                + " The pom must declare 'pk_generated_parent.pom' as parent pom." //
                + " Check the project-keeper user guide if you need a parent pom."));
    }

    // [utest->dsn~verify-own-version~1]
    @Test
    void noReferenceToProjectKeeperPlugin() {
        getTestModel().writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, hasFindingWithMessageMatchingRegex(
                "W-PK-CORE-151: Pom file '.*pom.xml' contains no reference to project-keeper-maven-plugin."));
    }

    // [utest->dsn~verify-own-version~1]
    @Test
    void outdatedReferenceToProjectKeeperPlugin() {
        getTestModel().withProjectKeeperPlugin("0.0.1").writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, hasFindingWithMessageMatchingRegex("W-PK-CORE-153: Project-keeper version 0.0.1 is outdated."
                + " Please update project-keeper to latest version.*"));
    }

    // [utest->dsn~self-update~1]
    @Test
    void updateReferenceToProjectKeeperMavenPlugin() {
        getTestModel().withProjectKeeperPlugin("0.0.1").writeAsPomToProject(this.tempDir);
        runFix(null);
        final Document pom = PomFileReader.parse(this.tempDir.resolve("pom.xml"));
        final String version = XPathErrorHandlingWrapper //
                .runXPath(pom, PomFileValidator.XPath.PROJECT_KEEPER_VERSION) //
                .getTextContent();
        assertThat(version, not("0.0.1"));
        assertThat(version, matchesRegex(Version.PATTERN));
    }

    @Test
    void testEquivalentParentPath() throws IOException {
        getTestModel() //
                .withProjectKeeperPlugin("0.0.1") //
                .configureAssemblyPluginFinalName() //
                .writeAsPomToProject(this.tempDir);
        runFix(null);
        final Path pom = this.tempDir.resolve("pom.xml");
        final String pomContent = Files.readString(pom);
        final String invalidPomContent = pomContent.replace("<relativePath>pk_generated_parent.pom</relativePath>",
                "<relativePath>./pk_generated_parent.pom</relativePath>");
        Files.writeString(pom, invalidPomContent);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, empty());
    }

    private TestMavenModel getTestModel() {
        return new TestMavenModel();
    }

    @Test
    void testValidAfterFix() throws IOException {
        getTestModel() //
                .withProjectKeeperPlugin("0.0.1") //
                .configureAssemblyPluginFinalName() //
                .writeAsPomToProject(this.tempDir);
        runFix(null);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, empty());
    }

}
