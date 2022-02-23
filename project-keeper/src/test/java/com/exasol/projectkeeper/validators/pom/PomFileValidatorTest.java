package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.validators.FindingMatcher.hasFindingWithMessage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.ProjectKeeperModule;
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
        final List<ValidationFinding> result = runValidator();
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
        runFix();
        final List<ValidationFinding> result = runValidator();
        assertThat(result, empty());
    }

    private void runFix() {
        new FindingsFixer(mock(Logger.class)).fixFindings(runValidator());
    }

    @Test
    void testMissingVersion() throws IOException {
        final TestMavenModel testMavenModel = new TestMavenModel();
        testMavenModel.setVersion(null);
        testMavenModel.writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator();
        assertThat(result, hasFindingWithMessage(
                "E-PK-CORE-101: Invalid pom file pom.xml: Missing required property '/project/version'. Please set the property manually."));
    }

    @Test
    void testMissingGroupId() throws IOException {
        final TestMavenModel testMavenModel = new TestMavenModel();
        testMavenModel.setGroupId(null);
        testMavenModel.writeAsPomToProject(this.tempDir);
        final List<ValidationFinding> result = runValidator();
        assertThat(result, hasFindingWithMessage(
                "E-PK-CORE-102: Invalid pom file pom.xml: Missing required property 'groupId'. Please either set '/project/groupId' or '/project/parent/groupId'."));
    }

    private List<ValidationFinding> runValidator() {
        final PomFileValidator validator = new PomFileValidator(this.tempDir, List.of(ProjectKeeperModule.DEFAULT),
                this.tempDir.resolve("pom.xml"), null);
        return validator.validate();
    }

    @Test
    void testAddParentFix() throws IOException {
        new TestMavenModel().writeAsPomToProject(this.tempDir);
        runFix();
        final String expected = ("<parent>\n" + "        <artifactId>my-test-project-generated-parent</artifactId>\n"
                + "        <groupId>com.example</groupId>\n" + "        <version>0.1.0</version>\n"
                + "        <relativePath>pk_generated_parent.pom</relativePath>\n" + "    </parent>").replace("\n",
                        System.lineSeparator());
        assertThat(Files.readString(this.tempDir.resolve("pom.xml")), Matchers.containsString(expected));
    }

    @Test
    void testGeneratedPomFileFix() throws IOException {
        new TestMavenModel().writeAsPomToProject(this.tempDir);
        runFix();
        final String expected = ("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
                + "<!--This file is auto-generated by project-keeper. All changes will be overwritten.--><project")
                        .replace("\n", System.lineSeparator());
        assertThat(Files.readString(this.tempDir.resolve("pk_generated_parent.pom")), Matchers.startsWith(expected));
    }

    @Test
    void testInvalidParentTag() throws IOException {
        new TestMavenModel().writeAsPomToProject(this.tempDir);
        runFix();
        final Path pom = this.tempDir.resolve("pom.xml");
        final String pomContent = Files.readString(pom);
        final String invalidPomContent = pomContent.replace("<artifactId>my-test-project-generated-parent</artifactId>",
                "<artifactId>other</artifactId>");
        Files.writeString(pom, invalidPomContent);
        final List<ValidationFinding> result = runValidator();
        assertThat(result, hasFindingWithMessage(
                "E-PK-CORE-104: Invalid pom file pom.xml: Invalid '/project/parent/artifactId'. Expected value is 'my-test-project-generated-parent'. The pom must declare pk_generated_parent.pom as parent pom. Check the project-keeper user guide if you need a parent pom."));
    }
}