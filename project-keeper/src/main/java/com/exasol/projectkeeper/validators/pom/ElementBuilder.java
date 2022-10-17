package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.validators.pom.NodeBuilder.element;
import static com.exasol.projectkeeper.validators.pom.NodeBuilder.textNode;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;

/**
 * Builder for DOM Elements
 */
public class ElementBuilder implements NodeBuilder {

    private final String namespace;
    private final String name;
    private final List<Attribute> attributes = new ArrayList<>();
    private final List<NodeBuilder> children = new ArrayList<>();

    ElementBuilder(final String namespace, final String name) {
        this.namespace = namespace;
        this.name = name;
    }

    /**
     * Add a child element to the current element
     *
     * @param name name of the child
     * @param text text content of the child
     * @return this for fluent programming
     */
    public ElementBuilder child(final String name, final String text) {
        return child(element(name).child(text));
    }

    /**
     * Add a text node as child to current element.
     *
     * @param text content of text node
     * @return this for fluent programming
     */
    public ElementBuilder child(final String text) {
        return child(textNode(text));
    }

    /**
     * Add a child element to the current element
     *
     * @param child child element
     * @return this for fluent programming
     */
    public ElementBuilder child(final NodeBuilder child) {
        this.children.add(child);
        return this;
    }

    /**
     * Add child only if child != null
     *
     * @param child child element
     * @return this for fluent programming
     */
    public ElementBuilder nullableChild(final NodeBuilder child) {
        if (child != null) {
            this.children.add(child);
        }
        return this;
    }

    /**
     * @param children children to add to the current element
     * @return this for fluent programming
     */
    public ElementBuilder children(final List<NodeBuilder> children) {
        this.children.addAll(children);
        return this;
    }

    /**
     * Add an attribute to the current element
     *
     * @param name  name of the new attribute
     * @param value value of the attribute
     * @return this for fluent programming
     */
    public ElementBuilder attribute(final String name, final String value) {
        return attribute(null, name, value);
    }

    /**
     * Add an attribute with namespace to the current element
     *
     * @param namespace XML namespace of the new attribute
     * @param name      name of the new attribute
     * @param value     value of the attribute
     * @return this for fluent programming
     */
    public ElementBuilder attribute(final String namespace, final String name, final String value) {
        this.attributes.add(new Attribute(namespace, name, value));
        return this;
    }

    @Override
    public Node build(final Document document) {
        final Element element = this.namespace == null //
                ? document.createElement(this.name)
                : document.createElementNS(this.namespace, this.name);
        for (final Attribute attr : this.attributes) {
            if (attr.namespace == null) {
                element.setAttribute(attr.name, attr.value);
            } else {
                element.setAttributeNS(attr.namespace, attr.name, attr.value);
            }
        }
        this.children.stream().map(c -> c.build(document)).forEach(element::appendChild);
        return element;
    }

    static class Attribute {
        final String namespace;
        final String name;
        final String value;

        Attribute(final String namespace, final String name, final String value) {
            this.namespace = namespace;
            this.name = name;
            this.value = value;
        }
    }
}
