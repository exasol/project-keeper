package com.exasol.projectkeeper;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;

/**
 * Helper class for XPath evaluation.
 */
public class XPathErrorHanlingWrapper {
    private static final XPathFactory X_PATH_FACTORY = XPathFactory.newInstance();

    private XPathErrorHanlingWrapper() {
        // empty on purpose
    }

    /**
     * Evaluate an XPath on a given document node.
     * 
     * @param current node to use a current node in XPath
     * @param xPath   xPath String
     * @return evaluated node
     */
    public static Node runXpath(final Node current, final String xPath) {
        try {
            return (Node) X_PATH_FACTORY.newXPath().compile(xPath).evaluate(current, XPathConstants.NODE);
        } catch (final XPathExpressionException exception) {
            throw new IllegalStateException("F-PK-9 Internal error. Please open a ticket.", exception);
        }
    }
}
