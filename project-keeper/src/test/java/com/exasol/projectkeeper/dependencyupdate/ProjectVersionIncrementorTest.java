package com.exasol.projectkeeper.dependencyupdate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.time.*;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.shared.config.*;
import com.exasol.projectkeeper.sources.analyze.generic.CommandExecutor;
import com.exasol.projectkeeper.sources.analyze.generic.ShellCommand;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileIO;

// [utest->dsn~dependency-updater.increment-version~1]
@ExtendWith(MockitoExtension.class)
class ProjectVersionIncrementorTest {

    private static final Instant NOW = Instant.parse("2007-12-03T10:15:30.00Z");
    private static final Path PROJECT_DIR = Path.of("ProjectDir");
    private static final Path POM_PATH = PROJECT_DIR.resolve("pom.xml");
    private static final String CURRENT_PROJECT_VERSION = "1.2.3";
    private static final Clock fixedClock = Clock.fixed(NOW, ZoneId.of("CET"));

    @Mock
    private Logger loggerMock;
    @Mock
    private ChangesFileIO changesFileIOMock;
    @Mock
    private XmlDocumentIO xmlDocumentIOMock;
    @Mock
    private CommandExecutor commandExecutorMock;

    @Mock
    Document pomModel;
    @Mock
    Node versionNode;

    @ParameterizedTest
    @CsvSource(nullValues = "NULL", value = { "NULL, false", "invalid data, false", "2007-??-??, false",
            "2024-??-??, false", "2007-01-01, true", "2007-12-02, true", "2007-12-03, false", "2007-12-04, false",
            "2024-02-08, false" })
    void isCurrentVersionReleased(final String releaseDate, final boolean expectedReleaseState) {
        final ChangesFile changesFile = ChangesFile.builder().releaseDate(releaseDate).build();
        when(changesFileIOMock.read(PROJECT_DIR.resolve("doc/changes/changes_1.2.3.md"))).thenReturn(changesFile);
        assertThat("release date '" + releaseDate + "' is considered as " + (expectedReleaseState ? "" : "not ")
                + "released", testee().isCurrentVersionReleased(), is(expectedReleaseState));
    }

    @Test
    void incrementProjectVersionFailsForInconsistentVersionInPom() {
        simulatePomVersion("1.2.2");

        final ProjectVersionIncrementor testee = testee(configWithoutJarArtifact());

        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                testee::incrementProjectVersion);
        assertThat(exception.getMessage(),
                startsWith("E-PK-CORE-174: Inconsistent project version '1.2.2' found in pom '" + POM_PATH
                        + "', expected '1.2.3'."));
    }

    private void simulatePomVersion(final String version) {
        when(xmlDocumentIOMock.read(POM_PATH)).thenReturn(pomModel);
        when(versionNode.getTextContent()).thenReturn(version);
        when(xmlDocumentIOMock.runXPath(same(pomModel), eq("/project/version"))).thenReturn(versionNode);
    }

    @ParameterizedTest
    @CsvSource({ "0.0.0, 0.0.1", "0.1.1, 0.1.2", "1.2.1, 1.2.2", "9.9.9, 9.9.10", "1.2.99, 1.2.100",
            "99.34.12345, 99.34.12346" })
    void incrementProjectVersion(final String currentVersion, final String expectedNextVersion) {
        simulatePomVersion(currentVersion);
        final String newVersion = testee(configWithoutJarArtifact(), currentVersion).incrementProjectVersion();
        assertThat(newVersion, equalTo(expectedNextVersion));
        verifyPomVersionUpdated(expectedNextVersion);
    }

    private void verifyPomVersionUpdated(final String expectedNextVersion) {
        verify(versionNode).setTextContent(expectedNextVersion);
        verify(xmlDocumentIOMock).write(same(pomModel), eq(POM_PATH));
    }

    @Test
    void incrementProjectVersionWithJarArtifactUpdatesReferences() {
        simulatePomVersion(CURRENT_PROJECT_VERSION);
        final String newVersion = testee(configWithJarArtifact(), CURRENT_PROJECT_VERSION).incrementProjectVersion();
        assertAll(() -> assertThat(newVersion, equalTo("1.2.4")), //
                () -> assertMavenExecuted());
        verifyPomVersionUpdated("1.2.4");
    }

    private void assertMavenExecuted(final String... mavenArguments) {
        final ShellCommand command = getExecutedCommand();
        assertThat(command.workingDir().get(), equalTo(PROJECT_DIR));
        assertThat(command.commandline(),
                contains(startsWith("mvn"), equalTo("--batch-mode"), equalTo("artifact-reference-checker:unify")));
    }

    private ShellCommand getExecutedCommand() {
        final ArgumentCaptor<ShellCommand> arg = ArgumentCaptor.forClass(ShellCommand.class);
        verify(commandExecutorMock).execute(arg.capture());
        return arg.getValue();
    }

    private ProjectVersionIncrementor testee() {
        return testee(null);
    }

    private ProjectVersionIncrementor testee(final ProjectKeeperConfig config) {
        return testee(config, CURRENT_PROJECT_VERSION);
    }

    private ProjectVersionIncrementor testee(final ProjectKeeperConfig config, final String currentProjectVersion) {
        return new ProjectVersionIncrementor(config, loggerMock, PROJECT_DIR, currentProjectVersion, changesFileIOMock,
                xmlDocumentIOMock, commandExecutorMock, fixedClock);
    }

    private ProjectKeeperConfig configWithJarArtifact() {
        return ProjectKeeperConfig.builder()
                .sources(List.of(Source.builder().modules(Set.of(ProjectKeeperModule.JAR_ARTIFACT)).build())).build();
    }

    private ProjectKeeperConfig configWithoutJarArtifact() {
        return ProjectKeeperConfig.builder().sources(List.of(Source.builder().modules(Set.of()).build())).build();
    }
}
