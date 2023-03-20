package com.exasol.projectkeeper.validators.dependencies.renderer;

/**
 * For a few particular licenses issue #436 describes inconsistent results in file dependencies.md. Sometimes the
 * results will use name "The Apache Software License" while in other cases the name will be "Apache License".
 *
 * <p>
 * Even careful and extensive investigations did not identify the root cause of this problem.
 *
 * <p>
 * The current solution is a workaround simply replacing the one string by the other to have at least consistent and
 * stable results.
 *
 * <p>
 * This class serves as a marker to easily find all places affected by this issue and the workaround.
 */
public enum Workarounds {

    ALTERNATIVING_DEPENDENCIES(State.ACTIVE, "");

    private final State state;
    @SuppressWarnings("unused")
    private final String comment;

    private Workarounds(final State state, final String comment) {
        this.state = state;
        this.comment = comment;
    }

    /**
     * @return {@link true} if the current workaround is active
     */
    public boolean isActive() {
        return this.state == State.ACTIVE;
    }

    enum State {
        ACTIVE, DISABLED;
    }
}
