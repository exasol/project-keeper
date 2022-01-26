package com.exasol.projectkeeper;

import static org.mockito.Mockito.mock;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.exasol.projectkeeper.validators.finding.FindingsFixer;

public class HasNoMoreFindingsAfterApplyingFixesMatcher extends AbstractValidationMatcher {

    public static Matcher<Validator> hasNoMoreFindingsAfterApplyingFixes() {
        return new HasNoMoreFindingsAfterApplyingFixesMatcher();
    }

    @Override
    protected boolean matchesSafely(final Validator item) {
        item.validate().forEach(finding -> new FindingsFixer(mock(Logger.class)).fixFindings(List.of(finding)));
        return getMessages(item).isEmpty();
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("no findings after applying fixes");
    }

    @Override
    protected void describeMismatchSafely(final Validator item, final Description mismatchDescription) {
        mismatchDescription.appendText("still had the following findings:" + findingsAsString(getMessages(item)));
    }
}
