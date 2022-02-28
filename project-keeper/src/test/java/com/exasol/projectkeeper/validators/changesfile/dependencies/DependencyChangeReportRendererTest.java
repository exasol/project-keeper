package com.exasol.projectkeeper.validators.changesfile.dependencies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.shared.dependencychanges.NewDependency;
import com.exasol.projectkeeper.validators.changesfile.NamedDependencyChangeReport;

class DependencyChangeReportRendererTest {
    private static final DependencyChangeReport REPORT = new DependencyChangeReport(
            List.of(new NewDependency("com.example", "my-lib", "1.2.3")), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList());
    private static final DependencyChangeReport EMPTY_REPORT = new DependencyChangeReport(Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(), Collections.emptyList());

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
}