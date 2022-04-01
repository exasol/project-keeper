package com.exasol.projectkeeper.sources.analyze.golang;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.sources.analyze.golang.GoModFile.GoModDependency;

class GoModFileParser {
    private final List<Regex> regexps;
    String moduleName = null;
    String goVersion;
    boolean insideRequireBlock = false;
    final List<GoModDependency> dependencies = new ArrayList<>();

    GoModFileParser() {
        this.regexps = List.of( //
                Regex.create("^module\\s+(.+)$", match -> this.moduleName = match.group(1)),
                Regex.create("^go\\s+(.+)$", match -> this.goVersion = match.group(1)),
                Regex.create("^require \\($", match -> this.insideRequireBlock = true),
                Regex.create("^\\)$", match -> this.insideRequireBlock = false),
                Regex.create("^(?:require\\s+)?([^\\s]+)\\s+([^\\s/]+)(?:\\s*//\\s*(.*))?$",
                        match -> this.dependencies.add(createDependency(match.getGroups()))),
                Regex.create(".*", match -> {
                    throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-138")
                            .message("Found unexpected line {{line}} in go.mod file", match.getLine()).toString());
                }));
    }

    private GoModDependency createDependency(final List<String> groups) {
        final boolean indirect = (groups.size() >= 3) //
                && (groups.get(2) != null) //
                && groups.get(2).startsWith("indirect");
        return new GoModDependency(groups.get(0), groups.get(1), indirect);
    }

    void parse(final String content) {
        Arrays.stream(content.split("\n")).map(String::trim).forEach(this::readLine);
    }

    void readLine(final String line) {
        if (line.isBlank()) {
            return;
        }
        for (final Regex regex : this.regexps) {
            final Optional<RegexMatch> match = regex.matches(line);
            if (match.isPresent()) {
                match.get().runAction();
                break;
            }
        }
    }

    private static class Regex {
        Pattern pattern;
        private final Consumer<RegexMatch> action;

        public Regex(final Pattern pattern, final Consumer<RegexMatch> action) {
            this.pattern = pattern;
            this.action = action;
        }

        static Regex create(final String pattern, final Consumer<RegexMatch> action) {
            return new Regex(Pattern.compile(pattern), action);
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
