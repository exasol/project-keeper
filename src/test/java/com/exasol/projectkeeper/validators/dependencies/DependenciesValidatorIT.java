package com.exasol.projectkeeper.validators.dependencies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.ProjectKeeperAbstractIT;
import com.exasol.projectkeeper.validators.TestMavenModel;

class DependenciesValidatorIT extends ProjectKeeperAbstractIT {
    @Test
    void testVerify() throws IOException, VerificationException {
        createExamplePomFile();
        final Verifier verifier = getVerifier();
        final VerificationException verificationException = assertThrows(VerificationException.class,
                () -> verifier.executeGoal("project-keeper:verify"));
        final String output = verificationException.getMessage();
        assertThat(output, containsString("E-PK-50: This project does not have a dependencies.md file."));
    }

    @Test
    void testWrongContent() throws IOException, VerificationException {
        createExamplePomFile();
        Files.writeString(this.projectDir.resolve("dependencies.md"), "wrong content");
        final Verifier verifier = getVerifier();
        final VerificationException verificationException = assertThrows(VerificationException.class,
                () -> verifier.executeGoal("project-keeper:verify"));
        final String output = verificationException.getMessage();
        assertThat(output, containsString(
                "[ERROR] E-PK-53: The dependencies.md file has a outdated content.\n" + "Expected content:\n"));
    }

    @Test
    void testFix() throws IOException, VerificationException {
        createExamplePomFile();
        final Verifier verifier = getVerifier();
        verifier.executeGoal("project-keeper:fix");
        final String dependenciesFileContent = Files.readString(this.projectDir.resolve("dependencies.md"));
        assertAll(//
                () -> assertThat(dependenciesFileContent, containsString("error-reporting-java")),
                () -> assertThat(dependenciesFileContent, containsString("Project keeper maven plugin"))//
        );
    }

    @Test
    void testBrokenLinkReplacing() throws IOException, VerificationException {
        final TestMavenModel pomModel = getTestMavenModelWithProjectKeeperPlugin();
        addLinkReplacementConfiguration(pomModel,
                "https://www.apache.org/licenses/LICENSE-2.0.txt|https://my-replacement.de");
        pomModel.writeAsPomToProject(this.projectDir);
        final Verifier verifier = getVerifier();
        verifier.executeGoal("project-keeper:fix");
        final String dependenciesFileContent = Files.readString(this.projectDir.resolve("dependencies.md"));
        assertThat(dependenciesFileContent, containsString("https://my-replacement.de"));
    }

    private void addLinkReplacementConfiguration(final TestMavenModel pomModel, final String linkReplacementValue) {
        final Plugin projectKeeperPlugin = pomModel.getBuild().getPlugins().stream()
                .filter(plugin -> plugin.getArtifactId().equals("project-keeper-maven-plugin")).findFirst()
                .orElseThrow();
        final Xpp3Dom configuration = (Xpp3Dom) projectKeeperPlugin.getConfiguration();
        final Xpp3Dom linkReplacements = new Xpp3Dom("linkReplacements");
        final Xpp3Dom linkReplacement = new Xpp3Dom("linkReplacement");
        linkReplacement.setValue(linkReplacementValue);
        linkReplacements.addChild(linkReplacement);
        configuration.addChild(linkReplacements);
    }

    private void createExamplePomFile() throws IOException {
        final TestMavenModel pomModel = getTestMavenModelWithProjectKeeperPlugin();
        pomModel.addDependency("error-reporting-java", "com.exasol", "compile", "0.1.0");
        pomModel.writeAsPomToProject(this.projectDir);
    }
}
