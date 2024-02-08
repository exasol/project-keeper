package com.exasol.projectkeeper.validators.changelog;

import java.util.List;

import com.exasol.projectkeeper.validators.changesfile.ChangesFileName;

/**
 * This class generates the content for the changelog file.
 */
class ChangelogFileGenerator {
    private static final String NL = System.lineSeparator();

    /**
     * Generate the content for the changelog file.
     *
     * @param versions list of project versions
     * @return content for the changelog file
     */
    final StringBuilder templateBuilder = new StringBuilder();

    String generate(final List<ChangesFileName> filenames) {
        this.templateBuilder.append("# Changes" + NL + NL);
        for (final ChangesFileName file : filenames) {
            this.templateBuilder.append("* [" + file.version() + "](" + file.filename() + ")" + NL);
        }
        return this.templateBuilder.toString();
    }
}
