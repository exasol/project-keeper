package com.exasol.projectkeeper.validators.release.github;

/**
 * This enum represents the state of a GitHub issue.
 */
public enum IssueState {
    /** The issue is open. */
    OPEN,
    /** The issue is closed. */
    CLOSED,
    /** The issue does not exist. */
    MISSING;
}
