package com.exasol.projectkeeper.validators.pom.builder;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import com.exasol.errorreporting.ExaError;

/**
 * Builder for {@link org.w3c.dom.Document}
 */
public class DocumentBuilder extends ChildrenBuilder<DocumentBuilder> {

    DocumentBuilder() {
    }

    /**
     * @return new instance of {@link org.w3c.dom.Document}
     */
    public Document build() {
        final Document document = createDocument();
        for (final NodeBuilder child : this.children) {
            document.appendChild(child.build(document));
        }
        return document;
    }

    private Document createDocument() {
        try {
            final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            document.setXmlVersion("1.0");
            document.setXmlStandalone(false);
            return document;
        } catch (final ParserConfigurationException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-109")
                    .message("Failed to create document.").ticketMitigation().toString(), exception);
        }
    }

    @Override
    protected DocumentBuilder getThis() {
        return this;
    }
}