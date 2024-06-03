package com.exasol.projectkeeper.sources.analyze.npm;

import static com.exasol.projectkeeper.sources.analyze.npm.TestData.json;
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

// [utest -> dsn~npm-dependency-additional-information~1]
// [utest -> dsn~npm-dependency-licenses~1]
@ExtendWith(MockitoExtension.class)
class NpmDependenciesTest {

    @Mock
    NpmServices npmServices;

    @Test
    void getDependencies() {
        when(this.npmServices.listDependencies(any())).thenReturn(json(TestData.DEPENDENCIES));
        when(this.npmServices.getLicenses(any())).thenReturn(json(TestData.LICENSES));

        final NpmDependencies testee = new NpmDependencies(this.npmServices, TestData.samplePackageJson());
        assertThat(testee.getDependencies(), containsInAnyOrder( //
                dependency(Type.DEV, "changed-plugin", "1.2.0", "CP"), //
                dependency(Type.DEV, "new-plugin", "1.3.0", "NP"), //
                dependency(Type.COMPILE, "changed-compile", "2.2.0", "CC"), //
                dependency(Type.COMPILE, "new-compile", "2.3.0", "NC") //
        ));
    }

    @Test
    void getDependenciesMissingUrl() {
        when(this.npmServices.listDependencies(any())).thenReturn(json(TestData.DEPENDENCIES_MISSING_URL));
        when(this.npmServices.getLicenses(any())).thenReturn(json(TestData.LICENSES));

        final NpmDependencies testee = new NpmDependencies(this.npmServices, TestData.samplePackageJson());
        assertThat(testee.getDependencies(), containsInAnyOrder( //
                dependency(Type.DEV, "changed-plugin", "1.2.0", "CP"), //
                dependency(Type.DEV, "new-plugin", "1.3.0", "NP"), //
                dependency(Type.COMPILE, "changed-compile", "2.2.0", "CC"), //
                ProjectDependency.builder() //
                        .type(Type.COMPILE)//
                        .name("new-compile") //
                        .websiteUrl(null) //
                        .licenses(List.of(new License("NC" + "-License", "https://" + "new-compile"))) //
                        .build() //
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
