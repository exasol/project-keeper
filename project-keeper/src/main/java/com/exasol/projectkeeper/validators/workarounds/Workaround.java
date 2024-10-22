package com.exasol.projectkeeper.validators.workarounds;

/**
 * This class serves as a marker to easily find all places affected by a particular workaround.
 */
public interface Workaround {

    /**
     * Apply this workaround.
     * 
     * @param input string to modify
     * @return string as modified by the workaround
     */
    public String apply(String input);
}
