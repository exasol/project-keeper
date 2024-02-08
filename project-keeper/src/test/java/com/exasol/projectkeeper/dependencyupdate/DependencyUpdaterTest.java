package com.exasol.projectkeeper.dependencyupdate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.ProjectKeeper;
import com.exasol.projectkeeper.sources.analyze.generic.CommandExecutor;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileIO;

@ExtendWith(MockitoExtension.class)
class DependencyUpdaterTest {

    private static final Path PROJECT_DIR = Path.of("ProjectDir");
    private static final String CURRENT_PROJECT_VERSION = "1.2.3";
    @Mock
    private ProjectKeeper projectKeeperMock;
    @Mock
    private Logger loggerMock;
    @Mock
    private ProjectVersionIncrementor projectVersionIncrementorMock;
    @Mock
    private ChangesFileIO changesFileIOMock;
    @Mock
    private ChangesFileUpdater changesFileUpdaterMock;
    @Mock
    private CommandExecutor commandExecutorMock;

    @Test
    void updateDependencies() {
        // assertUpdate();
    }

    private DependencyUpdater testee() {
        return new DependencyUpdater(projectKeeperMock, loggerMock, PROJECT_DIR, CURRENT_PROJECT_VERSION,
                projectVersionIncrementorMock, changesFileIOMock, changesFileUpdaterMock, commandExecutorMock);
    }

    private void assertUpdate() {
        assertThat(testee().updateDependencies(), is(true));
    }
}
