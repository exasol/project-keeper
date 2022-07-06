package com.exasol.projectkeeper.validators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.mavenrepo.MavenRepository;
import com.exasol.projectkeeper.validators.OwnVersionValidator.Updater;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

//[utest->dsn~verify-own-version~1]
//[itest->dsn~verify-own-version~1]
//[utest->dsn~self-update~1]
//[itest->dsn~self-update~1]
public class OwnVersionValidatorTest {

    private static String expectedMessage(final int number, final String suffix) {
        return "W-PK-CORE-" + number + ": Could not detect latest .* " + suffix;
    }

    private static final String OUTDATED = "W-PK-CORE-153: Project-keeper version 0.0.1 is outdated."
            + " Please update project-keeper to latest version.*";

    @Test
    void retrieveLatestVersion_IoError() {
        verifyOptionalFinding(testee("0.1.0"), expectedMessage(155, "Connection refused.*"));
    }

    @Test
    void ownVersionUnsupportedFormat() {
        verifyOptionalFinding(testee("a.b.c"), //
                "W-PK-CORE-152: Could not validate version of project-keeper."
                        + " Unsupported format of own version.*");
    }

    @Test
    void latestVersionUnsupportedFormat() throws Exception {
        verifyOptionalFinding(testee("0.0.1", "x.y.z"),
                expectedMessage(154, "Unsupported format of latest version from Maven repository.*"));
    }

    @Test
    void ownVersionOutDatet_ProposeUpdate() throws Exception {
        verifyOptionalFinding(testee("0.0.1", "99999999"), OUTDATED);
    }

    @Tag("integration")
    @Test
    void versionUpToDate_NoFinding() throws Exception {
        final String currentVersion = MavenRepository.mavenPlugin().getLatestVersion();
        final OwnVersionValidator testee = OwnVersionValidator //
                .forMavenPlugin(currentVersion, mock(Updater.class));
        assertThat(testee.validate(), empty());
    }

    @Tag("integration")
    @Test
    void cli_NoFinding() throws Exception {
        final String currentVersion = MavenRepository.cli().getLatestVersion();
        final OwnVersionValidator testee = OwnVersionValidator.forCli(currentVersion);
        assertThat(testee.validate(), empty());
    }

    @Tag("integration")
    @Test
    void cli_NoFix() throws Exception {
        final OwnVersionValidator testee = OwnVersionValidator.forCli("0.0.1");
        final SimpleValidationFinding finding = (SimpleValidationFinding) testee.validate().get(0);
        assertThat(finding.getMessage(), matchesRegex(OUTDATED));
        assertThat(finding.hasFix(), is(false));
    }

    // private helpers

    private OwnVersionValidator testee(final String currentVersion) {
        return testee(currentVersion, new MavenRepository("https://localhost/unknown/end/point"));
    }

    private OwnVersionValidator testee(final String currentVersion, final String latestVersion) throws Exception {
        final MavenRepository repo = mock(MavenRepository.class);
        when(repo.getLatestVersion()).thenReturn(latestVersion);
        return testee(currentVersion, repo);
    }

    private OwnVersionValidator testee(final String currentVersion, final MavenRepository repo) {
        final Updater updater = mock(Updater.class);
        return new OwnVersionValidator(currentVersion, repo, updater);
    }

    private SimpleValidationFinding verifyOptionalFinding(final OwnVersionValidator testee,
            final String expectedMessagePattern) {
        final List<ValidationFinding> list = testee.validate();
        assertThat(list, hasSize(1));
        assertThat(list.get(0), isA(SimpleValidationFinding.class));
        final SimpleValidationFinding finding = (SimpleValidationFinding) list.get(0);
        assertThat(finding.getMessage(), matchesRegex(expectedMessagePattern));
        assertThat(finding.isOptional(), is(true));
        return finding;
    }

}
