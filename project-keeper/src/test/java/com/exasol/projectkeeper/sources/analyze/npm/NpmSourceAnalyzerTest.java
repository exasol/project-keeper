package com.exasol.projectkeeper.sources.analyze.npm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.sources.AnalyzedSourceImpl;

class NpmSourceAnalyzerTest {
    @Test
    void test() {
        final NpmServices services = mock(NpmServices.class);

        final PackageJson current = JsonFixture.samplePackageJson();
        when(services.readPackageJson(any())).thenReturn(current);
        when(services.listDependencies(any())).thenReturn(JsonFixture.dependencyInfos());
        when(services.getLicenses(any())).thenReturn(JsonFixture.licenseInfos());

        doCallRealMethod().when(services).getDependencies(any());
        final ProjectDependencies dependencies = services.getDependencies(current);

        final Optional<PackageJson> previous = Optional.of(JsonFixture.previousPackageJson());
        when(services.retrievePrevious(any(), any())).thenReturn(previous);

        final Path path = Paths.get("sample/path");
        final Source source = Source.builder() //
                .path(path) //
                .modules(Set.of()) //
                .build();

        final Path projectDir = Paths.get("");
        final List<AnalyzedSource> result = new NpmSourceAnalyzer(services).analyze(projectDir, List.of(source));
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
}
