package com.exasol.projectkeeper.validators.changesfile;

import static com.exasol.projectkeeper.validators.changesfile.ChangesFile.DEPENDENCY_UPDATES_HEADING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.jupiter.api.*;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.shared.dependencychanges.NewDependency;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile.Builder;

@Tag("integration")
// [utest->dsn~dependency-section-in-changes_x.x.x.md-file-validator~1]
class DependencySectionFixerTest {
    private static AnalyzedMavenSource source;

    @BeforeAll
    static void beforeAll() {
        source = AnalyzedMavenSource.builder() //
                .dependencyChanges(DependencyChangeReport.builder() //
                        .typed(Type.COMPILE, List.of(new NewDependency("com.example", "my-lib", "1.2.3"))) //
                        .build())
                .projectName("my test project") //
                .isRootProject(true) //
                .build();
    }

    @Test
    void testSectionIsAdded() {
        final ChangesFile changesFile = changesFileBuilder().addSection(ChangesFileSection.builder("heading").build())
                .build();
        final ChangesFile fixedChangesFile = new DependencySectionFixer(List.of(source)).fix(changesFile);
        assertThat(fixedChangesFile.getDependencyChangeSection().get().getHeading(),
                equalTo(DEPENDENCY_UPDATES_HEADING));
    }

    private Builder changesFileBuilder() {
        return ChangesFile.builder().projectName("projectName").projectVersion("1.2.3").releaseDate("releaseDate")
                .summary(ChangesFileSection.builder("## Summary").build());
    }

    @Test
    void testSectionIsUpdated() {
        final ChangesFile changesFile = changesFileBuilder().dependencyChangeSection(
                ChangesFileSection.builder("## Dependency Updates").addLine("content will be overwritten").build())
                .build();
        final ChangesFile fixedChangesFile = new DependencySectionFixer(List.of(source)).fix(changesFile);
        final ChangesFileSection changesFileSection = fixedChangesFile.getDependencyChangeSection().get();
        assertThat(changesFileSection.getHeading(), equalTo(DEPENDENCY_UPDATES_HEADING));
        assertThat(changesFileSection.getContent(),
                contains("", "### Compile Dependency Updates", "", "* Added `com.example:my-lib:1.2.3`"));
        assertThat("dependency fixer changed the changes file", changesFile,
                allOf(not(equalTo(fixedChangesFile)), not(sameInstance(fixedChangesFile))));
    }

    @Test
    void testDependencySectionIsRemoved() {
        final ChangesFile changesFile = changesFileBuilder().addSection(ChangesFileSection.builder("heading").build())
                .dependencyChangeSection(ChangesFileSection.builder("## Dependency Updates")
                        .addLine("content will be preserved").build())
                .build();
        final ChangesFile fixedChangesFile = new DependencySectionFixer(List.of()).fix(changesFile);

        assertThat(fixedChangesFile.getDependencyChangeSection().isPresent(), is(false));
        assertThat(fixedChangesFile, not(sameInstance(changesFile)));
        assertThat("dependency fixer changed the changes file", changesFile,
                allOf(not(equalTo(fixedChangesFile)), not(sameInstance(fixedChangesFile))));
    }
}
