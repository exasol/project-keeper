package com.exasol.projectkeeper.validators.pom.builder;

import static com.exasol.projectkeeper.validators.pom.builder.NodeBuilder.textNode;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;

/**
 * Builder for DOM Elements
 */
public class ElementBuilder extends ChildrenBuilder<ElementBuilder> implements NodeBuilder {

    private final String namespace;
    private final String name;
    private final List<Attribute> attributes = new ArrayList<>();

    ElementBuilder(final String namespace, final String name) {
        this.namespace = namespace;
        this.name = name;
    }

    /**
     * Add a text node as child to current element.
     *
     * @param text content of text node
     * @return this for fluent programming
     */
    public ElementBuilder text(final String text) {
        return child(textNode(text));
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

    @Override
    protected ElementBuilder getThis() {
        return this;
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
