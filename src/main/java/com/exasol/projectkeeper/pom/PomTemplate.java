package com.exasol.projectkeeper.pom;

import java.util.Collection;

import org.w3c.dom.Document;

import com.exasol.projectkeeper.Module;
import com.exasol.projectkeeper.pom.plugin.PomTemplateValidationException;

/**
 * Interface for Pom file validation templates.
 * <p>
 * Register your instances of this interface to {@link PomFileTemplateRunner#TEMPLATES}.
 * </p>
 */
public interface PomTemplate {

    /**
     * Run the template validation or fixing.
     * 
     * @param pom            pom document
     * @param runMode        mode (verify / fix)
     * @param enabledModules list of enabled modules
     * @throws PomTemplateValidationException if validation fails
     */
    public void run(final Document pom, final RunMode runMode, final Collection<Module> enabledModules)
            throws PomTemplateValidationException;

    /**
     * Get the module this template belongs to.
     *
     * @return module this template belongs to
     */
    public Module getModule();

    /**
     * Run modes for {@link PomTemplate}.
     */
    public enum RunMode {
        VERIFY, FIX
    }
}
