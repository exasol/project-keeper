package com.exasol.projectkeeper.validators.pom;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlHelper {
    private XmlHelper() {
        // static class
    }

    public static void addTextElement(final Node target, final String tagName, final String value) {
        final Element node = target.getOwnerDocument().createElement(tagName);
        node.setTextContent(value);
        target.appendChild(node);
    }
}
