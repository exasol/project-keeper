package com.exasol.projectkeeper.sources.analyze.golang;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.dependencies.VersionedDependency;

/**
 * This class implements a parser for {@code go.mod} files.
 */
class GoModFileParser {
    private final List<RegexAction> regexps;
    String moduleName = null;
    String goVersion;
    boolean insideRequireBlock = false;
    final List<VersionedDependency> dependencies = new ArrayList<>();

    GoModFileParser() {
        this.regexps = List.of( //
                RegexAction.create("^module\\s+(.+)$", match -> this.moduleName = match.group(1)),
                RegexAction.create("^go\\s+(.+)$", match -> this.goVersion = match.group(1)),
                RegexAction.create("^require \\($", match -> this.insideRequireBlock = true),
                RegexAction.create("^\\)$", match -> this.insideRequireBlock = false),
                RegexAction.create("^(?:require\\s+)?([^\\s]+)\\s+([^\\s/]+)(?:\\s*//\\s*(.*))?$",
                        match -> this.dependencies.add(createDependency(match.getGroups()))),
                RegexAction.create(".*", match -> {
                    throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-138")
                            .message("Found unexpected line {{line}} in go.mod file", match.getLine()).toString());
                }));
    }

    private VersionedDependency createDependency(final List<String> groups) {
        final boolean indirect = (groups.size() >= 3) //
                && (groups.get(2) != null) //
                && groups.get(2).startsWith("indirect");
        return VersionedDependency.builder().name(groups.get(0)).version(groups.get(1)).isIndirect(indirect).build();
//        return new Dependency(groups.get(0), groups.get(1), indirect);
    }

    void parse(final String content) {
        Arrays.stream(content.split("\n")).map(String::trim).forEach(this::readLine);
    }

    void readLine(final String line) {
        if (line.isBlank()) {
            return;
        }
        for (final RegexAction regex : this.regexps) {
            final Optional<RegexMatch> match = regex.matches(line);
            if (match.isPresent()) {
                match.get().runAction();
                break;
            }
        }
    }

    /**
     * This class represents an action that should be executed when a given regular expression matches a line from the
     * input.
     */
    private static class RegexAction {
        private final Pattern pattern;
        private final Consumer<RegexMatch> action;

        private RegexAction(final Pattern pattern, final Consumer<RegexMatch> action) {
            this.pattern = pattern;
            this.action = action;
        }

        static RegexAction create(final String pattern, final Consumer<RegexMatch> action) {
            return new RegexAction(Pattern.compile(pattern), action);
        }

        Optional<RegexMatch> matches(final String string) {
            final Matcher matcher = this.pattern.matcher(string);
            if (matcher.matches()) {
                return Optional.of(new RegexMatch(matcher, string, this.action));
            } else {
                return Optional.empty();
            }
        }
    }

    /**
     * This class represents a regular expression that has matched a line from the input. It allows accessing the
     * matched regex groups and the complete line.
     */
    private static class RegexMatch {

        private final Matcher matcher;
        private final Consumer<RegexMatch> action;
        private final String line;

        RegexMatch(final Matcher matcher, final String line, final Consumer<RegexMatch> action) {
            this.matcher = matcher;
            this.line = line;
            this.action = action;
        }

        void runAction() {
            this.action.accept(this);
        }

        String group(final int group) {
            return this.matcher.group(group);
        }

        String getLine() {
            return this.line;
        }

        List<String> getGroups() {
            final List<String> groups = new ArrayList<>(this.matcher.groupCount());
            for (int i = 1; i <= this.matcher.groupCount(); i++) {
                groups.add(this.matcher.group(i));
            }
            return groups;
        }
    }
}
