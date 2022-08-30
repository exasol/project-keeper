package com.exasol.projectkeeper.sources.analyze.golang;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesRegex;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.File;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GoBinaryTest {

    private static final String SEP = File.separator.replace("\\", "\\\\");
    @Mock
    private final GoProcess goProcess = mock(GoProcess.class);

    @Test
    void nonExistingBinary() {
        assertThat(testee().isInstalled(), is(false));
    }

    @Test
    void goLicenses() {
        final GoBinary testee = GoBinary.GO_LICENSES;
        final String path = testee.path().toString();
        assertThat(path, Matchers.endsWith(testee.nameWithSuffix()));
        assertThat(path, matchesRegex(".*" + SEP + "bin" + SEP + "go-licenses.*"));
    }

    @Test
    void go_IsInstalled() {
        assertThat(GoBinary.GO.isInstalled(), is(true));
    }

    @Test
    void installFailure() {
        when(this.goProcess.start(any(), any(), any())).thenThrow(new IllegalStateException("bla bla"));
        final GoBinary testee = testee();
        final Exception e = assertThrows(IllegalStateException.class, () -> testee.install());
        assertThat(e.getMessage(), Matchers.startsWith("E-PK-CORE-161: Error installing go binary"));
        assertThat(testee.isInstalled(), is(false));
    }

    @Test
    void installSuccess() {
        final SimpleProcess process = mock(SimpleProcess.class);
        when(this.goProcess.start(any(), any(), any())).thenReturn(process);
        testee().install();
        verify(process).waitUntilFinished(any());
    }

    @Test
    @Tag("integration")
    void installSuccessIT() {
        final GoBinary testee = GoBinary.GO_LICENSES.install();
        assertThat(testee.isInstalled(), is(true));
    }

    private GoBinary testee() {
        return new GoBinary(this.goProcess, "non-existing-go-binary", null);
    }

}
