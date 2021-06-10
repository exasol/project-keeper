package com.exasol.projectkeeper.validators.pom;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Document;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;

/**
 * Interface for Pom file validation templates.
 * <p>
 * Register your instances of this interface to {@link PomFileValidator#ALL_VALIDATORS}.
 * </p>
 */
public interface PomValidator {

    /**
     * Run the template validation or fixing.
     * 
     * @param pom             pom document
     * @param enabledModules  list of enabled modules
     * @param findingConsumer Consumer that accepts the {@link ValidationFinding}s. The findings must be fixed in the
     *                        order, they are reported.
     */
    public void validate(final Document pom, final Collection<ProjectKeeperModule> enabledModules,
            Consumer<ValidationFinding> findingConsumer);

    /**
     * Get the module this template belongs to.
     *
     * @return module this template belongs to
     */
    public ProjectKeeperModule getModule();

    /**
     * Get if this validator is excluded by the excluded plugins.
     * 
     * @param excludedPlugins list of excluded plugins (group_id:artifact_id)
     * @return {@code true} if the validator is excluded
     */
    // [impl->dsn~exclduding-mvn-plugins~1]
    public boolean isExcluded(Collection<String> excludedPlugins);
}
