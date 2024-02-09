package com.exasol.projectkeeper.dependencyupdate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileIO;

@ExtendWith(MockitoExtension.class)
class ChangesFileUpdaterTest {

    private static final String VERSION = "1.2.3";
    private static final Path PROJECT_DIR = Path.of("projectDir");
    private static final Path CHANGES_FILE = PROJECT_DIR.resolve("doc/changes/").resolve("changes_" + VERSION + ".md");
    @Mock
    VulnerabilityInfoProvider vulnerabilityInfoProviderMock;
    @Mock
    ChangesFileIO changesFileIOMock;

    @Test
    void updateChangesFileWritesChangesFile() {
        assertChangesUpdated(ChangesFile.builder().build());
    }

    private ChangesFile assertChangesUpdated(final ChangesFile inputChangesFile) {
        when(changesFileIOMock.read(CHANGES_FILE)).thenReturn(inputChangesFile);
        testee().updateChanges(VERSION);
        final ChangesFile updatedChangesFile = verifyChangesFileWritten();
        assertAll(() -> assertThat(updatedChangesFile, not(nullValue())),
                () -> assertThat(updatedChangesFile, not(sameInstance(inputChangesFile))));
        return updatedChangesFile;
    }

    private ChangesFile verifyChangesFileWritten() {
        final ArgumentCaptor<ChangesFile> arg = ArgumentCaptor.forClass(ChangesFile.class);
        verify(changesFileIOMock).write(arg.capture(), eq(CHANGES_FILE));
        return arg.getValue();
    }

    private ChangesFileUpdater testee() {
        return new ChangesFileUpdater(vulnerabilityInfoProviderMock, changesFileIOMock, PROJECT_DIR);
    }
}
