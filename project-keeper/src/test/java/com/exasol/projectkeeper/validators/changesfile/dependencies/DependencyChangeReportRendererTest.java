package com.exasol.projectkeeper.validators.changesfile.dependencies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.shared.dependencychanges.NewDependency;
import com.exasol.projectkeeper.validators.changesfile.NamedDependencyChangeReport;

class DependencyChangeReportRendererTest {

    private static final DependencyChangeReport REPORT = DependencyChangeReport.builder()
            .typed(Type.COMPILE, List.of(new NewDependency("com.example", "my-lib", "1.2.3"))).build();
    private static final DependencyChangeReport EMPTY_REPORT = DependencyChangeReport.builder().build();

    @Test
    void testRenderSingleSourceReport() {
        final NamedDependencyChangeReport namedReport = new NamedDependencyChangeReport("my-project", REPORT);
        final String result = String.join("\n", new DependencyChangeReportRenderer().render(List.of(namedReport)));
        assertThat(result, equalTo("## Dependency Updates\n" + "\n" + "### Compile Dependency Updates\n" + "\n"
                + "* Added `com.example:my-lib:1.2.3`"));
    }

    @Test
    void testRenderMultiSourceReport() {
        final NamedDependencyChangeReport sourceA = new NamedDependencyChangeReport("project A", REPORT);
        final NamedDependencyChangeReport sourceB = new NamedDependencyChangeReport("project B", REPORT);
        final String result = String.join("\n", new DependencyChangeReportRenderer().render(List.of(sourceA, sourceB)));
        assertThat(result, equalTo(
                "## Dependency Updates\n\n### Project A\n\n#### Compile Dependency Updates\n\n* Added `com.example:my-lib:1.2.3`\n\n### Project B\n\n#### Compile Dependency Updates\n\n* Added `com.example:my-lib:1.2.3`"));
    }

    @Test
    void testRenderMultiSourceReportWithNoChangesInOneReport() {
        final NamedDependencyChangeReport sourceA = new NamedDependencyChangeReport("project A", REPORT);
        final NamedDependencyChangeReport sourceB = new NamedDependencyChangeReport("project B", EMPTY_REPORT);
        final String result = String.join("\n", new DependencyChangeReportRenderer().render(List.of(sourceA, sourceB)));
        assertThat(result, equalTo(
                "## Dependency Updates\n\n### Project A\n\n#### Compile Dependency Updates\n\n* Added `com.example:my-lib:1.2.3`"));
    }

    @Test
    void testRenderSourceReportWithoutChanges() {
        final NamedDependencyChangeReport sourceA = new NamedDependencyChangeReport("project A", EMPTY_REPORT);
        final String result = String.join("\n", new DependencyChangeReportRenderer().render(List.of(sourceA)));
        assertThat(result, emptyString());
    }
}