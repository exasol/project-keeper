package com.exasol.projectkeeper.sources.analyze.npm;

import static com.exasol.projectkeeper.sources.analyze.npm.NpmServices.LICENSE_CHECKER;
import static com.exasol.projectkeeper.sources.analyze.npm.NpmServices.LIST_DEPENDENCIES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.sources.analyze.generic.CommandExecutor;

class NpmServicesTest {
    @Test
    void getDependencies() {
        final CommandExecutor executor = mock(CommandExecutor.class);
        when(executor.execute(eq(LIST_DEPENDENCIES), any())).thenReturn(JsonFixture.DEPENDENCIES);
        when(executor.execute(eq(LICENSE_CHECKER), any())).thenReturn(JsonFixture.LICENSES);

        final PackageJson current = JsonFixture.samplePackageJson();
        assertThat(new NpmServices(executor).getDependencies(current), isA(ProjectDependencies.class));
    }
}
