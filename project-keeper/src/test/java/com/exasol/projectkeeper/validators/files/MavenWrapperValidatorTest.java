package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.validationErrorMessages;
import static java.nio.file.attribute.PosixFilePermission.OWNER_READ;
import static java.nio.file.attribute.PosixFilePermission.OWNER_WRITE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.validators.FindingFixHelper;

class MavenWrapperValidatorTest {
    @TempDir
    Path projectDirectory;

    @Test
    // [utest->dsn~maven-wrapper~1]
    void fixesAllMavenWrapperFiles() throws IOException {
        final MavenWrapperValidator validator = validator();

        fixAllFindings(validator);

        assertAll(() -> assertThat(this.projectDirectory.resolve("mvnw").toFile(), anExistingFile()),
                () -> assertThat(this.projectDirectory.resolve("mvnw.cmd").toFile(), anExistingFile()),
                () -> assertThat(this.projectDirectory.resolve(".mvn/wrapper/maven-wrapper.properties").toFile(),
                        anExistingFile()),
                () -> assertThat(Files.readString(this.projectDirectory.resolve("mvnw")), containsString("Maven Wrapper")),
                () -> assertThat(Files.readString(this.projectDirectory.resolve("mvnw.cmd")), containsString("Maven Wrapper")),
                () -> assertThat(Files.readString(this.projectDirectory.resolve(".mvn/wrapper/maven-wrapper.properties")),
                        containsString("distributionType=only-script")));
    }

    @Test
    @EnabledOnOs({ OS.LINUX, OS.MAC })
    void fixesMissingExecutablePermissionWithoutRewritingContent() throws IOException {
        final MavenWrapperValidator validator = validator();
        fixAllFindings(validator);
        final Path wrapper = this.projectDirectory.resolve("mvnw");
        final String originalContent = Files.readString(wrapper);
        Files.setPosixFilePermissions(wrapper, Set.of(OWNER_READ, OWNER_WRITE));

        assertThat(validator, validationErrorMessages(contains("E-PK-CORE-215: Missing executable permission: 'mvnw'")));

        fixAllFindings(validator);
        assertAll(() -> assertThat(Files.isExecutable(wrapper), equalTo(true)),
                () -> assertThat(Files.readString(wrapper), equalTo(originalContent)));
    }

    @Test
    void restoresModifiedWrapperContent() throws IOException {
        final MavenWrapperValidator validator = validator();
        fixAllFindings(validator);
        final Path wrapper = this.projectDirectory.resolve("mvnw");
        Files.writeString(wrapper, "modified");

        assertThat(validator, validationErrorMessages(contains("E-PK-CORE-18: Outdated content: 'mvnw'")));

        fixAllFindings(validator);
        assertThat(Files.readString(wrapper), containsString("Maven Wrapper"));
    }

    private MavenWrapperValidator validator() {
        return new MavenWrapperValidator(this.projectDirectory);
    }

    private static void fixAllFindings(final MavenWrapperValidator validator) {
        validator.validate().forEach(FindingFixHelper::fix);
    }
}
