package com.exasol.projectkeeper;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a validation phase, can supply a provision for the next validation phase and a list of validators to be
 * applied within the current phase.
 */
public class ValidationPhase {

    /**
     * Create a new validation phase.
     * 
     * @param validators list of validators
     * @return new instance of {@link ValidationPhase} with provision {@code null}.
     */
    public static ValidationPhase from(final Validator... validators) {
        return new ValidationPhase(null, Arrays.asList(validators));
    }

    private final Provision provision;
    private final List<Validator> validators;

    /**
     * Create a new instance of {@link ValidationPhase}.
     *
     * @param provision  created during the current validation phase
     * @param validators list of validators to be applied within the current validation phase
     */
    public ValidationPhase(final Provision provision, final List<Validator> validators) {
        this.provision = provision;
        this.validators = validators;
    }

    /**
     * Get validators.
     * 
     * @return list of validators
     */
    public List<Validator> validators() {
        return this.validators;
    }

    /**
     * Get provision.
     * 
     * @return provision created during the current validation phase
     */
    public Provision provision() {
        return this.provision;
    }

    /**
     * Represents additional achievements or state created by a phase to be provided to the next validation phase.
     */
    public static class Provision {
        private final String projectVersion;

        /**
         * Set project version.
         * 
         * @param projectVersion version of the current project kept by project-keeper detected by
         *                       {@link ProjectVersionDetector}
         */
        public Provision(final String projectVersion) {
            this.projectVersion = projectVersion;
        }

        /**
         * Get project version.
         * 
         * @return version of the current project
         */
        public String projectVersion() {
            return this.projectVersion;
        }
    }
}
