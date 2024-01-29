package com.exasol.projectkeeper.validators.changesfile;

import static com.exasol.projectkeeper.validators.changesfile.ChangesFile.DEPENDENCY_UPDATES_HEADING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

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
        final ChangesFile changesFile = changesFileBuilder().setHeader(List.of("heading")).build();
        final List<ChangesFileSection> sections = new DependencySectionFixer(List.of(source)).fix(changesFile)
                .getSections();
        assertThat(sections.size(), equalTo(1));
        assertThat(sections.get(0).getHeading(), equalTo(DEPENDENCY_UPDATES_HEADING));
    }

    private Builder changesFileBuilder() {
        return ChangesFile.builder().projectName("projectName").projectVersion("1.2.3").releaseDate("releaseDate");
    }

    @Test
    void testSectionIsUpdated() {
        final ChangesFile changesFile = changesFileBuilder().setHeader(List.of("heading"))
                .addSection(List.of(DEPENDENCY_UPDATES_HEADING, "myLine")).build();
        final ChangesFile fixedChangesFile = new DependencySectionFixer(List.of(source)).fix(changesFile);
        final List<ChangesFileSection> sections = fixedChangesFile.getSections();
        assertThat(sections.size(), equalTo(1));
        assertThat(sections.get(0).getHeading(), equalTo(DEPENDENCY_UPDATES_HEADING));
        assertThat("dependency fixer changed the changes file", changesFile, not(equalTo(fixedChangesFile)));
    }

    @Test
    void testHeaderIsPreserved() {
        final ChangesFile changesFile = changesFileBuilder().setHeader(List.of("heading"))
                .addSection(List.of(DEPENDENCY_UPDATES_HEADING, "myLine")).build();
        final ChangesFile fixedChangesFile = new DependencySectionFixer(List.of(source)).fix(changesFile);
        assertThat(changesFile.getHeaderSectionLines(), equalTo(fixedChangesFile.getHeaderSectionLines()));
    }
}
