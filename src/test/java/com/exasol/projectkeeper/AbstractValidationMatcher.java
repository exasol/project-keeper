package com.exasol.projectkeeper;

import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.TypeSafeMatcher;

public abstract class AbstractValidationMatcher extends TypeSafeMatcher<Validator> {
    protected List<String> getMessages(final Validator item) {
        return item.validate().stream().map(ValidationFinding::getMessage).collect(Collectors.toList());
    }

    protected String findingsAsString(final List<String> messages) {
        return "\n          - \"" + String.join("\",\n          - \"", messages) + "\"";
    }
}
