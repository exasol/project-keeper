package com.exasol.projectkeeper.sources.analyze.npm;

import static com.exasol.projectkeeper.sources.analyze.npm.NpmServices.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.repository.GitRepository;
import com.exasol.projectkeeper.shared.repository.TaggedCommit;
import com.exasol.projectkeeper.sources.analyze.generic.CommandExecutor;
import com.exasol.projectkeeper.sources.analyze.generic.GitService;

@ExtendWith(MockitoExtension.class)
class NpmServicesTest {

    @Mock
    CommandExecutor executor;
    @Mock
    GitService git;
    @Mock
    TaggedCommit previousTag;

    private static final Path PROJECT_DIR = Path.of("/projects/sample-project");
    private static final Path PACKAGE_JSON_FILE = Path.of("some/file.txt");

    @Test
    void getDependencies() {
        when(this.executor.execute(eq(FETCH_DEPENDENCIES), any())).thenReturn("");
        when(this.executor.execute(eq(LIST_DEPENDENCIES), any())).thenReturn(TestData.DEPENDENCIES);
        when(this.executor.execute(eq(LICENSE_CHECKER), any())).thenReturn(TestData.LICENSES);
        final PackageJson current = TestData.samplePackageJson();
        assertThat(testee().getDependencies(current), Matchers.isA(ProjectDependencies.class));
    }

    @Test
    void retrievePrevious() throws FileNotFoundException {
        final GitRepository repo = mock(GitRepository.class);
        when(repo.getFileFromCommit(any(), any())).thenReturn(TestData.PREVIOUS);
        final PackageJson current = currentPackageJson(PACKAGE_JSON_FILE);
        final PackageJson previous = retrievePrevious(repo, current);
        assertThat(previous, notNullValue());
        assertThat(previous.getVersion(), equalTo("1.0.0"));
        assertThat(previous.getModuleName(), equalTo(current.getModuleName()));
    }

    @Test
    void previousNotFound() throws FileNotFoundException {
        final GitRepository repo = mock(GitRepository.class);
        when(repo.getFileFromCommit(any(), any())).thenThrow(new FileNotFoundException());
        final PackageJson current = currentPackageJson(PACKAGE_JSON_FILE);
        assertThat(retrievePrevious(repo, current), nullValue());
    }

    @Test
    void dependenciesFetchedOnlyOnceForWorkingDir() {
        when(this.executor.execute(eq(FETCH_DEPENDENCIES), any())).thenReturn("");
        when(this.executor.execute(eq(LIST_DEPENDENCIES), any())).thenReturn(TestData.DEPENDENCIES);
        final NpmServices testee = testee();
        final Path workingDir = Path.of("testing-working-dir");
        testee.listDependencies(workingDir);
        testee.listDependencies(workingDir);
        verify(this.executor, times(1)).execute(FETCH_DEPENDENCIES, workingDir);
    }

    @Test
    void dependenciesFetchedForEachWorkingDir() {
        when(this.executor.execute(eq(FETCH_DEPENDENCIES), any())).thenReturn("");
        when(this.executor.execute(eq(LIST_DEPENDENCIES), any())).thenReturn(TestData.DEPENDENCIES);
        final NpmServices testee = testee();
        final Path workingDir1 = Path.of("testing-working-dir1");
        final Path workingDir2 = Path.of("testing-working-dir2");
        testee.listDependencies(workingDir1);
        testee.listDependencies(workingDir2);
        verify(this.executor, times(1)).execute(FETCH_DEPENDENCIES, workingDir1);
        verify(this.executor, times(1)).execute(FETCH_DEPENDENCIES, workingDir2);
    }

    @Test
    void fetchDependenciesSucceeds() {
        when(this.executor.execute(eq(FETCH_DEPENDENCIES), any())).thenReturn("");
        assertDoesNotThrow(() -> testee().fetchDependencies(Path.of("testing-working-dir1")));
    }

    @Test
    void fetchDependenciesFailsWithIllegalStateException() {
        when(this.executor.execute(eq(FETCH_DEPENDENCIES), any())).thenThrow(IllegalStateException.class);
        final NpmServices testee = testee();
        final Path workingDir = Path.of("testing-working-dir1");
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> testee.fetchDependencies(workingDir));
        assertThat(exception.getMessage(), equalTo("E-PK-CORE-168: Installing dependencies in 'testing-working-dir1' via 'npm ci' failed. Try running 'npm ci' manually in directory 'testing-working-dir1'."));
    }

    private PackageJson currentPackageJson(final Path relative) {
        return TestData.packageJson(PROJECT_DIR.resolve(relative), TestData.CURRENT);
    }

    private PackageJson retrievePrevious(final GitRepository repo, final PackageJson current)
            throws FileNotFoundException {
        when(this.git.getRepository(any())).thenReturn(repo);
        when(repo.findLatestReleaseCommit(any())).thenReturn(Optional.of(this.previousTag));
        return testee().retrievePrevious(PROJECT_DIR, current).orElse(null);
    }

    private NpmServices testee() {
        return new NpmServices(this.executor, this.git);
    }
}
