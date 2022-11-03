package com.exasol.projectkeeper.sources.analyze.golang;

import static com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type.COMPILE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChange;
import com.exasol.projectkeeper.sources.analyze.golang.GolangDependencyType.DependencyMatcher;
import com.exasol.projectkeeper.sources.analyze.golang.GolangDependencyType.DependencyMatcher.PrefixMatcher;

class GolangDependencyTypeTest {

    @ParameterizedTest
    @ValueSource(strings = { "abc", "a/v", "x/v_", "123", "123/a" })
    void withoutVersion(final String moduleName) {
        assertThat(DependencyMatcher.forModule(moduleName), not(isA(PrefixMatcher.class)));
    }

    @ParameterizedTest
    @ValueSource(strings = { "abc/v1", "a/v123", "x/v0" })
    void versioned(final String moduleName) {
        assertThat(DependencyMatcher.forModule(moduleName), isA(PrefixMatcher.class));
    }

    @Test
    void identical() {
        verifySingle("abc", "abc", Type.COMPILE);
    }

    @Test
    void golangRuntime() {
        verifySingle(GolangServices.GOLANG_DEPENDENCY_NAME, "xxx", Type.COMPILE);
    }

    @ParameterizedTest
    @ValueSource(strings = { "abc", "abc/v1", "x", "xxxx" })
    void noMatch(final String moduleName) {
        verifySingle(moduleName, "xxx", Type.UNKNOWN);
    }

    @ParameterizedTest
    @ValueSource(strings = { "xxx/v1", "xxx/v0" })
    void prefix() {
        verifySingle("xxx/v1", "xxx-suffix", Type.COMPILE);
    }

    private void verifySingle(final String moduleName, final String dependencyName, final Type expectedType) {
        final GolangDependencyType testee = new GolangDependencyType(List.of( //
                ProjectDependency.builder() //
                        .name(dependencyName) //
                        .type(COMPILE) //
                        .build()));
        final DependencyChange change = change(moduleName);
        assertThat(testee.getType(change), equalTo(expectedType));
    }

    private DependencyChange change(final String moduleName) {
        final DependencyChange change = mock(DependencyChange.class);
        when(change.getArtifactId()).thenReturn(moduleName);
        return change;
    }
}
