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
// [impl->dsn~verify-own-version~1]
public final class Version implements Comparable<Version> {

    /**
     * Regular expression pattern in order to verify version number format.
     *
     * <p>
     * Only used internally and in tests.
     * </p>
     */
    public static final Pattern PATTERN = Pattern.compile("[0-9]+(\\.[0-9]+)*+");

    private static final int LESS = -1;
    private static final int EQUAL = 0;
    private static final int GREATER = 1;

    private final String raw;
    private final int[] items;

    /**
     * @param version string representation of version number.
     * @throws UnsupportedVersionFormatException if string does not match {@link #PATTERN}.
     */
    public Version(final String version) throws UnsupportedVersionFormatException {
        this.raw = version;
        this.items = parseVersion(version);
    }

    private static int[] parseVersion(final String v) throws UnsupportedVersionFormatException {
        if (!PATTERN.matcher(v).matches()) {
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
