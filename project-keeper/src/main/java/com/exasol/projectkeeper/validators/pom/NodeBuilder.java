package com.exasol.projectkeeper.validators.pom;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Builder for {@link w3c.dom.Node}
 */
public interface NodeBuilder {
    /**
     * @return new instance of a {@link w3c.dom.Node}
     */
    public Node build(Document document);

    /**
     * @param name name of the element to be built by this builder
     * @return New instance of {@link ElementBuilder}
     */
    public static ElementBuilder element(final String name) {
        return element(null, name);
    }

    /**
     * @param namespace XML namespace of the element
     * @param name      name of the element to be built by this builder
     * @return New instance of {@link ElementBuilder}
     */
    public static ElementBuilder element(final String namespace, final String name) {
        return new ElementBuilder(namespace, name);
    }

    /**
     * Create builder for plain, already rendered XML Node
     *
     * @param node {@link org.w3c.dom.Node}
     * @return New instance of {@link NodeBuilder}
     */
    public static NodeBuilder plainNode(final Node node) {
        return document -> {
            document.adoptNode(node);
            return node;
        };
    }

    /**
     * @param text text content of the Node
     * @return New instance of {@link NodeBuilder}
     */
    public static NodeBuilder textNode(final String text) {
        return document -> document.createTextNode(text);
    }
}
