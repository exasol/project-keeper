package com.exasol.projectkeeper.validators;

import java.util.List;

import org.hamcrest.*;

import com.exasol.projectkeeper.validators.finding.*;

public class FindingMatcher extends TypeSafeMatcher<List<ValidationFinding>> {

    public static Matcher<List<ValidationFinding>> hasFindingWithMessage(final String message) {
        return new FindingMatcher(message, false);
    }

    public static Matcher<List<ValidationFinding>> hasFindingWithMessageMatchingRegex(final String message) {
        return new FindingMatcher(message, true);
    }

    private final String expected;
    private final boolean useRegex;

    public FindingMatcher(final String expected, final boolean useRegex) {
        this.expected = expected;
        this.useRegex = useRegex;
    }

    @Override
    protected boolean matchesSafely(final List<ValidationFinding> validationFindings) {
        final List<SimpleValidationFinding> flatFindings = new FindingsUngrouper().ungroupFindings(validationFindings);
        for (final SimpleValidationFinding flatFinding : flatFindings) {
            if ((this.useRegex && flatFinding.getMessage().matches(this.expected))
                    || flatFinding.getMessage().equals(this.expected)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("Has finding with message: ").appendText(this.expected);
    }
}
