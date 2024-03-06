package com.exasol.projectkeeper.validators.release;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileIO;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;

@ExtendWith(MockitoExtension.class)
class ReleaseValidationBuilderTest {
    private static final Path PROJECT_DIR = Path.of("project-dir");
    private static final String PROJECT_VERSION = "1.2.3";
    private static final String REPO_NAME = "repo-name";

    @Mock
    ChangesFileIO changesFileIOMock;

    @Test
    void validatorCount() {
        assertThat(testee().validators(), hasSize(1));
    }

    // [utest->dsn~verify-release-mode.verify-release-date~1]
    @Test
    void verifyReleaseDate() {
        simulateChangesFile(ChangesFile.builder().releaseDate("invalid"));
        final List<String> findings = getFindings();
        assertThat(findings,
                contains("E-PK-CORE-182: Release date 'invalid' has invalid format in '" + getChangesFilePath() + "'"));
    }

    private void simulateChangesFile(final ChangesFile.Builder changesFileBuilder) {
        when(changesFileIOMock.read(getChangesFilePath())).thenReturn(changesFileBuilder.build());
    }

    private Path getChangesFilePath() {
        return PROJECT_DIR.resolve("doc/changes/changes_" + PROJECT_VERSION + ".md");
    }

    private List<String> getFindings() {
        return testee().validators().stream() //
                .map(Validator::validate) //
                .flatMap(List::stream)//
                .map(SimpleValidationFinding.class::cast) //
                .map(SimpleValidationFinding::getMessage) //
                .toList();
    }

    private ReleaseValidationBuilder testee() {
        return new ReleaseValidationBuilder(REPO_NAME, PROJECT_VERSION, PROJECT_DIR, changesFileIOMock);
    }
}
