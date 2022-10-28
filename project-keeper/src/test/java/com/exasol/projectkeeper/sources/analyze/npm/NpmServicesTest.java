package com.exasol.projectkeeper.sources.analyze.npm;

import static com.exasol.projectkeeper.sources.analyze.npm.NpmServices.LICENSE_CHECKER;
import static com.exasol.projectkeeper.sources.analyze.npm.NpmServices.LIST_DEPENDENCIES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

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

    private static final String PREVIOUS_VERSION = "1.1.1";
    private static final Path PROJECT_DIR = Paths.get("/projects/sample-project");
    private static final Path PACKAGE_JSON_FILE = Paths.get("some/file.txt");

    @Test
    void getDependencies() {
        when(this.executor.execute(eq(LIST_DEPENDENCIES), any())).thenReturn(TestData.DEPENDENCIES);
        when(this.executor.execute(eq(LICENSE_CHECKER), any())).thenReturn(TestData.LICENSES);
        final PackageJson current = TestData.samplePackageJson();
        assertThat(testee().getDependencies(current), isA(ProjectDependencies.class));
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
        when(this.previousTag.getTag()).thenReturn(PREVIOUS_VERSION);
        final PackageJson current = currentPackageJson(PACKAGE_JSON_FILE);
        final Exception exception = assertThrows(IllegalStateException.class, () -> retrievePrevious(repo, current));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-134: File " + PACKAGE_JSON_FILE + " does not exist at tag '" + PREVIOUS_VERSION + "'"));
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
