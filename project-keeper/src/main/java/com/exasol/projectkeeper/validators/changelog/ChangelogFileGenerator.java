package com.exasol.projectkeeper.validators.changelog;

import java.util.List;

import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile.Filename;

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

    String generate(final List<ChangesFile.Filename> filenames) {
        this.templateBuilder.append("# Changes" + NL + NL);
        for (final Filename file : filenames) {
            this.templateBuilder.append("* [" + file.version() + "](" + file.filename() + ")" + NL);
        }
        return this.templateBuilder.toString();
    }
}
