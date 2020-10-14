package com.exasol.projectkeeper.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.maven.plugin.logging.Log;

import com.exasol.projectkeeper.ProjectKeeperModule;

/**
 * Validator for the projects file structure.
 */
public class ProjectFilesValidator extends AbstractProjectFilesWalker {
    private final Log log;

    /**
     * Crate a new instance of {@link ProjectFilesValidator}.
     *
     * @param log logger
     */
    public ProjectFilesValidator(final Log log) {
        this.log = log;
    }

    /**
     * Validate the file structure of the project.
     * 
     * @param projectDirectory project's base directory
     * @param enabledModules   list of enabled modules
     * @return {@code true} if structure is valid
     */
    public boolean validateProjectStructure(final File projectDirectory,
            final Collection<ProjectKeeperModule> enabledModules) {
        final ValidationTemplateVisitor validationVisitor = new ValidationTemplateVisitor(this.log);
        super.run(validationVisitor, projectDirectory, enabledModules);
        return !validationVisitor.hadErrors();
    }

    private static class ValidationTemplateVisitor implements ProjectFileTemplateVisitor {
        private static final InputStreamComparator COMPARATOR = new InputStreamComparator();
        private final Log log;
        boolean hadErrors = false;

        private ValidationTemplateVisitor(final Log log) {
            this.log = log;
        }

        @Override
        public void visit(final String fileName, final File projectFile, final InputStream template,
                final TemplateType templateType) {
            if (!projectFile.exists()) {
                this.hadErrors = true;
                this.log.error("Missing required: " + fileName);
                return;
            }
            if (templateType.equals(TemplateType.REQUIRE_EXACT)) {
                validateContent(fileName, projectFile, template);
            }
        }

        private void validateContent(final String fileName, final File projectFile, final InputStream template) {
            try (final FileInputStream actualInputStream = new FileInputStream(projectFile)) {
                if (!COMPARATOR.areStreamsEqual(actualInputStream, template)) {
                    this.hadErrors = true;
                    this.log.error("Outdated content: " + fileName);
                }
            } catch (final IOException exception) {
                this.hadErrors = true;
                this.log.error("Failed to open " + fileName + "for read. Cause: " + exception.getMessage());
            }
        }

        public boolean hadErrors() {
            return this.hadErrors;
        }
    }
}
