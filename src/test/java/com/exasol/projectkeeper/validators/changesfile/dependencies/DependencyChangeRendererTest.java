package com.exasol.projectkeeper.validators.changesfile.dependencies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

class DependencyChangeRendererTest {
    private static final DependencyChangeRenderer RENDERER = new DependencyChangeRenderer();

    @Test
    void testRenderAdd() {
        final DependencyChange dependencyChange = new NewDependency("com.example", "my-lib", "1.0.0");
        assertThat(RENDERER.render(dependencyChange), equalTo("* Added `com.example:my-lib:1.0.0`"));
    }

    @Test
    void testRenderUpdate() {
        final DependencyChange dependencyChange = new UpdatedDependency("com.example", "my-lib", "1.0.0", "1.0.1");
        assertThat(RENDERER.render(dependencyChange), equalTo("* Updated `com.example:my-lib:1.0.0` to 1.0.1"));
    }

    @Test
    void testRenderRemove() {
        final DependencyChange dependencyChange = new RemovedDependency("com.example", "my-lib", "1.0.0");
        assertThat(RENDERER.render(dependencyChange), equalTo("* Removed `com.example:my-lib:1.0.0`"));
    }
}