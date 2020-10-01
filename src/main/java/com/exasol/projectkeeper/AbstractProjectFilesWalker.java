package com.exasol.projectkeeper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ScanResult;

/**
 * Abstract basis classes that validate the project files.
 * <p>
 * This class walks the project templates, and calls a {@link ProjectFileTemplateVisitor} for each template file.
 * </p>
 */
public abstract class AbstractProjectFilesWalker {
    private static final Pattern TEMPLATE_NAME_PATTERN = Pattern
            .compile(".*\\/templates\\/([^\\/]+)\\/([^\\/]+)\\/(.*)");

    /**
     * Run the template walking.
     * 
     * @param visitor          visitor to be called for each template
     * @param projectDirectory project's root
     * @param enabledModules   list of enabled modules
     */
    protected void run(final ProjectFileTemplateVisitor visitor, final File projectDirectory,
            final List<String> enabledModules) {
        try (final ScanResult scanResult = new ClassGraph().acceptPaths("templates/").scan()) {
            scanResult.getAllResources().forEach(resource -> {
                walkFile(visitor, projectDirectory, enabledModules, resource);
            });
        }
    }

    private void walkFile(final ProjectFileTemplateVisitor visitor, final File projectDirectory,
            final List<String> enabledModules, final Resource resource) {
        final String resourceName = resource.getURI().toString();
        final Matcher matcher = TEMPLATE_NAME_PATTERN.matcher(resourceName);
        if (!matcher.matches()) {
            throw new IllegalStateException("F-PK-1 Template name had invalid format. Please open an issue.");
        }
        final String module = matcher.group(1);
        final TemplateType templateType = TemplateType.fromString(matcher.group(2));
        final String fileName = matcher.group(3);
        if (enabledModules.contains(module)) {
            invokeVisitor(visitor, projectDirectory, resource, templateType, fileName);
        }
    }

    private void invokeVisitor(final ProjectFileTemplateVisitor visitor, final File projectDirectory,
            final Resource resource, final TemplateType templateType, final String fileName) {
        try (final InputStream templateStream = resource.open()) {
            final File projectFile = projectDirectory.toPath().resolve(fileName).toFile();
            visitor.visit(fileName, projectFile, templateStream, templateType);
        } catch (final IOException exception) {
            throw new IllegalStateException("F-PK-2 Failed to open template file. Please open an issue.", exception);
        }
    }

    public enum TemplateType {
        REQUIRE_EXACT, REQUIRE_EXIST;

        private static TemplateType fromString(final String templateTypeString) {
            switch (templateTypeString) {
            case "require_exact":
                return TemplateType.REQUIRE_EXACT;
            case "require_exist":
                return TemplateType.REQUIRE_EXIST;
            default:
                throw new IllegalArgumentException(
                        "F-PK-3 Unknown template type " + templateTypeString + ". Please open an issue.");
            }
        }
    }

    /**
     * Interface for the template visitors.
     */
    protected interface ProjectFileTemplateVisitor {

        /**
         * Visit a file, declared in the template directories.
         *
         * @param fileName     name of the file (without prefix; for logging)
         * @param projectFile  file in the real project (may not exist)
         * @param template     {@link InputStream} with the contents of the template file
         * @param templateType type of the template
         */
        public void visit(String fileName, File projectFile, InputStream template, TemplateType templateType);
    }
}