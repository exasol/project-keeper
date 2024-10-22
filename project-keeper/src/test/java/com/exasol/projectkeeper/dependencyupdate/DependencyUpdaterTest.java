package com.exasol.projectkeeper.dependencyupdate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.nio.file.Path;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.*;
import com.exasol.projectkeeper.sources.analyze.generic.CommandExecutor;
import com.exasol.projectkeeper.sources.analyze.generic.ShellCommand;

@ExtendWith(MockitoExtension.class)
class DependencyUpdaterTest {

    private static final Path PROJECT_DIR = Path.of("ProjectDir");
    private static final String CURRENT_PROJECT_VERSION = "1.2.3";
    private static final String NEXT_PROJECT_VERSION = "1.2.4";
    @Mock
    private ProjectKeeper projectKeeperMock;
    @Mock
    private ProjectVersionIncrementor projectVersionIncrementorMock;
    @Mock
    private ChangesFileUpdater changesFileUpdaterMock;
    @Mock
    private CommandExecutor commandExecutorMock;

    @Test
    void updateDependenciesIncrementsVersionIfRequired() {
        when(projectKeeperMock.fix()).thenReturn(true);
        when(projectVersionIncrementorMock.isCurrentVersionReleased()).thenReturn(true);
        when(projectVersionIncrementorMock.incrementProjectVersion()).thenReturn(NEXT_PROJECT_VERSION);
        assertUpdate();
        verify(projectVersionIncrementorMock).incrementProjectVersion();
    }

    @Test
    void updateDependenciesDoesNotIncrementsVersionIfNotRequired() {
        when(projectKeeperMock.fix()).thenReturn(true);
        when(projectVersionIncrementorMock.isCurrentVersionReleased()).thenReturn(false);
        assertUpdate();
        verify(projectVersionIncrementorMock, never()).incrementProjectVersion();
    }

    @Test
    void updateDependenciesPKFixFails() {
        when(projectKeeperMock.fix()).thenReturn(false);
        final DependencyUpdater testee = testee();
        final IllegalStateException exception = assertThrows(IllegalStateException.class, testee::updateDependencies);
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-177: Running project-keeper fix failed, see errors above. Fix findings and try again."));
    }

    @Test
    void updateDependenciesPKFixSucceeds() {
        when(projectKeeperMock.fix()).thenReturn(true);
        assertUpdate();
        verify(projectKeeperMock).fix();
    }

    // [utest->dsn~dependency-updater.update-dependencies~1]
    @Test
    void updateDependenciesRunsVersionsPlugin() {
        when(projectKeeperMock.fix()).thenReturn(true);
        assertUpdate();
        final List<ShellCommand> commands = verifyCommandExecuted();
        final ShellCommand useLatestVersion = commands.get(0);
        final ShellCommand updateProperties = commands.get(1);
        assertThat(useLatestVersion.workingDir().get(), equalTo(PROJECT_DIR));
        assertThat(updateProperties.workingDir().get(), equalTo(PROJECT_DIR));
        final String mavenExecutable = "mvn" + OsCheck.suffix(".cmd");
        assertThat(useLatestVersion.commandLine(),
                Matchers.contains(mavenExecutable, "--batch-mode", "versions:use-latest-releases"));
        assertThat(updateProperties.commandLine(),
                Matchers.contains(mavenExecutable, "--batch-mode", "versions:update-properties"));
    }

    @Test
    void updateDependenciesUpdatesChangesFile() {
        when(projectKeeperMock.fix()).thenReturn(true);
        assertUpdate();
        verify(changesFileUpdaterMock).updateChanges(CURRENT_PROJECT_VERSION);
    }

    private List<ShellCommand> verifyCommandExecuted() {
        final ArgumentCaptor<ShellCommand> arg = ArgumentCaptor.forClass(ShellCommand.class);
        verify(commandExecutorMock, times(2)).execute(arg.capture());
        assertThat(arg.getAllValues(), hasSize(2));
        return arg.getAllValues();
    }

    private DependencyUpdater testee() {
        return new DependencyUpdater(projectKeeperMock, new PrintLogger(), PROJECT_DIR, CURRENT_PROJECT_VERSION,
                projectVersionIncrementorMock, changesFileUpdaterMock, commandExecutorMock);
    }

    private void assertUpdate() {
        assertThat(testee().updateDependencies(), is(true));
    }

    static class PrintLogger implements Logger {

        @Override
        public void info(final String message) {
            System.out.println(message);
        }

        @Override
        public void warn(final String message) {
            System.out.println(message);
        }

        @Override
        public void error(final String message) {
            System.out.println(message);
        }

    }
}
