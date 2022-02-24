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
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.ProjectKeeperModule;
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
        new TestMavenModel().writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator(null);
        assertAll(
                () -> assertThat(result, hasFindingWithMessage("E-PK-CORE-103: Missing parent declaration in pom.xml")),
                () -> assertThat(result,
                        hasFindingWithMessage("E-PK-CORE-17: Missing required file: 'pk_generated_parent.pom'")),
                () -> assertThat(result, hasFindingWithMessage(
                        "E-PK-CORE-105: Invalid pom file pom.xml: Missing required property finalName property in maven-assembly-plugin. Use the following template and set finalName:\n<plugin>\n"
                                + "    <artifactId>maven-assembly-plugin</artifactId>\n"
                                + "    <groupId>org.apache.maven.plugins</groupId>\n" + "    <configuration>\n"
                                + "        <finalName>NAME_OF_YOUR_JAR</finalName>\n" + "    </configuration>\n"
                                + "</plugin>"))//
        );
    }

    @Test
    void testValidAfterFix() throws IOException {
        final TestMavenModel pom = new TestMavenModel();
        pom.configureAssemblyPluginFinalName();
        pom.writeAsPomToProject(this.tempDir);
        runFix(null);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, empty());
    }

    private void runFix(final ProjectKeeperConfig.ParentPomRef parentPomRef) {
        new FindingsFixer(mock(Logger.class)).fixFindings(runValidator(parentPomRef));
    }

    @Test
    void testMissingVersion() throws IOException {
        final TestMavenModel testMavenModel = new TestMavenModel();
        testMavenModel.setVersion(null);
        testMavenModel.writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, hasFindingWithMessage(
                "E-PK-CORE-111: Invalid pom file pom.xml: Missing required property /project/version. Please either set /project/version manually."));
    }

    @Test
    void testMissingVersionButParentPomRef() throws IOException, XmlPullParserException {
        final TestMavenModel testMavenModel = new TestMavenModel();
        testMavenModel.setVersion(null);
        testMavenModel.writeAsPomToProject(this.tempDir);
        runFix(new ProjectKeeperConfig.ParentPomRef("com.example", "my-parent", "1.2.3", null));
        try (final FileReader reader = new FileReader(this.tempDir.resolve("pk_generated_parent.pom").toFile())) {
            final Model pom = new MavenXpp3Reader().read(reader);
            assertThat(pom.getVersion(), equalTo("1.2.3"));
        }
    }

    @Test
    void testMissingGroupId() throws IOException {
        final TestMavenModel testMavenModel = new TestMavenModel();
        testMavenModel.setGroupId(null);
        testMavenModel.writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator(null);
        assertThat(result, hasFindingWithMessage(
                "E-PK-CORE-102: Invalid pom file pom.xml: Missing required property 'groupId'. Please either set '/project/groupId' or '/project/parent/groupId'."));
    }

    private List<ValidationFinding> runValidator(final ProjectKeeperConfig.ParentPomRef parentPomRef) {
        final PomFileValidator validator = new PomFileValidator(this.tempDir,
                List.of(ProjectKeeperModule.DEFAULT, ProjectKeeperModule.JAR_ARTIFACT), this.tempDir.resolve("pom.xml"),
                parentPomRef);
        return validator.validate();
    }

    @Test
    void testAddParentFix() throws IOException {
        new TestMavenModel().writeAsPomToProject(this.tempDir);
        runFix(null);
        final String expected = ("<parent>\n" + "        <artifactId>my-test-project-generated-parent</artifactId>\n"
                + "        <groupId>com.example</groupId>\n" + "        <version>0.1.0</version>\n"
                + "        <relativePath>pk_generated_parent.pom</relativePath>\n" + "    </parent>").replace("\n",
                        System.lineSeparator());
        assertThat(Files.readString(this.tempDir.resolve("pom.xml")), Matchers.containsString(expected));
    }

    @Test
    void testGeneratedPomFileFix() throws IOException {
        new TestMavenModel().writeAsPomToProject(this.tempDir);
        runFix(null);
        final String expected = ("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
                + "<!--This file is auto-generated by project-keeper. All changes will be overwritten.--><project")
                        .replace("\n", System.lineSeparator());
        assertThat(Files.readString(this.tempDir.resolve("pk_generated_parent.pom")), Matchers.startsWith(expected));
    }

    @Test
    void testInvalidParentTag() throws IOException {
        new TestMavenModel().writeAsPomToProject(this.tempDir);
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
        final TestMavenModel model = new TestMavenModel();
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