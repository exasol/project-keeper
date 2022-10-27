package com.exasol.projectkeeper.sources.analyze.npm;

import static com.exasol.projectkeeper.sources.analyze.npm.NpmServices.LICENSE_CHECKER;
import static com.exasol.projectkeeper.sources.analyze.npm.NpmServices.LIST_DEPENDENCIES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.sources.AnalyzedSourceImpl;
import com.exasol.projectkeeper.sources.analyze.generic.CommandExecutor;

class NpmSourceAnalyzerTest {
    @Test
    void test(@TempDir final Path tempDir) {
        final NpmServices services = mock(NpmServices.class);

        final PackageJson current = JsonFixture.samplePackageJson();
        when(services.readPackageJson(any())).thenReturn(current);

        final ProjectDependencies dependencies = getDependencies();
        when(services.getDependencies(any())).thenReturn(dependencies);

        final Optional<PackageJson> previous = Optional.of(JsonFixture.previousPackageJson());
        when(services.retrievePrevious(any(), any())).thenReturn(previous);

        final Path path = Paths.get("sample/path");
        final Source source = Source.builder() //
                .path(path) //
                .modules(Set.of()) //
                .build();

        final List<AnalyzedSource> result = new NpmSourceAnalyzer(services).analyze(tempDir, List.of(source));
        assertThat(result, iterableWithSize(1));
        final AnalyzedSourceImpl expected = AnalyzedSourceImpl.builder() //
                .path(path) //
                .projectName("sample") //
                .moduleName("module-name") //
                .version("2.0.0") //
                .isRootProject(false) //
                .advertise(true) //
                .dependencies(dependencies) //
                .modules(Set.of()) //
                .dependencyChanges(NpmDependencyChanges.report(current, previous)) //
                .build();
        assertThat(result.get(0), equalTo(expected));
    }

    private ProjectDependencies getDependencies() {
        final CommandExecutor executor = mock(CommandExecutor.class);
        when(executor.execute(eq(LIST_DEPENDENCIES), any())).thenReturn(JsonFixture.DEPENDENCIES);
        when(executor.execute(eq(LICENSE_CHECKER), any())).thenReturn(JsonFixture.LICENSES);
        final PackageJson current = JsonFixture.samplePackageJson();
        final NpmServices services = new NpmServices(executor);
        return new ProjectDependencies(new NpmDependencies(services, current).getDependencies());
    }
}
