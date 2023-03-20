package com.exasol.projectkeeper.validators.workarounds;

import com.exasol.projectkeeper.validators.dependencies.renderer.AlternatingDependenciesWorkaraound;

/**
 * This class serves as a marker to easily find all places affected by a particular workaround.
 */
public interface Workaround {

    /** Workaround for contents of file dependencies.md reported with issue #436 **/
    public static Workaround ALTERNATING_DEPENDENCIES = new AlternatingDependenciesWorkaraound();

    /**
     * @param input string to modify
     * @return string as modified by the workaround
     */
    public String apply(String input);
}
