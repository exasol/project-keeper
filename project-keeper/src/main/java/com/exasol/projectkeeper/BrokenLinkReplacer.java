package com.exasol.projectkeeper;

import static java.util.stream.Collectors.toMap;

import java.util.*;
import java.util.Map.Entry;

import com.exasol.errorreporting.ExaError;

/**
 * Some maven projects define invalid / outdated links to their project homepage. Since PK writes theses links to the
 * dependencies.md file each project again contains these broken links. Since that's not desirable and breaks the CI
 * link checker, this class replaces the broken links by working ones.
 * <p>
 * This class has a built in mapping for some common broken links but also allows users to define new link mappings in
 * the pom file.
 * </p>
 */
public class BrokenLinkReplacer {
    private static final String JACOCO_HOMEPAGE = "https://www.eclemma.org/jacoco/index.html";
    private static final Map<String, String> BUILTIN_REPLACEMENTS_1 = Map.of(//
            "http://org.jacoco.agent", JACOCO_HOMEPAGE, //
            "http://org.jacoco.core", JACOCO_HOMEPAGE, //
            "http://jacoco-maven-plugin", JACOCO_HOMEPAGE, //
            "https://eclipse-ee4j.github.io/jaxb-ri/jaxb-runtime-parent/jaxb-runtime",
            "https://eclipse-ee4j.github.io/jaxb-ri/", //
            "https://www.eclipse.org/jgit//org.eclipse.jgit", "https://www.eclipse.org/jgit/", //
            "http://maven.apache.org/maven-project", "http://maven.apache.org/",
            "https://sonatype.github.io/ossindex-maven/ossindex-maven-plugin/",
            "https://sonatype.github.io/ossindex-maven/maven-plugin/", //
            "https://github.com/javaee/jaxb-spec/jaxb-api", "https://github.com/eclipse-ee4j/jaxb-api",
            "https://github.com/exasol/error-code-crawler-maven-plugint",
            "https://github.com/exasol/error-code-crawler-maven-plugin");

    private static final Map<String, String> BUILTIN_REPLACEMENTS_2 = Map.of(
            "https://www.mojohaus.org/flatten-maven-plugin/flatten-maven-plugin",
            "https://www.mojohaus.org/flatten-maven-plugin/", //
            "https://awhitford.github.com/lombok.maven/lombok-maven-plugin/",
            "https://anthonywhitford.com/lombok.maven/lombok-maven-plugin/", //
            "LICENSE-exasol-jdbc.txt", "https://docs.exasol.com/connect_exasol/drivers/jdbc.htm" //
    );
    private final Map<String, String> replacements = new HashMap<>(getBuiltinReplacements());

    private static Map<String, String> getBuiltinReplacements() {
        // The Map.of() function only supports 5 key-value pairs, so we have to split the definition.
        final Map<String, String> builtinReplacements = new HashMap<>();
        builtinReplacements.putAll(BUILTIN_REPLACEMENTS_1);
        builtinReplacements.putAll(BUILTIN_REPLACEMENTS_2);
        return convertKeysToLowerCase(builtinReplacements);
    }

    private static Map<String, String> convertKeysToLowerCase(final Map<String, String> map) {
        return map.entrySet().stream() //
                .collect(toMap(entry -> entry.getKey().toLowerCase(), Entry::getValue));
    }

    /**
     * Create a new instance of {@link BrokenLinkReplacer}.
     * 
     * @param replacementParameters list of replacement parameters in the format {@code original|replacement}
     */
    public BrokenLinkReplacer(final List<String> replacementParameters) {
        readReplacementParameter(replacementParameters);
    }

    private void readReplacementParameter(final List<String> replacementParameters) {
        for (final String replacementParameter : replacementParameters) {
            final String[] parts = replacementParameter.split("\\|");
            checkSyntax(replacementParameter, parts);
            this.replacements.put(parts[0].toLowerCase().trim(), parts[1].trim());
        }
    }

    private void checkSyntax(final String replacementParameter, final String[] parts) {
        if (parts.length != 2) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-55")
                    .message("Invalid link replacement {{link replacement}}. ", replacementParameter)
                    .message(
                            "The link replacement should have the syntax '<linkReplacement>originalLink|replacement</linkReplacement>'.")
                    .mitigation("Change the linkReplacement in you pom.xml file.").toString());
        }
    }

    /**
     * Get an replacement for a link in cast it's known as broken. Otherwise get the original link.
     * 
     * @param originalLink link to check
     * @return original link or replacement
     */
    public String replaceIfBroken(final String originalLink) {
        if (originalLink == null) {
            return null;
        } else {
            return this.replacements.getOrDefault(originalLink.toLowerCase(), originalLink);
        }
    }
}
