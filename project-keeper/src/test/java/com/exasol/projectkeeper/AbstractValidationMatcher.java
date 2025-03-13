package com.exasol.projectkeeper;

import java.util.List;

import org.hamcrest.TypeSafeMatcher;

import com.exasol.projectkeeper.validators.finding.*;

public abstract class AbstractValidationMatcher extends TypeSafeMatcher<Validator> {
    protected List<String> getMessages(final Validator item) {
        final List<ValidationFinding> findings = item.validate();
        return new FindingsUngrouper().ungroupFindings(findings).stream().map(SimpleValidationFinding::getMessage)
                .toList();
    }

    protected String findingsAsString(final List<String> messages) {
        return "\n          - \"" + String.join("\",\n          - \"", messages) + "\"";
    }
}
