package com.exasol.projectkeeper.validators.dependencies.renderer;

import java.util.Arrays;

/**
 * This class serves as a marker to easily find all places affected by a particular workaround.
 */
public enum Workarounds {

    /** workaround alternating dependencies **/
    ALTERNATING_DEPENDENCIES(State.ACTIVE,
            description(
                    "For a few particular licenses issue #436 describes inconsistent content of file dependencies.md.",
                    "Sometimes PK expected the license name 'The Apache Software License'",
                    " while in other cases the name will be 'Apache License'.",
                    "Even extensive investigations failed to identify the root cause of this problem.",
                    "The current solution is a workaround simply replacing the one string by the",
                    " other to have at least consistent and stable results."));

    private final State state;
    @SuppressWarnings("unused")
    private final String comment;

    private Workarounds(final State state, final String comment) {
        this.state = state;
        this.comment = comment;
    }

    /**
     * @return {@code true} if the current workaround is active
     */
    public boolean isActive() {
        return this.state == State.ACTIVE;
    }

    enum State {
        ACTIVE, DISABLED;
    }

    private static String description(final String... line) {
        return String.join("\n", Arrays.asList(line));
    }
}
