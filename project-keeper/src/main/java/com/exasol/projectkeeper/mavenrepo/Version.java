package com.exasol.projectkeeper.mavenrepo;

import java.util.Arrays;
import java.util.regex.Pattern;

//[impl->dsn~verify-own-version~1]
public class Version implements Comparable<Version> {

    public static Version of(final String string) throws UnsupportedVersionFormatException {
        return new Version(string);
    }

    public static final Pattern PATTERN = Pattern.compile("[0-9]+(\\.[0-9]+)*");
    private static final int LESS = -1;
    private static final int EQUAL = 0;
    private static final int GREATER = 1;

    private final String raw;
    private final int[] items;

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

    public boolean isLessThan(final Version other) {
        return compareTo(other) == LESS;
    }

    public int compareTo(final String other) throws UnsupportedVersionFormatException {
        return compareTo(new Version(other));
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

    public static class UnsupportedVersionFormatException extends Exception {
        private static final long serialVersionUID = 1L;

        public UnsupportedVersionFormatException(final String message) {
            super(message);
        }
    }

    @Override
    public String toString() {
        return this.raw;
    }
}
