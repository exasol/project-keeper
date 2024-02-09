package com.exasol.projectkeeper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.ClearSystemProperty;
import org.junitpioneer.jupiter.SetSystemProperty;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectKeeperTest {

    @Mock
    private Logger loggerMock;

    @Test
    void createProjectKeeper() {
        assertThat(ProjectKeeper.createProjectKeeper(null, null, null), not(nullValue()));
    }

    @Test
    @ClearSystemProperty(key = "com.exasol.projectkeeper.ownVersion")
    void getOwnVersionWithoutSystemProperty() {
        assertThat(ProjectKeeper.getOwnVersion(), equalTo("(unknownVersion)"));
    }

    @Test
    @SetSystemProperty(key = "com.exasol.projectkeeper.ownVersion", value = "version")
    void getOwnVersionWithSystemProperty() {
        assertThat(ProjectKeeper.getOwnVersion(), equalTo("version"));
    }
}
