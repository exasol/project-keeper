package com.exasol.projectkeeper.pom.plugin;

import static com.exasol.xpath.XPathErrorHanlingWrapper.runXPath;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.exasol.projectkeeper.pom.PomValidator;

/**
 * Abstract basis for {@link PomValidator}s.
 */
public class AbstractPomValidator {
    void createObjectPathIfNotExists(final Node root, final List<String> objects) {
        for (int pathLength = 0; pathLength < objects.size(); pathLength++) {
            final String parentXPath = String.join("/", objects.subList(0, pathLength));
            createObjectIfNotExists(root, parentXPath, objects.get(pathLength));
        }
    }

    private void createObjectIfNotExists(final Node root, final String parentXPath, final String objectName) {
        final Node parent = parentXPath.isEmpty() ? root : runXPath(root, parentXPath);
        if (runXPath(parent, objectName) == null) {
            final Element newNode = root.getOwnerDocument().createElement(objectName);
            parent.appendChild(newNode);
        }
    }
}
