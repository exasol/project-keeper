package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.validators.files.FileTemplate.Validation;

import nl.jqno.equalsverifier.EqualsVerifier;

class FileTemplateFromResourceTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(FileTemplateFromResource.class).verify();
    }

    @Test
    void getContent() {
        final String content = testee().getContent();
        assertThat(content, containsString("runs-on: $ciBuildRunnerOS"));
    }

    private FileTemplateFromResource testee() {
        return new FileTemplateFromResource(".github/workflows/ci-build.yml", Validation.REQUIRE_EXACT);
    }

    @Test
    void rejectsReplacingTheSameKey() {
        final FileTemplateFromResource testee = testee();
        testee.replacing("key", "value1");
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> testee.replacing("key", "value2"));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-170: Cannot add replacement with value 'value2' because 'key' is already registered as replacement with value 'value1'. Remove or rename one of the replacements."));
    }

    @Test
    void getContentReplacesPlaceholders() {
        final String content = testee().replacing("ciBuildRunnerOS", "custom-runner").getContent();
        assertThat(content, containsString("runs-on: custom-runner"));
    }

    @Test
    void getContentFailsWhenPlaceholderNotFound() {
        final FileTemplateFromResource testee = testee().replacing("unknownPlaceholder", "custom-runner");
        final IllegalStateException exception = assertThrows(IllegalStateException.class, testee::getContent);
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-169: Placeholder 'unknownPlaceholder' not found in template 'templates/.github/workflows/ci-build.yml'."
                        + " This is an internal error that should not happen. Please report it by opening a GitHub issue."));
    }
}
