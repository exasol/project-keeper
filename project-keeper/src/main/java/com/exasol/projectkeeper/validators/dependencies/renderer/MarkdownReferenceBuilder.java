package com.exasol.projectkeeper.validators.dependencies.renderer;

import java.util.LinkedHashMap;
import java.util.Map;

class MarkdownReferenceBuilder {
    private final Map<String, Integer> references = new LinkedHashMap<>();
    private int referenceCounter = 0;

    /**
     * Get a reference for a url.
     * <p>
     * You can use the reference instead of the link like {@code [link name][reference]}.
     * </p>
     *
     * @param url url the reference should point to
     * @return reference
     */
    public String getReferenceForUrl(final String url) {
        if (!this.references.containsKey(url)) {
            this.references.put(url, this.referenceCounter);
            final String key = String.valueOf(this.referenceCounter);
            this.referenceCounter++;
            return key;
        } else {
            return String.valueOf(this.references.get(url));
        }
    }

    /**
     * Get the mapping for the references in markdown format
     * 
     * @return string with the reference mapping. Example: {@code [0]: https://exasol.com}
     */
    public String getReferences() {
        final var stringBuilder = new StringBuilder();
        for (final Map.Entry<String, Integer> reference : this.references.entrySet()) {
            stringBuilder.append("[").append(reference.getValue()).append("]: ").append(reference.getKey())
                    .append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
}
