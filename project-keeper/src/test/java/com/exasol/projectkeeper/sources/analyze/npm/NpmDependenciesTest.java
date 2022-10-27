package com.exasol.projectkeeper.sources.analyze.npm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencies.License;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;

@ExtendWith(MockitoExtension.class)
class NpmDependenciesTest {

    @Mock
    NpmServices npmServices;

    @Test
    void getDependencies() {
        when(this.npmServices.listDependencies(any())).thenReturn(JsonFixture.dependencyInfos());
        when(this.npmServices.getLicenses(any())).thenReturn(JsonFixture.licenseInfos());

        final NpmDependencies testee = new NpmDependencies(this.npmServices, JsonFixture.samplePackageJson());
        assertThat(testee.getDependencies(), containsInAnyOrder( //
                dependency(Type.PLUGIN, "changed-plugin", "1.2.0", "CP"), //
                dependency(Type.PLUGIN, "new-plugin", "1.3.0", "NP"), //
                dependency(Type.COMPILE, "changed-compile", "2.2.0", "CC"), //
                dependency(Type.COMPILE, "new-compile", "2.3.0", "NC") //
        ));
    }

    private ProjectDependency dependency(final Type type, final String name, final String version,
            final String license) {
        return ProjectDependency.builder() //
                .type(type)//
                .name(name) //
                .websiteUrl("https://" + name + "/-/" + version + ".tgz") //
                .licenses(List.of(new License(license + "-License", "https://" + name))) //
                .build();
    }
}
