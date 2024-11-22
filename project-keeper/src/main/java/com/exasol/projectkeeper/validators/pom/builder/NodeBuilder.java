package com.exasol.projectkeeper.validators.pom.builder;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Builder for {@link org.w3c.dom.Node}
 */
public interface NodeBuilder {
    /**
     * Create a new node.
     * 
     * @param document document to use for building the Node
     * @return new instance of a {@link org.w3c.dom.Node}
     */
    public Node build(Document document);

    /**
     * Create a new document builder.
     * 
     * @return New instance of {@link DocumentBuilder}
     */
    public static DocumentBuilder document() {
        return new DocumentBuilder();
    }

    /**
     * Create a new element builder.
     * 
     * @param name name of the element to be built by this builder
     * @return New instance of {@link ElementBuilder}
     */
    public static ElementBuilder element(final String name) {
        return element(null, name);
    }

    /**
     * Create a new element builder.
     * 
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
     * Create builder for a text node.
     *
     * @param text text content of the Node
     * @return New instance of {@link NodeBuilder}
     */
    public static NodeBuilder textNode(final String text) {
        return document -> document.createTextNode(text);
    }

    /**
     * Create builder for a comment node.
     *
     * @param text textual content of the comment
     * @return New instance of {@link NodeBuilder}
     */
    public static NodeBuilder comment(final String text) {
        return document -> document.createComment(text);
    }
}
