package com.exasol.projectkeeper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

/**
 * This class fits the projects file structure.
 */
public class ProjectFilesFitter extends AbstractProjectFilesValidator {
    private final Log log;

    /**
     * Crate a new instance of {@link ProjectFilesFitter}.
     * 
     * @param log logger
     */
    public ProjectFilesFitter(final Log log) {
        super();
        this.log = log;
    }

    /**
     * Fit the file structure of the project.
     * 
     * @param projectDir     project's base directory
     * @param enabledModules list of enabled modules
     * @throws MojoFailureException in case fitting failed
     */
    public void fitProjectStructure(final File projectDir, final List<String> enabledModules)
            throws MojoFailureException {
        final FitterTemplateVisitor fitterVisitor = new FitterTemplateVisitor(this.log);
        super.run(fitterVisitor, projectDir, enabledModules);
        if (fitterVisitor.hadErrors()) {
            throw new MojoFailureException("Failed to fit repository file structure. See previous errors.");
        }
    }

    private static class FitterTemplateVisitor implements ProjectFileTemplateVisitor {
        private final Log log;
        private boolean hadErrors = false;

        private FitterTemplateVisitor(final Log log) {
            this.log = log;
        }

        @Override
        public void visit(final String fileName, final File projectFile, final InputStream template,
                final TemplateType templateType) {
            if (!projectFile.exists() || templateType.equals(TemplateType.REQUIRE_EXACT)) {
                writeFile(projectFile, template);
            }
        }

        private void writeFile(final File projectFile, final InputStream template) {
            try {
                projectFile.mkdirs();
                Files.copy(template, projectFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (final IOException exception) {
                this.log.error("Failed to create or replace " + projectFile.getAbsolutePath());
                this.hadErrors = true;
            }
        }

        public boolean hadErrors() {
            return this.hadErrors;
        }
    }
}
