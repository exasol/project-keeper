package com.exasol.projectkeeper.validators.files;

import static java.util.stream.Collectors.joining;

import java.nio.file.Path;
import java.util.*;

import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * Merges Project Keeper's generated-file attributes into an existing {@code .gitattributes} file.
 */
// [impl->dsn~generated-file-attributes~1]
final class GitattributesValidator implements Validator {
    private static final List<Rule> EOL_RULES = List.of(new Rule("*.sh", "text", "eol=lf"),
            new Rule("*.bat", "text", "eol=crlf"));
    private final Path projectDirectory;
    private final Path gitattributesFile;
    private final List<Rule> managedRules;

    GitattributesValidator(final Path projectDirectory, final Collection<String> generatedFiles) {
        this.projectDirectory = projectDirectory;
        this.gitattributesFile = projectDirectory.resolve(".gitattributes");
        this.managedRules = new ArrayList<>(EOL_RULES);
        generatedFiles.stream().map(file -> new Rule(file, "linguist-generated=true"))
                .forEach(this.managedRules::add);
    }

    @Override
    public List<ValidationFinding> validate() {
        return new RequiredFileValidator().validateFile(this.projectDirectory, this.gitattributesFile,
                this::mergeGeneratedRules);
    }

    private Optional<String> mergeGeneratedRules(final String existingContent) {
        if (existingContent == null) {
            return Optional.of(renderRules(this.managedRules));
        }
        final List<Rule> missingRules = this.managedRules.stream()
                .filter(rule -> !containsRule(existingContent, rule))
                .toList();
        if (missingRules.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(appendRules(existingContent, missingRules));
    }

    private static String renderRules(final List<Rule> rules) {
        return rules.stream()
                .map(Rule::render)
                .collect(joining(System.lineSeparator(), "", System.lineSeparator()));
    }

    private static boolean containsRule(final String content, final Rule expectedRule) {
        return Arrays.stream(content.split("\\R"))
                .map(Rule::parse)
                .flatMap(Optional::stream)
                .filter(rule -> rule.patternMatches(expectedRule))
                .reduce((first, second) -> second)
                .filter(expectedRule::equals)
                .isPresent();
    }

    private static String appendRules(final String existingContent, final List<Rule> missingRules) {
        final String lineSeparator = System.lineSeparator();
        final String normalizedContent = existingContent.replace("\r", "")
                .replace("\n", lineSeparator);
        final String separator = normalizedContent.endsWith(lineSeparator) ? "" : lineSeparator;
        return normalizedContent + separator + renderRules(missingRules);
    }

    private record Rule(List<String> tokens) {
        private Rule(final String... tokens) {
            this(List.of(tokens));
        }

        private static Optional<Rule> parse(final String line) {
            final String trimmedLine = line.trim();
            if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                return Optional.empty();
            }
            return Optional.of(new Rule(trimmedLine.split("\\s+")));
        }

        private String render() {
            return String.join(" ", this.tokens);
        }

        private String pattern() {
            return this.tokens.get(0);
        }

        private boolean patternMatches(final Rule other) {
            return this.pattern().equals(other.pattern());
        }
    }
}
