package com.exasol.projectkeeper.validators.dependencies.renderer;

import java.util.HashMap;
import java.util.Map;

class MarkdownReferenceBuilder {
    private final Map<String, String> references = new HashMap<>();
    private int referenceCounter = 0;

    /**
     * Get a reference for a url.
     * <p>
     * You can use the reference instead of the link like {@code [link name][reference]}.
     * </p>
     * 
     * @param name name of the link (used for building the reference)
     * @param url  url the reference should point to
     * @return reference
     */
    public String getReferenceForUrl(final String name, final String url) {
        final var referenceName = String.valueOf(this.referenceCounter);
        this.referenceCounter++;
        if (!this.references.containsKey(url)) {
            this.references.put(url, referenceName);
            return referenceName;
        } else {
            return this.references.get(url);
        }
    }

    /**
     * Get the mapping for the references in markdown format
     * 
     * @return string with the reference mapping. Example: {@code [0]: https://exasol.com}
     */
    public String getReferences() {
        final var stringBuilder = new StringBuilder();
        for (final Map.Entry<String, String> reference : this.references.entrySet()) {
            stringBuilder.append("[").append(reference.getValue()).append("]: ").append(reference.getKey())
                    .append("\n");
        }
        return stringBuilder.toString();
    }
}
