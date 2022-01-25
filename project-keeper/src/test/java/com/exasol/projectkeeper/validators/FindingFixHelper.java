package com.exasol.projectkeeper.validators;

import static org.mockito.Mockito.mock;

import java.util.List;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.validators.finding.FindingsFixer;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

public class FindingFixHelper {

    public static void fix(final ValidationFinding finding) {
        new FindingsFixer().fixFindings(List.of(finding), mock(Logger.class));
    }
}
