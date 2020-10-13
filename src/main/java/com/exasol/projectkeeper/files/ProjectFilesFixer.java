package com.exasol.projectkeeper.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

import com.exasol.projectkeeper.Module;

/**
 * This class fixes the project's file structure.
 */
public class ProjectFilesFixer extends AbstractProjectFilesWalker {
    private final Log log;

    /**
     * Crate a new instance of {@link ProjectFilesFixer}.
     * 
     * @param log logger
     */
    public ProjectFilesFixer(final Log log) {
        super();
        this.log = log;
    }

    /**
     * Fix the file structure of the project.
     * 
     * @param projectDirectory project's base directory
     * @param enabledModules   list of enabled modules
     * @throws MojoFailureException in case fixing failed
     */
    public void fixProjectStructure(final File projectDirectory, final Collection<Module> enabledModules)
            throws MojoFailureException {
        final FixerTemplateVisitor fixerVisitor = new FixerTemplateVisitor(this.log);
        super.run(fixerVisitor, projectDirectory, enabledModules);
        if (fixerVisitor.hadErrors()) {
            throw new MojoFailureException("E-PK-5 Failed to fix repository file structure. See previous errors.");
        }
    }

    private static class FixerTemplateVisitor implements ProjectFileTemplateVisitor {
        private final Log log;
        private boolean hadErrors = false;

        private FixerTemplateVisitor(final Log log) {
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
