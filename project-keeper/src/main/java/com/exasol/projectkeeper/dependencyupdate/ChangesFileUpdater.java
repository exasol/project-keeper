package com.exasol.projectkeeper.dependencyupdate;

import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile.Builder;

class ChangesFileUpdater {
    ChangesFile update(final ChangesFile changesFile) {
        final Builder builder = changesFile.toBuilder();
        update(builder);
        return builder.build();
    }

    private void update(final Builder builder) {

    }
}
