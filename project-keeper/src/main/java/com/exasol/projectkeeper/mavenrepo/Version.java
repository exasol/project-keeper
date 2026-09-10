package com.exasol.projectkeeper.mavenrepo;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Parse a version number and implement Comparable for that.
 *
 * <p>
 * Compared to class {@code org.apache.maven.artifact.versioning.ComparableVersion} from Maven this class supports less
 * features, e.g. only numeric version components but avoids an additional dependency.
 */
// [impl->dsn~verify-own-version~2]
public final class Version implements Comparable<Version> {

    private static final Pattern PATTERN = Pattern.compile("[0-9]+(\\.[0-9]+)*+");

    private static final int LESS = -1;
    private static final int EQUAL = 0;
    private static final int GREATER = 1;

    private final String raw;
    private final int[] items;

    /**
     * Parse the given version string and return a new {@link Version} instance.
     * <p>
     * This method just calls the constructor, but wraps the checked exception into an unchecked one.
     * </p>
     *
     * @param version version string to parse
     * @return new {@link Version} instance
     * @throws IllegalArgumentException if string does not match {@link #PATTERN}.
     */
    public static Version parse(final String version) {
        try {
            return new Version(version);
        } catch (final UnsupportedVersionFormatException exception) {
            throw new IllegalArgumentException("Unsupported version format: " + version, exception);
        }
    }

    /**
     * Check whether a string is a version number supported by this class.
     *
     * @param version version string to validate
     * @return {@code true} if the version consists of one or more numeric components separated by dots
     */
    public static boolean isValidVersion(final String version) {
        return version != null && PATTERN.matcher(version).matches();
    }

    /**
     * Create a new instance.
     *
     * @param version string representation of version number.
     * @throws UnsupportedVersionFormatException if string does not match {@link #PATTERN}.
     */
    public Version(final String version) throws UnsupportedVersionFormatException {
        this.raw = version;
        this.items = parseVersion(version);
    }

    private static int[] parseVersion(final String v) throws UnsupportedVersionFormatException {
        if (!isValidVersion(v)) {
            throw new UnsupportedVersionFormatException(v);
        }
        return Arrays.stream(v.split("\\.")) //
                .mapToInt(Integer::parseInt) //
                .toArray();
    }

    @Override
    public int compareTo(final Version other) {
        for (int i = 0; i < this.items.length; i++) {
            final int result = compare(i, other);
            if (differs(result)) {
                return result;
            }
        }
        if (this.items.length < other.items.length) {
            return LESS;
        }
        return EQUAL;
    }

    /**
     * Check if this version is greater than another version.
     *
     * @param other other version to compare this version to
     * @return {@code true} if this version is greater or equal than the other one
     */
    public boolean isGreaterOrEqualThan(final Version other) {
        return compareTo(other) > LESS;
    }

    private int compare(final int i, final Version other) {
        if (i >= other.items.length) {
            return GREATER;
        }
        return Integer.compare(this.items[i], other.items[i]);
    }

    private boolean differs(final int result) {
        return result != EQUAL;
    }

    /**
     * Thrown if version uses an unsupported format and cannot be parsed and compared to another version.
     */
    public static class UnsupportedVersionFormatException extends Exception {
        private static final long serialVersionUID = 1L;

        /**
         * Create a new instance.
         *
         * @param message detailed message of the exception
         */
        public UnsupportedVersionFormatException(final String message) {
            super(message);
        }
    }

    @Override
    public String toString() {
        return this.raw;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(items);
        result = prime * result + Objects.hash(raw);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Version other = (Version) obj;
        return Objects.equals(raw, other.raw) && Arrays.equals(items, other.items);
    }
}
