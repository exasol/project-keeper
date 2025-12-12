package com.exasol.projectkeeper.validators.pom;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.RepoInfo;
import com.exasol.projectkeeper.shared.config.ParentPomRef;
import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.validators.finding.*;

class GeneratedPomFileCustomizerIT {

    @Test
    void run(@TempDir final Path projectDir) throws IOException {
        final String pomContent = generateParentPom(List.of(ProjectKeeperModule.INTEGRATION_TESTS));
        assertThat(pomContent, not(containsString("javaagent")));
        final Path generatedParentPom = projectDir.resolve(PomFileValidator.GENERATED_PARENT_POM_NAME);
        Files.writeString(generatedParentPom, pomContent);
        final List<ValidationFinding> findings = new GeneratedPomFileCustomizer(
                List.of(AnalyzedMavenSource.builder().path(projectDir.resolve("pom.xml"))
                        .dependencies(new ProjectDependency(Type.TEST, "Mockito", "org.mockito:mockito-core", "website",
                                emptyList()))
                        .build()),
                projectDir)
                .validate();
        runFixes(findings);
        assertThat(Files.readString(generatedParentPom),
                allOf(containsString("<argLine>-javaagent:${org.mockito:mockito-core:jar} ${argLine}</argLine>")));
    }

    private void runFixes(final List<ValidationFinding> findings) {
        assertThat(findings, hasSize(1));
        final ValidationFindingGroup findingGroup = (ValidationFindingGroup) findings.get(0);
        findingGroup.getFindings().stream().map(SimpleValidationFinding.class::cast)
                .map(SimpleValidationFinding::getFix)
                .forEach(fix -> fix.fixError(new PrintLogger()));
        findingGroup.getPostFix().run();
    }

    private String generateParentPom(final Collection<ProjectKeeperModule> enabledModules) {
        return new PomFileGenerator().generatePomContent(enabledModules, "group-id", "artifact", "version",
                new ParentPomRef("parent-group", "parent-artifact", "parent-version", "parent-rel-path"),
                new RepoInfo("repo-name", "license-name"));
    }

    static class PrintLogger implements Logger {

        @Override
        public void info(final String message) {
            System.out.println(message);
        }

        @Override
        public void warn(final String message) {
            System.out.println(message);
        }

        @Override
        public void error(final String message) {
            System.out.println(message);
        }

    }
}
