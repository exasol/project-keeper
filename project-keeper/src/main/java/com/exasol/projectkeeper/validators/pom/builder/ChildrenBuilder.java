package com.exasol.projectkeeper.validators.pom.builder;

import static com.exasol.projectkeeper.validators.pom.builder.NodeBuilder.element;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for XML nodes with children.
 * 
 * @param <T> child element builder
 */
public abstract class ChildrenBuilder<T extends ChildrenBuilder<T>> {

    /** Create a new instance */
    protected ChildrenBuilder() {
        // Empty constructor required by javadoc
    }

    /**
     * children builders of the current builder
     */
    protected final List<NodeBuilder> children = new ArrayList<>();

    /**
     * Add a child element to the current element
     *
     * @param name name of the child
     * @param text text content of the child
     * @return this for fluent programming
     */
    public T child(final String name, final String text) {
        return child(element(name).text(text));
    }

    /**
     * Add a child element to the current element
     *
     * @param child child element
     * @return this for fluent programming
     */
    public T child(final NodeBuilder child) {
        this.children.add(child);
        return getThis();
    }

    /**
     * Add child only if child != null
     *
     * @param child child element
     * @return this for fluent programming
     */
    public T nullableChild(final NodeBuilder child) {
        if (child != null) {
            this.children.add(child);
        }
        return getThis();
    }

    /**
     * Add children.
     * 
     * @param children children to add to the current element
     * @return this for fluent programming
     */
    public T children(final List<NodeBuilder> children) {
        this.children.addAll(children);
        return getThis();
    }

    /**
     * Get typed this pointer.
     * 
     * @return instance of the current object to support generics
     */
    protected abstract T getThis();
}
