package com.exasol.projectkeeper.validators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.shared.repository.GitRepository;
import com.exasol.projectkeeper.shared.repository.TaggedCommit;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;

@ExtendWith(MockitoExtension.class)
class VersionIncrementValidatorTest {

    @Mock
    private GitRepository gitRepoMock;

    // [utest->dsn~version-increment-validator~1]
    @ParameterizedTest(name = "Incrementing {0} to {1} returns finding {2}")
    @CsvSource(nullValues = "NULL", value = { //
            "NULL, 1.2.3, NULL", //
            "1.2.3, 1.2.4, NULL", //
            "1.2.3, 1.3.0, NULL", //
            "1.2.3, 2.0.0, NULL", //
            "0.0.0, 0.0.1, NULL", //
            "0.0.0, 0.1.0, NULL", //
            "0.0.0, 1.0.0, NULL", //
            "0.0.1, 0.0.2, NULL", //
            "0.0.1, 0.1.0, NULL", //
            "0.0.1, 1.0.0, NULL", //
            "0.1.0, 0.1.1, NULL", //
            "0.1.0, 0.2.0, NULL", //
            "0.1.0, 1.0.0, NULL", //
            "1.0.0, 1.0.1, NULL", //
            "1.0.0, 1.1.0, NULL", //
            "1.0.0, 2.0.0, NULL", //
            "1.2.3, 1.2.3, E-PK-CORE-184: Project version '1.2.3' is not a valid successor of '1.2.3'.",
            "1.2.3, 1.2.2, E-PK-CORE-184: Project version '1.2.2' is not a valid successor of '1.2.3'.",
            "1.2.3, 1.2.5, E-PK-CORE-184: Project version '1.2.5' is not a valid successor of '1.2.3'.",
            "1.2.3, 1.1.0, E-PK-CORE-184: Project version '1.1.0' is not a valid successor of '1.2.3'.",
            "1.2.3, 1.4.0, E-PK-CORE-184: Project version '1.4.0' is not a valid successor of '1.2.3'.",
            "1.2.3, 0.0.0, E-PK-CORE-184: Project version '0.0.0' is not a valid successor of '1.2.3'.",
            "1.2.3, 3.0.0, E-PK-CORE-184: Project version '3.0.0' is not a valid successor of '1.2.3'.", })
    void validation(final String previousVersion, final String currentVersion, final String expectedFinding) {
        final List<String> findings = getFindings(previousVersion, currentVersion);
        if (expectedFinding == null) {
            assertThat(findings, emptyIterable());
        } else {
            assertAll(() -> assertThat(findings, hasSize(1)), //
                    () -> assertThat(findings, contains(startsWith(expectedFinding))));
        }
    }

    private List<String> getFindings(final String previousVersion, final String currentVersion) {
        when(gitRepoMock.findLatestReleaseCommit(null)) //
                .thenReturn(Optional.ofNullable(previousVersion) //
                        .map(v -> new TaggedCommit(null, v)));
        return testee(currentVersion).validate().stream() //
                .map(SimpleValidationFinding.class::cast) //
                .map(SimpleValidationFinding::getMessage) //
                .toList();
    }

    private VersionIncrementValidator testee(final String projectVersion) {
        return new VersionIncrementValidator(projectVersion, gitRepoMock);
    }
}
