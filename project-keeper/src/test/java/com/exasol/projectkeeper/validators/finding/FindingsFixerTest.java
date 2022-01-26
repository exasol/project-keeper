package com.exasol.projectkeeper.validators.finding;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.exasol.projectkeeper.Logger;

class FindingsFixerTest {

    @Test
    void testFix() {
        final SimpleValidationFinding.Fix fix1 = mock(SimpleValidationFinding.Fix.class);
        final SimpleValidationFinding.Fix fix2 = mock(SimpleValidationFinding.Fix.class);
        final SimpleValidationFinding.Fix fix3 = mock(SimpleValidationFinding.Fix.class);
        final Runnable postFix = mock(Runnable.class);
        final SimpleValidationFinding finding1 = SimpleValidationFinding.withMessage("test 1").andFix(fix1).build();
        final SimpleValidationFinding finding2 = SimpleValidationFinding.withMessage("test 2").andFix(fix2).build();
        final SimpleValidationFinding finding3 = SimpleValidationFinding.withMessage("test 3").andFix(fix3).build();
        final Logger logger = mock(Logger.class);
        new FindingsFixer(logger)
                .fixFindings(List.of(finding1, new ValidationFindingGroup(List.of(finding2, finding3), postFix)));
        final InOrder inOrder = inOrder(fix1, fix2, fix3, postFix);
        inOrder.verify(fix1).fixError(logger);
        inOrder.verify(fix2).fixError(logger);
        inOrder.verify(fix3).fixError(logger);
        inOrder.verify(postFix).run();
    }
}