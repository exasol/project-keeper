package com.exasol.projectkeeper.validators.changesfile.dependencies;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.shared.dependencychanges.NewDependency;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileSection;
import com.exasol.projectkeeper.validators.changesfile.NamedDependencyChangeReport;

class DependencyChangeReportRendererTest {

    private static final DependencyChangeReport REPORT = DependencyChangeReport.builder()
            .typed(Type.COMPILE, List.of(new NewDependency("com.example", "my-lib", "1.2.3"))).build();
    private static final DependencyChangeReport EMPTY_REPORT = DependencyChangeReport.builder().build();

    @Test
    void testRenderSingleSourceReport() {
        final NamedDependencyChangeReport namedReport = new NamedDependencyChangeReport("my-project", REPORT);
        assertThat(render(namedReport), equalTo("""
                ## Dependency Updates

                ### Compile Dependency Updates

                * Added `com.example:my-lib:1.2.3`"""));
    }

    @Test
    void testRenderMultiSourceReport() {
        final NamedDependencyChangeReport sourceA = new NamedDependencyChangeReport("project A", REPORT);
        final NamedDependencyChangeReport sourceB = new NamedDependencyChangeReport("project B", REPORT);
        assertThat(render(sourceA, sourceB), equalTo(
                """
                        ## Dependency Updates

                        ### Project A

                        #### Compile Dependency Updates

                        * Added `com.example:my-lib:1.2.3`

                        ### Project B

                        #### Compile Dependency Updates

                        * Added `com.example:my-lib:1.2.3`"""));
    }

    @Test
    void testRenderMultiSourceReportWithNoChangesInOneReport() {
        final NamedDependencyChangeReport sourceA = new NamedDependencyChangeReport("project A", REPORT);
        final NamedDependencyChangeReport sourceB = new NamedDependencyChangeReport("project B", EMPTY_REPORT);
        assertThat(render(sourceA, sourceB), equalTo(
                """
                        ## Dependency Updates

                        ### Project A

                        #### Compile Dependency Updates

                        * Added `com.example:my-lib:1.2.3`"""));
    }

    @Test
    void testRenderSourceReportWithoutChanges() {
        final NamedDependencyChangeReport sourceA = new NamedDependencyChangeReport("project A", EMPTY_REPORT);
        final Optional<ChangesFileSection> result = new DependencyChangeReportRenderer().render(List.of(sourceA));
        assertThat(result.isPresent(), is(false));
    }

    private String render(final NamedDependencyChangeReport... reports) {
        final Optional<ChangesFileSection> section = new DependencyChangeReportRenderer().render(asList(reports));
        assertThat(section.isPresent(), is(true));
        return section.get().toString();
    }
}
