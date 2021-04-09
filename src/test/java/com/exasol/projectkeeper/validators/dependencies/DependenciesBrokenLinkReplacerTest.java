package com.exasol.projectkeeper.validators.dependencies;

import static com.exasol.projectkeeper.validators.dependencies.ProjectDependency.Type.COMPILE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.BrokenLinkReplacer;

class DependenciesBrokenLinkReplacerTest {
    private static final String FIXED_URL = "http://example.com";
    private static final String BROKEN_URL = "http://broken.com";
    private static final DependenciesBrokenLinkReplacer REPLACER = new DependenciesBrokenLinkReplacer(
            new BrokenLinkReplacer(List.of(BROKEN_URL + "|" + FIXED_URL)));

    @Test
    void testBrokenProjectUrl() {
        final ProjectDependency dependency = new ProjectDependency("test", "http://broken.com", Collections.emptyList(),
                COMPILE);
        assertThat(REPLACER.replaceBrokenLinks(List.of(dependency)).get(0).getWebsiteUrl(), equalTo(FIXED_URL));
    }

    @Test
    void testNonBrokenProjectUrl() {
        final ProjectDependency dependency = new ProjectDependency("test", "http://other.com", Collections.emptyList(),
                COMPILE);
        assertThat(REPLACER.replaceBrokenLinks(List.of(dependency)).get(0).getWebsiteUrl(),
                equalTo("http://other.com"));
    }

    @Test
    void testBrokenLicenseUrl() {
        final ProjectDependency dependency = new ProjectDependency("test", "http://other.com",
                List.of(new License("my license", BROKEN_URL)), COMPILE);
        assertThat(REPLACER.replaceBrokenLinks(List.of(dependency)).get(0).getLicenses().get(0).getUrl(),
                equalTo(FIXED_URL));
    }
}