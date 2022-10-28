package com.exasol.projectkeeper.sources.analyze.npm;

import static com.exasol.projectkeeper.sources.analyze.npm.NpmServices.LICENSE_CHECKER;
import static com.exasol.projectkeeper.sources.analyze.npm.NpmServices.LIST_DEPENDENCIES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
        when(this.git.getRepository(any())).thenReturn(repo);
        final TaggedCommit tag = mock(TaggedCommit.class);
        when(repo.findLatestReleaseCommit(any())).thenReturn(Optional.of(tag));
        when(repo.getFileFromCommit(any(), any())).thenReturn(TestData.PREVIOUS);

        final Path path = Paths.get("some/file.txt");
        final PackageJson current = TestData.samplePackageJson();

        final Optional<PackageJson> result = testee().retrievePrevious(path, current);
        assertThat(result.isPresent(), is(true));
        final PackageJson actual = result.get();
        assertThat(actual.getVersion(), equalTo("1.0.0"));
        assertThat(actual.getModuleName(), equalTo(current.getModuleName()));
    }

    private NpmServices testee() {
        return new NpmServices(this.executor, this.git);
    }
}
