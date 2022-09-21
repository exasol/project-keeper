package com.exasol.projectkeeper.validators.pom;

import java.util.*;

class XmlPattern {
    static final String NL = System.lineSeparator();

    public static XmlPattern element(final String name) {
        return element(name, "");
    }

    public static XmlPattern element(final String name, final String text) {
        return new XmlPattern(name, text);
    }

    private final List<XmlPattern> children = new ArrayList<>();
    private final String name;
    private final String text;

    XmlPattern(final String name, final String text) {
        this.name = name;
        this.text = text;
    }

    public XmlPattern children(final XmlPattern... children) {
        this.children.addAll(Arrays.asList(children));
        return this;
    }

    public String build() {
        final StringBuilder sb = new StringBuilder("<" + this.name + ">" + this.text);
        if (!this.children.isEmpty()) {
            sb.append(NL);
            for (final XmlPattern c : this.children) {
                sb.append("        " + c.build() + NL);
            }
            sb.append("    ");
        }
        sb.append("</" + this.name + ">");
        return sb.toString();
    }

}
