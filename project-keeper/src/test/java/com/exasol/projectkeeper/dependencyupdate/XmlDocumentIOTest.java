package com.exasol.projectkeeper.dependencyupdate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import javax.xml.parsers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

class XmlDocumentIOTest {

    @TempDir
    Path tempDir;

    @Test
    void read() throws IOException {
        final Path file = tempDir.resolve("file.xml");
        Files.writeString(file, "<root>content</root>");
        final Document document = testee().read(file);
        assertThat(document.getFirstChild().getNodeName(), equalTo("root"));
    }

    @Test
    void write() throws ParserConfigurationException, IOException {
        final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        final Document doc = docBuilder.newDocument();
        doc.appendChild(doc.createElement("root"));
        final Path file = tempDir.resolve("file.xml");
        testee().write(doc, file);
        assertThat(Files.readString(file), equalTo(normalizeLineEndings("""
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <root/>
                """)));
    }

    @Test
    void readWritePreservesComments() throws IOException {
        final Path file = tempDir.resolve("file.xml");
        final String xmlContent = normalizeLineEndings("""
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <!-- comment --><root attr="value">content</root>
                """);
        Files.writeString(file, xmlContent);
        final XmlDocumentIO io = testee();
        io.write(io.read(file), file);
        assertThat(Files.readString(file), equalTo(xmlContent));
    }

    private String normalizeLineEndings(final String content) {
        return content.replace("\n", System.lineSeparator());
    }

    @Test
    void runXPath() throws IOException {
        final Path file = tempDir.resolve("file.xml");
        Files.writeString(file, "<root>content</root>");
        final Document document = testee().read(file);
        assertThat(testee().runXPath(document, "/root").get().getNodeName(), equalTo("root"));
    }

    @Test
    void runXPathNoElementFound() throws IOException {
        final Path file = tempDir.resolve("file.xml");
        Files.writeString(file, "<root>content</root>");
        final Document document = testee().read(file);
        assertThat(testee().runXPath(document, "/wrong").isPresent(), is(false));
    }

    @Test
    void runXPathReturnsFirstMatch() throws IOException {
        final Path file = tempDir.resolve("file.xml");
        Files.writeString(file, """
                <root>
                    <child name="child1"/>
                    <child name="child2"/>
                </root>
                """);
        final Document document = testee().read(file);
        final Optional<Node> result = testee().runXPath(document, "/root/child");
        assertThat(result.get().getAttributes().getNamedItem("name").getNodeValue(), equalTo("child1"));
    }

    private XmlDocumentIO testee() {
        return new XmlDocumentIO();
    }
}
