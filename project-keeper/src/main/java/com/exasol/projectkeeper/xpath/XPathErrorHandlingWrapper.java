package com.exasol.projectkeeper.xpath;

import javax.xml.xpath.*;

import org.w3c.dom.Node;

import com.exasol.errorreporting.ExaError;

/**
 * Helper class for XPath evaluation.
 */
public class XPathErrorHandlingWrapper {
    private static final XPathFactory X_PATH_FACTORY = XPathFactory.newInstance();

    private XPathErrorHandlingWrapper() {
        // empty on purpose
    }

    /**
     * Evaluate an XPath on a given document node.
     * 
     * @param current node to use a current node in XPath
     * @param xPath   xPath String
     * @return evaluated node
     */
    public static Node runXPath(final Node current, final String xPath) {
        try {
            return (Node) X_PATH_FACTORY.newXPath().compile(xPath).evaluate(current, XPathConstants.NODE);
        } catch (final XPathExpressionException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-9").message("Invalid xpath {{xpath}}.", xPath)
                    .ticketMitigation().toString(), exception);
        }
    }
}
