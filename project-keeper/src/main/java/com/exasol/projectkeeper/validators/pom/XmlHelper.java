package com.exasol.projectkeeper.validators.pom;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Class with XML helper functions.
 */
public class XmlHelper {
    private XmlHelper() {
        // static class
    }

    /**
     * Add a tag with text content to a given parent.
     * 
     * @param target  XML node to add the tag to
     * @param tagName tag name
     * @param value   text content
     */
    public static void addTextElement(final Node target, final String tagName, final String value) {
        final Element node = target.getOwnerDocument().createElement(tagName);
        node.setTextContent(value);
        target.appendChild(node);
    }
}
