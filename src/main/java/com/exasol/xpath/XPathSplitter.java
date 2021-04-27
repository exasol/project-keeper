package com.exasol.xpath;

import java.util.ArrayList;
import java.util.List;

/**
 * This class splits xPath expressions at the root level slashes.
 */
public class XPathSplitter {
    private XPathSplitter() {
        // empty on purpose
    }

    /**
     * Split the xPath expression at the root level slashes.
     * 
     * @param xPath xPath expression
     * @return fragments
     */
    public static List<String> split(final String xPath) {
        var nestLevel = 0;
        final List<String> fragments = new ArrayList<>();
        var fragmentBuilder = new StringBuilder();
        for (var position = 0; position < xPath.length(); position++) {
            final var currentChar = xPath.charAt(position);
            if (currentChar == '[') {
                nestLevel++;
                fragmentBuilder.append(currentChar);
            } else if (currentChar == ']') {
                nestLevel--;
                fragmentBuilder.append(currentChar);
            } else if (currentChar == '/' && position != 0 && nestLevel == 0) {
                fragments.add(fragmentBuilder.toString());
                fragmentBuilder = new StringBuilder();
            } else {
                fragmentBuilder.append(currentChar);
            }
        }
        final var fragment = fragmentBuilder.toString();
        if (!fragment.isEmpty()) {
            fragments.add(fragment);
        }
        return fragments;
    }
}
