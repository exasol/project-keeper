package com.exasol.projectkeeper.dependencyupdate;

import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile.Builder;

class ChangesFileUpdater {
    ChangesFile update(final ChangesFile changesFile) {
        final Builder builder = changesFile.toBuilder();
        // Changes file will be updated in the next PR
        return builder.build();
    }
}
