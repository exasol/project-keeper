package com.exasol.projectkeeper.validators.dependencies.renderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.validators.dependencies.Dependency;
import com.exasol.projectkeeper.validators.dependencies.License;

class DependencyPageRendererTest {
    @Test
    void testCompileDependency() {
        final Dependency dependency = buildDependency(Dependency.Scope.COMPILE);
        final String result = new DependencyPageRenderer().render(List.of(dependency));
        assertThat(result, equalTo(
                "<!-- @formatter:off -->\n# Dependencies\n\n## Compile Dependencies\n\n| Dependency  | License          |\n| ----------- | ---------------- |\n| [my-lib][0] | [MIT License][1] |\n\n[0]: https://exasmple.com/mylib\n[1]: https://mit.edu\n"));
    }

    @Test
    void testTestDependency() {
        final Dependency dependency = buildDependency(Dependency.Scope.TEST);
        assertThat(new DependencyPageRenderer().render(List.of(dependency)), containsString("## Test Dependencies"));
    }

    @Test
    void testRuntimeDependency() {
        final Dependency dependency = buildDependency(Dependency.Scope.RUNTIME);
        assertThat(new DependencyPageRenderer().render(List.of(dependency)), containsString("## Runtime Dependencies"));
    }

    @Test
    void testPluginDependency() {
        final Dependency dependency = buildDependency(Dependency.Scope.PLUGIN);
        assertThat(new DependencyPageRenderer().render(List.of(dependency)), containsString("## Plugin Dependencies"));
    }

    private Dependency buildDependency(final Dependency.Scope scope) {
        return new Dependency("my-lib", "https://exasmple.com/mylib",
                List.of(new License("MIT License", "https://mit.edu")), scope);
    }
}