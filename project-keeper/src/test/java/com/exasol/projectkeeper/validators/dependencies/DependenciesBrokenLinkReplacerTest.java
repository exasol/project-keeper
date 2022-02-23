package com.exasol.projectkeeper.validators.dependencies;

import static com.exasol.projectkeeper.shared.dependencies.ProjectDependency.Type.COMPILE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.BrokenLinkReplacer;
import com.exasol.projectkeeper.shared.dependencies.License;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;

class DependenciesBrokenLinkReplacerTest {
    private static final String FIXED_URL = "http://example.com";
    private static final String BROKEN_URL = "http://broken.com";
    private static final DependenciesBrokenLinkReplacer REPLACER = new DependenciesBrokenLinkReplacer(
            new BrokenLinkReplacer(List.of(BROKEN_URL + "|" + FIXED_URL)));

    @Test
    void testBrokenProjectUrl() {
        final ProjectDependency dependency = new ProjectDependency("test", "http://broken.com", Collections.emptyList(),
                COMPILE);
        assertThat(runDependencyReplacement(dependency).getWebsiteUrl(), equalTo(FIXED_URL));
    }

    private ProjectDependency runDependencyReplacement(final ProjectDependency dependency) {
        return REPLACER.replaceBrokenLinks(new ProjectWithDependencies("", List.of(dependency))).getDependencies()
                .get(0);
    }

    @Test
    void testNonBrokenProjectUrl() {
        final ProjectDependency dependency = new ProjectDependency("test", "http://other.com", Collections.emptyList(),
                COMPILE);
        assertThat(runDependencyReplacement(dependency).getWebsiteUrl(), equalTo("http://other.com"));
    }

    @Test
    void testBrokenLicenseUrl() {
        final ProjectDependency dependency = new ProjectDependency("test", "http://other.com",
                List.of(new License("my license", BROKEN_URL)), COMPILE);
        assertThat(runDependencyReplacement(dependency).getLicenses().get(0).getUrl(), equalTo(FIXED_URL));
    }

    @Test
    void testProjectNameIsPreserved() {
        final ProjectWithDependencies projectWithDependencies = new ProjectWithDependencies("my-project",
                Collections.emptyList());
        assertThat(REPLACER.replaceBrokenLinks(projectWithDependencies).getProjectName(), equalTo("my-project"));
    }
}