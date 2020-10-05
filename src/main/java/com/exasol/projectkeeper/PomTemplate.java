package com.exasol.projectkeeper;

import org.w3c.dom.Document;

/**
 * Interface for Pom file validation templates.
 * <p>
 * Register your instances of this interface to {@link PomFileTemplateRunner#TEMPLATES}.
 * </p>
 */
public interface PomTemplate {

    /**
     * Run the template validation or fixing.s
     * 
     * @param pom     pom document
     * @param runMode mode (verify / fix)
     * @throws PomTemplateValidationException if validation fails
     */
    public void run(final Document pom, final RunMode runMode) throws PomTemplateValidationException;

    /**
     * Get the module this template belongs to.
     *
     * @return module this template belongs to
     */
    public String getModule();

    /**
     * Run modes for {@link PomTemplate}.
     */
    public enum RunMode {
        VERIFY, FIX
    }
}
