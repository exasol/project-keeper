package com.exasol.projectkeeper.validators.changesfile;

/**
 * An issue marked as fixed in a {@link ChangesFileSection}.
 * 
 * @param issueNumber GitHub issue number
 * @param description description for the issue in the changes file
 */
public record FixedIssue(int issueNumber, String description) {

}
