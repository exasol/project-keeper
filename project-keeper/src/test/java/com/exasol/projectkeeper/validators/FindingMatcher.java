package com.exasol.projectkeeper.validators;

import java.util.List;

import org.hamcrest.*;

import com.exasol.projectkeeper.validators.finding.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindingMatcher extends TypeSafeMatcher<List<ValidationFinding>> {
    private final String requiredMessage;

    public static Matcher<List<ValidationFinding>> hasFindingWithMessage(final String message) {
        return new FindingMatcher(message);
    }

    @Override
    protected boolean matchesSafely(final List<ValidationFinding> validationFindings) {
        final List<SimpleValidationFinding> flatFindings = new FindingsUngrouper().ungroupFindings(validationFindings);
        for (final SimpleValidationFinding flatFinding : flatFindings) {
            if (flatFinding.getMessage().equals(this.requiredMessage)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("Has finding with message: ").appendText(this.requiredMessage);
    }
}
