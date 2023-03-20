package com.exasol.projectkeeper.validators.dependencies.renderer;

import static com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type.COMPILE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.projectkeeper.shared.dependencies.License;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.validators.dependencies.ProjectWithDependencies;

class DependencyPageRendererTest {

    @ParameterizedTest
    @ValueSource(strings = { "The Apache Software License", "Apache License" })
    void testApachaLicense(final String licenseName) {
        final List<ProjectWithDependencies> projects = singleProjectWith( //
                buildDependency("maven-clean-plugin", List.of(buildLicense(licenseName))));
        assertThat(new DependencyPageRenderer().render(projects),
                containsString(AlternatingDependenciesWorkaraound.LICENSE.to));
        assertThat(new DependencyPageRenderer().render(projects),
                not(containsString(AlternatingDependenciesWorkaraound.LICENSE.from)));
    }

    @Test
    void twoDependenciesSameLicense() {
        final List<License> licenses = List.of(buildLicense("L1"), buildLicense("L2"));
        final List<ProjectWithDependencies> projects = singleProjectWith( //
                buildDependency("lombok", List.of(buildLicense("L0"))), //
                buildDependency("Jakarta JSON Processing API", licenses), //
                buildDependency("JSON-B API", licenses), //
                buildDependency("org.eclipse.yasson", List.of(buildLicense("Lx"))));
        assertThat(new DependencyPageRenderer().render(projects), equalTo(lines( //
                "<!-- @formatter:off -->", //
                "# Dependencies", "", //
                "## Compile Dependencies", "", //
                "| Dependency                       | License                          |", //
                "| -------------------------------- | -------------------------------- |", //
                "| [lombok][0]                      | [L0 License][1]                  |", //
                "| [Jakarta JSON Processing API][2] | [L1 License][3]; [L2 License][4] |", //
                "| [JSON-B API][5]                  | [L1 License][3]; [L2 License][4] |", //
                "| [org.eclipse.yasson][6]          | [Lx License][7]                  |", //
                "", //
                "[0]: https://company-lombok.com/download", //
                "[1]: https://l0.org/license", //
                "[2]: https://company-Jakarta JSON Processing API.com/download", //
                "[3]: https://l1.org/license", //
                "[4]: https://l2.org/license", //
                "[5]: https://company-JSON-B API.com/download", //
                "[6]: https://company-org.eclipse.yasson.com/download", //
                "[7]: https://lx.org/license")));
    }

    @Test
    void testCompileDependency() {
        final ProjectDependency dependency = buildDependency(ProjectDependency.Type.COMPILE);
        final String result = new DependencyPageRenderer().render(singleProjectWith(dependency));
        assertThat(result, equalTo(lines( //
                "<!-- @formatter:off -->", //
                "# Dependencies", "", //
                "## Compile Dependencies", "", //
                "| Dependency  | License          |", //
                "| ----------- | ---------------- |", //
                "| [my-lib][0] | [MIT License][1] |", //
                "", //
                "[0]: https://example.com/mylib", //
                "[1]: https://mit.edu")));
    }

    private List<ProjectWithDependencies> singleProjectWith(final ProjectDependency... dependencies) {
        return List.of(new ProjectWithDependencies("", Arrays.asList(dependencies)));
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
        assertThat(new DependencyPageRenderer().render(projects), equalTo(lines( //
                "<!-- @formatter:off -->", //
                "# Dependencies", "", //
                "## Project a", "", //
                "### Compile Dependencies", "", //
                "| Dependency  | License          |", //
                "| ----------- | ---------------- |", //
                "| [my-lib][0] | [MIT License][1] |", //
                "", //
                "## Project b", "", //
                "### Compile Dependencies", //
                "", //
                "| Dependency  | License          |", //
                "| ----------- | ---------------- |", //
                "| [my-lib][0] | [MIT License][1] |", //
                "", //
                "[0]: https://example.com/mylib", //
                "[1]: https://mit.edu")));
    }

    private final String NEWLINE = System.lineSeparator();

    private String lines(final String... lines) {
        return Stream.of(lines).collect(Collectors.joining(this.NEWLINE, "", this.NEWLINE));
    }

    private License buildLicense(final String id) {
        return new License(id + " License", "https://" + id.toLowerCase() + ".org/license");
    }

    private ProjectDependency buildDependency(final String id, final List<License> licenses) {
        return ProjectDependency.builder() //
                .type(COMPILE) //
                .name(id) //
                .websiteUrl("https://company-" + id + ".com/download") //
                .licenses(licenses) //
                .build();
    }

    private ProjectDependency buildDependency(final ProjectDependency.Type type) {
        return ProjectDependency.builder() //
                .type(type) //
                .name("my-lib") //
                .websiteUrl("https://example.com/mylib") //
                .licenses(List.of(new License("MIT License", "https://mit.edu"))) //
                .build();
    }
}
