package com.exasol.projectkeeper.plugin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AbstractProjectKeeperMojoTest {

    @Test
    void testInvalidSkipValue() throws NoSuchFieldException, IllegalAccessException {
        final AbstractProjectKeeperMojo abstractProjectKeeperMojo = Mockito.spy(AbstractProjectKeeperMojo.class);
        final Field skipField = AbstractProjectKeeperMojo.class.getDeclaredField("skip");
        skipField.setAccessible(true);
        skipField.set(abstractProjectKeeperMojo, "otherValue");
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                abstractProjectKeeperMojo::isEnabled);
        assertThat(exception.getMessage(), Matchers.equalTo(
                "E-PK-MVNP-75: Invalid value 'otherValue' for property 'project-keeper.skip'. Please set the property to 'true' or 'false'."));
    }
}