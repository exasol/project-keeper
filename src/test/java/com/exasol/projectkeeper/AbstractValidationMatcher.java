package com.exasol.projectkeeper;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.TypeSafeMatcher;

public abstract class AbstractValidationMatcher extends TypeSafeMatcher<Validator> {
    protected List<String> getMessages(final Validator item) {
        final List<String> messages = new ArrayList<>();
        item.validate(finding -> messages.add(finding.getMessage()));
        return messages;
    }

    protected String findingsAsString(final List<String> messages) {
        return "\n          - \"" + String.join("\",\n          - \"", messages) + "\"";
    }
}
