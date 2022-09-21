package com.exasol.projectkeeper;

/**
 * This class formats a string in the associated press headline style.
 */
public class ApStyleFormatter {

    /**
     * @param string name to be converted
     * @return human readable format for input string
     */
    public static String humanReadable(final String string) {
        return capitalizeApStyle(string.replace("-", " ").replace("_", " "));
    }

    /**
     * Format a string in the associated press headline style.
     *
     * @param string string to format
     * @return formatted string
     */
    public static String capitalizeApStyle(final String string) {
        if ((string == null) || string.isBlank()) {
            return "";
        }
        final String[] parts = string.split(" ");
        boolean isFirst = true;
        final StringBuilder result = new StringBuilder();
        for (final String part : parts) {
            if (!isFirst) {
                result.append(" ");
            }
            if (isFirst || (part.length() > 3)) {
                result.append(capitalizeFirstLetter(part));
            } else {
                result.append(part);
            }
            isFirst = false;
        }
        return result.toString();
    }

    private static String capitalizeFirstLetter(final String string) {
        final var firstLetter = string.substring(0, 1);
        return firstLetter.toUpperCase() + string.substring(1);
    }

    private ApStyleFormatter() {
        // static class
    }
}
