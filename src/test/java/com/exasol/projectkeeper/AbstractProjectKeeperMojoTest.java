package com.exasol.projectkeeper;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class AbstractProjectKeeperMojoTest {

    @Test
    void testGetModulesAddsDefault() throws NoSuchFieldException, IllegalAccessException {
        final AbstractProjectKeeperMojo abstractProjectKeeperMojo = getAbstractProjectKeeperMojo(
                Collections.emptyList());
        assertThat(abstractProjectKeeperMojo.getModules(), containsInAnyOrder("default"));
    }

    @Test
    void testGetModulesWithExplicitModule() throws NoSuchFieldException, IllegalAccessException {
        final AbstractProjectKeeperMojo abstractProjectKeeperMojo = getAbstractProjectKeeperMojo(
                List.of("mavenCentral"));
        assertThat(abstractProjectKeeperMojo.getModules(), containsInAnyOrder("default", "mavenCentral"));
    }

    @Test
    void testGetModulesDoesNotAddDefualtTwice() throws NoSuchFieldException, IllegalAccessException {
        final AbstractProjectKeeperMojo abstractProjectKeeperMojo = getAbstractProjectKeeperMojo(
                List.of("default", "mavenCentral"));
        assertThat(abstractProjectKeeperMojo.getModules(), containsInAnyOrder("default", "mavenCentral"));
    }

    @Test
    void testGetModulesWithUnknownModule() throws NoSuchFieldException, IllegalAccessException {
        final AbstractProjectKeeperMojo abstractProjectKeeperMojo = getAbstractProjectKeeperMojo(List.of("unknown"));
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                abstractProjectKeeperMojo::getModules);
        assertThat(exception.getMessage(), startsWith(
                "E-PK-4 Unknown module: 'unknown'. Please update your <modules> configuration in the pom.file to only use supported modules: "));
    }

    private AbstractProjectKeeperMojo getAbstractProjectKeeperMojo(final List<String> modules)
            throws NoSuchFieldException, IllegalAccessException {
        final AbstractProjectKeeperMojo abstractProjectKeeperMojo = spy(AbstractProjectKeeperMojo.class);
        final Field modulesFiled = AbstractProjectKeeperMojo.class.getDeclaredField("modules");
        modulesFiled.setAccessible(true);
        modulesFiled.set(abstractProjectKeeperMojo, modules);
        return abstractProjectKeeperMojo;
    }
}