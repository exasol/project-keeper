package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasNoValidationFindings;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.validationErrorMessages;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.validators.FindingFixHelper;

// [utest->dsn~generated-file-attributes~1]
class GitattributesValidatorTest {
    private static final String GENERATED_FILE = "generated.txt";
    private static final String GENERATED_RULE = GENERATED_FILE + " linguist-generated=true";
    private static final String SECOND_GENERATED_FILE = "second-generated.txt";
    private static final String SECOND_GENERATED_RULE = SECOND_GENERATED_FILE + " linguist-generated=true";
    private static final String EOL_RULES = "*.sh text eol=lf\n*.bat text eol=crlf\n";
    private static final String MANAGED_RULES = EOL_RULES + GENERATED_RULE + "\n" + SECOND_GENERATED_RULE + "\n";

    @TempDir
    Path projectDirectory;

    @Test
    void createsCompleteTemplateWhenFileIsMissing() throws IOException {
        final GitattributesValidator validator = validator();

        fixAllFindings(validator);

        assertThat(Files.readString(gitattributes()), equalTo(withNativeLineEndings(MANAGED_RULES)));
    }

    @Test
    void preservesCustomContentAndAppendsMissingGeneratedRule() throws IOException {
        Files.writeString(gitattributes(), "# custom rule\r\ncustom/** text\r\n");
        final GitattributesValidator validator = validator();

        assertThat(validator, validationErrorMessages(contains("E-PK-CORE-18: Outdated content: '.gitattributes'")));
        fixAllFindings(validator);

        assertThat(Files.readString(gitattributes()), equalTo(withNativeLineEndings(
                "# custom rule\ncustom/** text\n" + MANAGED_RULES)));
    }

    @Test
    void appendsGeneratedRuleAfterConflictingRule() throws IOException {
        Files.writeString(gitattributes(), GENERATED_RULE + "\n" + GENERATED_FILE + " linguist-generated=false\n");
        final GitattributesValidator validator = validator();

        fixAllFindings(validator);

        assertThat(Files.readString(gitattributes()), equalTo(withNativeLineEndings(
                GENERATED_RULE + "\n" + GENERATED_FILE + " linguist-generated=false\n" + EOL_RULES + GENERATED_RULE
                        + "\n" + SECOND_GENERATED_RULE + "\n")));
    }

    @Test
    void appendsOnlyRulesMissingFromExistingFile() throws IOException {
        Files.writeString(gitattributes(), "# custom rule\n" + EOL_RULES + GENERATED_RULE + "\n");
        final GitattributesValidator validator = validator();

        fixAllFindings(validator);
        assertThat(Files.readString(gitattributes()),
                equalTo(withNativeLineEndings("# custom rule\n" + EOL_RULES + GENERATED_RULE + "\n"
                        + SECOND_GENERATED_RULE + "\n")));
        assertThat(validator, hasNoValidationFindings());
        assertThat(validator, hasNoMoreFindingsAfterApplyingFixes());
    }

    @Test
    void acceptsManagedRulesWithCustomWhitespace() throws IOException {
        Files.writeString(gitattributes(), "\t*.sh  text\teol=lf  \n*.bat\ttext eol=crlf\n" + GENERATED_FILE
                + "   linguist-generated=true\n" + SECOND_GENERATED_FILE + "\tlinguist-generated=true\n");

        assertThat(validator(), hasNoValidationFindings());
    }

    private GitattributesValidator validator() {
        return new GitattributesValidator(this.projectDirectory, List.of(GENERATED_FILE, SECOND_GENERATED_FILE));
    }

    private Path gitattributes() {
        return this.projectDirectory.resolve(".gitattributes");
    }

    private static void fixAllFindings(final GitattributesValidator validator) {
        validator.validate().forEach(FindingFixHelper::fix);
    }

    private static String withNativeLineEndings(final String content) {
        return content.replace("\n", System.lineSeparator());
    }
}
