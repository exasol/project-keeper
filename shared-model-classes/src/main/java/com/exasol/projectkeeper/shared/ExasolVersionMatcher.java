package com.exasol.projectkeeper.shared;

import java.util.regex.Pattern;

/**
 * This class matches version numbers in the format used for Exasol open-source projects (major.minor.fix).
 */
public class ExasolVersionMatcher {
    /** Suffix for snapshot versions. */
    public static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";
    private static final Pattern EXASOL_VERSION_PATTERN = Pattern.compile("\\d+\\.\\d+\\.\\d+");

    /**
     * Match version numbers in the format used for Exasol open-source projects (major.minor.fix).
     * 
     * @param tag string to match
     * @return {@code true} if string matches the format
     */
    public boolean isExasolStyleVersion(final String tag) {
        return EXASOL_VERSION_PATTERN.matcher(tag).matches();
    }

    /**
     * Check if a given version string is a snapshot version.
     * 
     * @param version version string
     * @return {@code true if it's a snapshot version}
     */
    public boolean isSnapshotVersion(final String version) {
        return version.endsWith(SNAPSHOT_SUFFIX);
    }
}
