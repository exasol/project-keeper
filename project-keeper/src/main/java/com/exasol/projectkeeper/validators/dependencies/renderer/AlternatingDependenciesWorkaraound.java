package com.exasol.projectkeeper.validators.dependencies.renderer;

import java.util.List;

import com.exasol.projectkeeper.validators.workarounds.Workaround;

/**
 * For a few particular licenses issue #436 describes inconsistent content of file dependencies.md. Sometimes PK
 * expected the license name "The Apache Software License" while in other cases the name will be "Apache License".
 * <p>
 * Even extensive investigations failed to identify the root cause of this problem.
 * <p>
 * This class therefore implements a workaround simply replacing the one string by the other to have at least consistent
 * and stable results."));
 */
public class AlternatingDependenciesWorkaraound implements Workaround {

    static final Replacement LICENSE = new Replacement("The Apache Software License", "Apache License");
    static final Replacement URL = new Replacement("http://maven.apache.org", "https://maven.apache.org");

    private static final List<Replacement> REPLACEMENTS = List.of(LICENSE, URL);

    @Override
    public String apply(final String input) {
        String result = input;
        for (final Replacement repl : REPLACEMENTS) {
            result = result.replace(repl.from, repl.to);
        }
        return result;
    }

    static class Replacement {
        String from;
        String to;

        Replacement(final String from, final String to) {
            this.from = from;
            this.to = to;
        }
    }
}
