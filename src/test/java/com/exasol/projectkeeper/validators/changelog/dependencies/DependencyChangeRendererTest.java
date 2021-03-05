package com.exasol.projectkeeper.validators.changelog.dependencies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

class DependencyChangeRendererTest {
    private static final DependencyChangeRenderer RENDERER = new DependencyChangeRenderer();

    @Test
    void testRenderAdd() {
        final DependencyChange dependencyChange = new DependencyAdd("com.example", "my-lib", "1.0.0");
        assertThat(RENDERER.render(dependencyChange), equalTo("* Added `com.example:my-lib:1.0.0`"));
    }

    @Test
    void testRenderUpdate() {
        final DependencyChange dependencyChange = new DependencyUpdate("com.example", "my-lib", "1.0.0", "1.0.1");
        assertThat(RENDERER.render(dependencyChange), equalTo("* Updated `com.example:my-lib:1.0.0` to 1.0.1"));
    }

    @Test
    void testRenderRemove() {
        final DependencyChange dependencyChange = new DependencyRemove("com.example", "my-lib", "1.0.0");
        assertThat(RENDERER.render(dependencyChange), equalTo("* Removed `com.example:my-lib:1.0.0`"));
    }
}