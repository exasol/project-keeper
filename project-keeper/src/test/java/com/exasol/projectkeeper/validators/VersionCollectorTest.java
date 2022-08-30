package com.exasol.projectkeeper.validators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile.Filename;

class VersionCollectorTest {
    @Test
    void sorted(@TempDir final Path tempDir) throws IOException {
        final Path folder = tempDir.resolve(Path.of("doc", "changes"));
        Files.createDirectories(folder);
        for (final String version : List.of( //
                "1.0.2", //
                "0.3.0", //
                "1.0.10", //
                "1.1.0", //
                "1.0.0")) {
            createChangesFile(folder, version);
        }
        final List<ChangesFile.Filename> expected = Stream.of( //
                "1.1.0", //
                "1.0.10", //
                "1.0.2", //
                "1.0.0", //
                "0.3.0") //
                .map(ChangesFile.Filename::new) //
                .collect(Collectors.toList());
        assertThat(new VersionCollector(tempDir).collectVersions(), equalTo(expected));
    }

    private ChangesFile.Filename createChangesFile(final Path folder, final String version) throws IOException {
        final Filename cfile = new Filename(version);
        Files.createFile(folder.resolve(cfile.filename()));
        return cfile;
    }
}