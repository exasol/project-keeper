package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.dependencychanges.*;
import com.exasol.projectkeeper.sources.analyze.golang.GoModFile.GoModDependency;

class GolangServicesTest {
    private static final String PROJECT_VERSION = "1.2.3";
    private static final String MODULE_NAME = "module/name";

    @Test
    void calculateChangesIgnoresModuleNameChange() {
        assertChanges(modFile("mod1", "1.16"), modFile("mod2", "1.16"));
    }

    @Test
    void calculateChangesNoDependencies() {
        assertChanges(modFile(MODULE_NAME, "1.16"), modFile(MODULE_NAME, "1.16"));
    }

    @Test
    void calculateChangesGoVersionChanged() {
        assertChanges(modFile(MODULE_NAME, "1.16"), modFile(MODULE_NAME, "1.17"), updated("golang", "1.16", "1.17"));
    }

    @Test
    void calculateChangesGoVersionAdded() {
        assertChanges(modFile(MODULE_NAME, null), modFile(MODULE_NAME, "1.17"), added("golang", "1.17"));
    }

    @Test
    void calculateChangesGoVersionRemoved() {
        assertChanges(modFile(MODULE_NAME, "1.16"), modFile(MODULE_NAME, null), removed("golang", "1.16"));
    }

    // [utest -> dsn~golang-changed-dependency~1]
    @Test
    void calculateChangesUnchangedDependency() {
        assertChanges(modFile(dep("mod1", "v1")), modFile(dep("mod1", "v1")));
    }

    @Test
    void calculateChangesDependencyAdded() {
        assertChanges(modFile(), modFile(dep("mod1", "v1")), added("mod1", "v1"));
    }

    @Test
    void calculateChangesDependencyRemoved() {
        assertChanges(modFile(dep("mod1", "v1")), modFile(), removed("mod1", "v1"));
    }

    @Test
    void calculateChangesDependencyUpdated() {
        assertChanges(modFile(dep("mod1", "v1")), modFile(dep("mod1", "v2")), updated("mod1", "v1", "v2"));
    }

    @Test
    void calculateChangesNoOldModFile() {
        assertChanges(null, modFile(dep("mod1", "v2")), added("mod1", "v2"), added("golang", "1.17"));
    }

    @Test
    void calculateChangesMultipleChanges() {
        assertChanges(modFile(dep("updated", "v1"), dep("removed", "v3")),
                modFile(dep("updated", "v2"), dep("added", "v4")), updated("updated", "v1", "v2"),
                removed("removed", "v3"), added("added", "v4"));
    }

    @Test
    void calculateChangesIgnoresIndirectDependencies() {
        assertChanges(modFile(indirectDep("updated", "v1"), indirectDep("removed", "v3")),
                modFile(indirectDep("updated", "v2"), indirectDep("added", "v4")));
    }

    private void assertChanges(final GoModFile oldMod, final GoModFile newMod,
            final DependencyChange... expectedChanges) {
        final GolangServices golangServices = new GolangServices(() -> PROJECT_VERSION);
        final List<DependencyChange> changes = golangServices.calculateChanges(oldMod, newMod);
        assertAll(() -> assertThat(changes, hasSize(expectedChanges.length)),
                () -> assertThat(changes, containsInAnyOrder(expectedChanges)));
    }

    private UpdatedDependency updated(final String moduleName, final String oldVersion, final String newVersion) {
        return new UpdatedDependency(null, moduleName, oldVersion, newVersion);
    }

    private NewDependency added(final String moduleName, final String version) {
        return new NewDependency(null, moduleName, version);
    }

    private RemovedDependency removed(final String moduleName, final String version) {
        return new RemovedDependency(null, moduleName, version);
    }

    private GoModFile modFile(final GoModDependency... dependencies) {
        return modFile("mod", "1.17", dependencies);
    }

    private GoModFile modFile(final String moduleName, final String goVersion, final GoModDependency... dependencies) {
        return new GoModFile(moduleName, goVersion, asList(dependencies));
    }

    private GoModDependency indirectDep(final String name, final String version) {
        return new GoModDependency(name, version, true);
    }

    private GoModDependency dep(final String name, final String version) {
        return new GoModDependency(name, version, false);
    }
}
