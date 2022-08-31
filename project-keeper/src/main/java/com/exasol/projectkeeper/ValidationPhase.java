package com.exasol.projectkeeper;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a validation phase, can supply a list of validators and
 */
public class ValidationPhase {

    /**
     * @param validators validator to be returned by this phase
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
     * @param validatorSupplier supplier for validators
     */
    public ValidationPhase(final Provision provision, final List<Validator> validators) {
        this.provision = provision;
        this.validators = validators;
    }

    /**
     * @return list of validators
     */
    public List<Validator> validators() {
        return this.validators;
    }

    public Provision provision() {
        return this.provision;
    }

    /**
     * Represents additional achievements or state created by a phase to be provided to the next validation phase.
     */
    public static class Provision {
        private final String projectVersion;

        /**
         * @param projectVersion version of the current project kept by project-keeper detected by
         *                       {@link ProjectVersionDetector}
         */
        public Provision(final String projectVersion) {
            this.projectVersion = projectVersion;
        }

        /**
         *
         * @return version of the current project
         */
        public String projectVersion() {
            return this.projectVersion;
        }
    }
}
