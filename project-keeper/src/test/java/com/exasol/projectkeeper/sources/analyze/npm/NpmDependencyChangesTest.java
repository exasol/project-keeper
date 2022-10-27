package com.exasol.projectkeeper.sources.analyze.npm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.*;

public class NpmDependencyChangesTest {

    static final String CURRENT = "/npm/current-package.json";
    static final String PREVIOUS = "/npm/previous-package.json";

    @Test
    void noPrevious() {
        final DependencyChangeReport expected = DependencyChangeReport.builder() //
                .typed(Type.PLUGIN, List.of( //
                        new NewDependency(null, "changed-plugin", "1.2.0"), //
                        new NewDependency(null, "new-plugin", "1.3.0") //
                )) //
                .typed(Type.COMPILE, List.of( //
                        new NewDependency(null, "changed-compile", "2.2.0"), //
                        new NewDependency(null, "new-compile", "2.3.0") //
                )) //
                .build();
        assertThat(report(CURRENT, null), equalTo(expected));
    }

    @Test
    void withPrevious() {
        final DependencyChangeReport expected = DependencyChangeReport.builder() //
                .typed(Type.PLUGIN, List.of( //
                        new UpdatedDependency(null, "changed-plugin", "1.1.0", "1.2.0"), //
                        new NewDependency(null, "new-plugin", "1.3.0"), //
                        new RemovedDependency(null, "removed-plugin", "1.2.0") //
                )) //
                .typed(Type.COMPILE, List.of( //
                        new UpdatedDependency(null, "changed-compile", "2.1.0", "2.2.0"), //
                        new NewDependency(null, "new-compile", "2.3.0"), //
                        new RemovedDependency(null, "removed-compile", "2.2.0") //
                )) //
                .build();
        assertThat(report(CURRENT, PREVIOUS), equalTo(expected));
    }

    private DependencyChangeReport report(final String current, final String previous) {
        return NpmDependencyChanges.report(JsonFixture.packageJson(current),
                Optional.ofNullable(previous == null ? null : JsonFixture.packageJson(previous)));
    }
}
