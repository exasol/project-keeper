package com.exasol.projectkeeper.validators.pom;

import static com.exasol.xpath.XPathErrorHandlingWrapper.runXPath;

import java.util.List;

import org.w3c.dom.Node;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.*;

/**
 * Abstract basis for {@link PomValidator}s.
 */
public class AbstractPomValidator {
    protected void createObjectPathIfNotExists(final Node root, final List<String> objects) {
        for (var pathLength = 0; pathLength < objects.size(); pathLength++) {
            final var parentXPath = String.join("/", objects.subList(0, pathLength));
            createObjectIfNotExists(root, parentXPath, objects.get(pathLength));
        }
    }

    private void createObjectIfNotExists(final Node root, final String parentXPath, final String objectName) {
        final Node parent = parentXPath.isEmpty() ? root : runXPath(root, parentXPath);
        if (runXPath(parent, objectName) == null) {
            final var newNode = root.getOwnerDocument().createElement(objectName);
            parent.appendChild(newNode);
        }
    }

    protected boolean isXmlEqual(final Node property1, final Node property2) {
        final Diff comparison = DiffBuilder.compare(property1).withTest(property2).ignoreComments().ignoreWhitespace()
                .checkForSimilar().withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText)).build();
        return !comparison.hasDifferences();
    }
}
