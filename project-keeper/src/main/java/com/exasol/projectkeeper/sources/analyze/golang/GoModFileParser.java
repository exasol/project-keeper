package com.exasol.projectkeeper.sources.analyze.golang;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exasol.projectkeeper.sources.analyze.golang.GoModFile.GoModDependency;

class GoModFileParser {

    List<Regex> regexps;
    String moduleName = null;
    String goVersion;
    boolean insideRequireBlock = false;
    List<GoModDependency> dependencies = new ArrayList<>();

    GoModFileParser() {
        this.regexps = List.of( //
                Regex.create("^module\\s+(.+)$", groups -> this.moduleName = groups.get(0)),
                Regex.create("^go\\s+(.+)$", groups -> this.goVersion = groups.get(0)),
                Regex.create("^require \\($", groups -> this.insideRequireBlock = true),
                Regex.create("^\\)$", groups -> this.insideRequireBlock = false),
                Regex.create("^([^\\s]+)\\s+([^\\s/]+)(?:\\s*//\\s*(.*))?$",
                        groups -> this.dependencies.add(createDependency(groups))));
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
        private final Consumer<List<String>> action;

        public Regex(final Pattern pattern, final Consumer<List<String>> action) {
            this.pattern = pattern;
            this.action = action;
        }

        static Regex create(final String pattern, final Consumer<List<String>> action) {
            return new Regex(Pattern.compile(pattern), action);
        }

        Optional<RegexMatch> matches(final String string) {
            final Matcher matcher = this.pattern.matcher(string);
            if (matcher.matches()) {
                return Optional.of(new RegexMatch(matcher, this.action));
            } else {
                return Optional.empty();
            }
        }
    }

    private static class RegexMatch {

        private final Matcher matcher;
        private final Consumer<List<String>> action;

        RegexMatch(final Matcher matcher, final Consumer<List<String>> action) {
            this.matcher = matcher;
            this.action = action;
        }

        void runAction() {
            this.action.accept(getMatchedGroups());
        }

        private List<String> getMatchedGroups() {
            final List<String> groups = new ArrayList<>(this.matcher.groupCount());
            for (int i = 1; i <= this.matcher.groupCount(); i++) {
                groups.add(this.matcher.group(i));
            }
            return groups;
        }
    }
}
