package com.exasol.projectkeeper.validators.dependencies.renderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.dependencies.License;
import com.exasol.projectkeeper.dependencies.ProjectDependency;
import com.exasol.projectkeeper.validators.dependencies.ProjectWithDependencies;

class DependencyPageRendererTest {
    @Test
    void testCompileDependency() {
        final ProjectDependency dependency = buildDependency(ProjectDependency.Type.COMPILE);
        final String result = new DependencyPageRenderer().render(singleProjectWith(dependency));
        assertThat(result, equalTo(
                "<!-- @formatter:off -->\n# Dependencies\n\n## Compile Dependencies\n\n| Dependency  | License          |\n| ----------- | ---------------- |\n| [my-lib][0] | [MIT License][1] |\n\n[0]: https://exasmple.com/mylib\n[1]: https://mit.edu\n"
                        .replace("\n", System.lineSeparator())));
    }

    private List<ProjectWithDependencies> singleProjectWith(final ProjectDependency dependency) {
        return List.of(new ProjectWithDependencies("", List.of(dependency)));
    }

    @Test
    void testTestDependency() {
        final ProjectDependency dependency = buildDependency(ProjectDependency.Type.TEST);
        assertThat(new DependencyPageRenderer().render(singleProjectWith(dependency)),
                containsString("## Test Dependencies"));
    }

    @Test
    void testRuntimeDependency() {
        final ProjectDependency dependency = buildDependency(ProjectDependency.Type.RUNTIME);
        assertThat(new DependencyPageRenderer().render(singleProjectWith(dependency)),
                containsString("## Runtime Dependencies"));
    }

    @Test
    void testPluginDependency() {
        final ProjectDependency dependency = buildDependency(ProjectDependency.Type.PLUGIN);
        assertThat(new DependencyPageRenderer().render(singleProjectWith(dependency)),
                containsString("## Plugin Dependencies"));
    }

    @Test
    void testMultiProjectSetup() {
        final ProjectDependency dependency = buildDependency(ProjectDependency.Type.COMPILE);
        final List<ProjectWithDependencies> projects = List.of(
                new ProjectWithDependencies("project a", List.of(dependency)),
                new ProjectWithDependencies("project b", List.of(dependency)));
        assertThat(new DependencyPageRenderer().render(projects), equalTo(
                "<!-- @formatter:off -->\n# Dependencies\n\n## Project a\n\n### Compile Dependencies\n\n| Dependency  | License          |\n| ----------- | ---------------- |\n| [my-lib][0] | [MIT License][1] |\n\n## Project b\n\n### Compile Dependencies\n\n| Dependency  | License          |\n| ----------- | ---------------- |\n| [my-lib][0] | [MIT License][1] |\n\n[0]: https://exasmple.com/mylib\n[1]: https://mit.edu\n"
                        .replace("\n", System.lineSeparator())));
    }

    private ProjectDependency buildDependency(final ProjectDependency.Type type) {
        return new ProjectDependency("my-lib", "https://exasmple.com/mylib",
                List.of(new License("MIT License", "https://mit.edu")), type);
    }
}