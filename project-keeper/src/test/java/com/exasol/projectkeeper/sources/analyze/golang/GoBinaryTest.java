package com.exasol.projectkeeper.sources.analyze.golang;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.sources.analyze.generic.CommandExecutor;

@ExtendWith(MockitoExtension.class)
class GoBinaryTest {

    private static final String SEP = File.separator.replace("\\", "\\\\");
    @Mock
    private CommandExecutor executor;

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
    void goItself() {
        assertThat(GoBinary.GO.isInstalled(), is(true));
        assertThat(GoBinary.GO.command(), equalTo("go"));
    }

    @Test
    void installFailure() {
        when(this.executor.execute(any(), any())).thenThrow(new IllegalStateException("bla bla"));
        final GoBinary testee = testee();
        final Exception e = assertThrows(IllegalStateException.class, () -> testee.install());
        assertThat(e.getMessage(), Matchers.startsWith("E-PK-CORE-161: Error installing go binary"));
        assertThat(testee.isInstalled(), is(false));
    }

    @Test
    void installSuccess() {
        testee().install();
        verify(this.executor).execute(any(), any());
    }

    @Test
    @Tag("integration")
    void installSuccessIT() {
        final GoBinary testee = GoBinary.GO_LICENSES.install();
        assertThat(testee.isInstalled(), is(true));
    }

    private GoBinary testee() {
        return new GoBinary(this.executor, null, "non-existing-go-binary");
    }
}
