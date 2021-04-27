package com.exasol.projectkeeper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.hamcrest.*;

public class FileContentMatcher extends TypeSafeMatcher<Path> {
    private final Matcher<String> contentMatcher;

    private FileContentMatcher(final Matcher<String> contentMatcher) {
        this.contentMatcher = contentMatcher;
    }

    public static Matcher<Path> hasContent(final Matcher<String> contentMatcher) {
        return new FileContentMatcher(contentMatcher);
    }

    @Override
    protected boolean matchesSafely(final Path item) {
        try {
            final String actualContent = Files.readString(item);
            return this.contentMatcher.matches(actualContent);
        } catch (final IOException exception) {
            return false;
        }
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("file has content matching: ");
        this.contentMatcher.describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(final Path item, final Description mismatchDescription) {
        try {
            final String actualContent = Files.readString(item);
            mismatchDescription.appendText("content was: \"" + actualContent + "\"");
        } catch (final IOException exception) {
            mismatchDescription.appendText("could not open file");
        }
    }
}
