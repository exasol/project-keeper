package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.validators.FindingMatcher.hasFindingWithMessage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.*;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.validators.TestMavenModel;
import com.exasol.projectkeeper.validators.finding.FindingsFixer;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

//[utest->dsn~pom-file-validator~1]
class PomFileValidatorTest {
    @TempDir
    Path tempDir;

    @Test
    void testValidation() throws IOException {
        getTestModel().writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator(null);
        assertAll(
                () -> assertThat(result, hasFindingWithMessage("E-PK-CORE-103: Missing parent declaration in pom.xml")),
                () -> assertThat(result,
                        hasFindingWithMessage("E-PK-CORE-17: Missing required file: 'pk_generated_parent.pom'")),
                () -> assertThat(result, hasFindingWithMessage(
                        "E-PK-CORE-123: Invalid pom file pom.xml: Missing required property '/project/url'. The expected value is 'https://github.com/exasol/my-repo/'.")),
                () -> assertThat(result, hasFindingWithMessage(
                        "E-PK-CORE-105: Invalid pom file pom.xml: Missing required property finalName property in maven-assembly-plugin. Use the following template and set finalName:\n<plugin>\n"
                                + "    <artifactId>maven-assembly-plugin</artifactId>\n"
                                + "    <groupId>org.apache.maven.plugins</groupId>\n" + "    <configuration>\n"
                                + "        <finalName>NAME_OF_YOUR_JAR</finalName>\n" + "    </configuration>\n"
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
                "E-PK-CORE-120: Invalid pom file pom.xml: Missing required property /project/description. Please manually add a description."));
    }

    private TestMavenModel getTestModel() {
        return new TestMavenModel();
    }

    @Test
    void testValidAfterFix() throws IOException {
        final TestMavenModel pom = getTestModel();
        pom.configureAssemblyPluginFinalName();
        pom.writeAsPomToProject(this.tempDir);
        runFix(null);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, empty());
    }

    @Test
    void testWrongGroupId() throws IOException {
        final TestMavenModel pom = getTestModel();
        pom.setGroupId("com.other");
        pom.writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, hasFindingWithMessage(
                "E-PK-CORE-121: Invalid pom file pom.xml: Invalid groupId 'com.other'. Manually set the groupId to 'com.exasol'."));
    }

    @Test
    void testOutdatedUrl() throws IOException {
        final TestMavenModel pom = getTestModel();
        pom.setUrl("https://other.de");
        pom.writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, hasFindingWithMessage(
                "E-PK-CORE-122: Invalid pom file pom.xml: Invalid value 'https://other.de' for property '/project/url'. The expected value is 'https://github.com/exasol/my-repo/'."));
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

    private void runFix(final ProjectKeeperConfig.ParentPomRef parentPomRef) {
        new FindingsFixer(mock(Logger.class)).fixFindings(runValidator(parentPomRef));
    }

    @Test
    void testMissingVersion() throws IOException {
        final TestMavenModel testMavenModel = getTestModel();
        testMavenModel.setVersion(null);
        testMavenModel.writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, hasFindingWithMessage(
                "E-PK-CORE-111: Invalid pom file pom.xml: Missing required property /project/version. Please either set /project/version manually."));
    }

    @Test
    void testMissingVersionButParentPomRef() throws IOException, XmlPullParserException {
        final TestMavenModel testMavenModel = getTestModel();
        testMavenModel.setVersion(null);
        testMavenModel.writeAsPomToProject(this.tempDir);
        runFix(new ProjectKeeperConfig.ParentPomRef("com.example", "my-parent", "1.2.3", null));
        try (final FileReader reader = new FileReader(this.tempDir.resolve("pk_generated_parent.pom").toFile())) {
            final Model pom = new MavenXpp3Reader().read(reader);
            assertThat(pom.getVersion(), equalTo("1.2.3"));
        }
    }

    private Model readModel(final Path projectDir) throws XmlPullParserException, IOException {
        try (final FileReader fileReader = new FileReader(projectDir.toFile())) {
            return new MavenXpp3Reader().read(fileReader);
        }
    }

    @Test
    void testMissingGroupId() throws IOException {
        final TestMavenModel testMavenModel = getTestModel();
        testMavenModel.setGroupId(null);
        testMavenModel.writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, hasFindingWithMessage(
                "E-PK-CORE-102: Invalid pom file pom.xml: Missing required property 'groupId'. Please either set '/project/groupId' or '/project/parent/groupId'."));
    }

    private List<ValidationFinding> runValidator(final ProjectKeeperConfig.ParentPomRef parentPomRef) {
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
        final String expected = ("<parent>\n" + "        <artifactId>my-test-project-generated-parent</artifactId>\n"
                + "        <groupId>com.exasol</groupId>\n" + "        <version>0.1.0</version>\n"
                + "        <relativePath>pk_generated_parent.pom</relativePath>\n" + "    </parent>").replace("\n",
                        System.lineSeparator());
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
        assertThat(result, hasFindingWithMessage(
                "E-PK-CORE-104: Invalid pom file pom.xml: Invalid '/project/parent/artifactId'. Expected value is 'my-test-project-generated-parent'. The pom must declare pk_generated_parent.pom as parent pom. Check the project-keeper user guide if you need a parent pom."));
    }

    @Test
    void testEquivalentParentPath() throws IOException {
        final TestMavenModel model = getTestModel();
        model.configureAssemblyPluginFinalName();
        model.writeAsPomToProject(this.tempDir);
        runFix(null);
        final Path pom = this.tempDir.resolve("pom.xml");
        final String pomContent = Files.readString(pom);
        final String invalidPomContent = pomContent.replace("<relativePath>pk_generated_parent.pom</relativePath>",
                "<relativePath>./pk_generated_parent.pom</relativePath>");
        Files.writeString(pom, invalidPomContent);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, empty());
    }
}