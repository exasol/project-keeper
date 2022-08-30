package com.exasol.projectkeeper.sources.analyze.golang;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.nio.file.Paths;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class GoBinaryTest {

    private static final String SEP = File.separator.replace("\\", "\\\\");
    private static final GoBinary TESTEE = new GoBinary(null, "non-existing-go-binary", null);

    @Test
    void nonExistingBinary() {
        assertThat(TESTEE.isInstalled(), is(false));
    }

    @Test
    void goLicenses() {
        final GoBinary testee = GoBinary.GO_LICENSES;
        final String path = testee.path().toString();
        assertThat(path, Matchers.endsWith(testee.nameWithSuffix()));
        assertThat(path, matchesRegex(".*" + SEP + "bin" + SEP + "go-licenses.*"));
        assertThat(testee.installSource(), equalTo("github.com/google/go-licenses@latest"));
    }

    @Test
    void go_IsInstalled() {
        assertThat(GoBinary.GO.isInstalled(), is(true));
    }

    @Test
    void installFailure() {
        final GoProcess goProcess = mock(GoProcess.class);
        when(goProcess.start(any(), any(), any())).thenThrow(new IllegalStateException("bla bla"));
        final Exception e = assertThrows(IllegalStateException.class, () -> TESTEE.install(null, goProcess));
        assertThat(e.getMessage(), Matchers.startsWith("E-PK-CORE-161: Error installing go binary"));
        assertThat(TESTEE.isInstalled(), is(false));
    }

    @Test
    void installSuccess() {
        final GoProcess goProcess = mock(GoProcess.class);
        final SimpleProcess process = mock(SimpleProcess.class);
        when(goProcess.start(any(), any(), any())).thenReturn(process);
        TESTEE.install(null, goProcess);
        verify(process).waitUntilFinished(any());
    }

    @Test
    @Tag("integration")
    void installSuccessIT() {
        final GoBinary testee = GoBinary.GO_LICENSES.install(Paths.get("").toAbsolutePath(), new GoProcess());
        assertThat(testee.isInstalled(), is(true));
    }
}
