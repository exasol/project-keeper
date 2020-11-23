package com.exasol.projectkeeper;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class HasValidationFindingWithMessageMatcher<T extends Iterable<? extends String>>
        extends AbstractValidationMatcher {
    private final Matcher<T> messagesMatcher;

    public HasValidationFindingWithMessageMatcher(final Matcher<T> messagesMatcher) {
        this.messagesMatcher = messagesMatcher;
    }

    public static Matcher<Validator> hasValidationFindingWithMessage(final String expectedMessage) {
        return new HasValidationFindingWithMessageMatcher(hasItem(expectedMessage));
    }

    public static Matcher<Validator> validationErrorMessages(
            final Matcher<? extends Iterable<? extends String>> messagesMatcher) {
        return new HasValidationFindingWithMessageMatcher(messagesMatcher);
    }

    public static Matcher<Validator> hasNoValidationFindings() {
        return new HasValidationFindingWithMessageMatcher(empty());
    }

    public static Matcher<Validator> hasValidationFindings() {
        return new HasValidationFindingWithMessageMatcher(not(empty()));
    }

    @Override
    protected boolean matchesSafely(final Validator item) {
        final List<String> messages = getMessages(item);
        return this.messagesMatcher.matches(messages);
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("finding with messages matching: ");
        this.messagesMatcher.describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(final Validator item, final Description mismatchDescription) {
        final List<String> messages = getMessages(item);
        if (messages.isEmpty()) {
            mismatchDescription.appendText("had not findings");
        } else {
            mismatchDescription.appendText("only had the following findings:" + findingsAsString(messages));
        }
    }
}
