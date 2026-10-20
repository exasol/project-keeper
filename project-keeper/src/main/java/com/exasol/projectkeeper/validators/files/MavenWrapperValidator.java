package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.validators.files.FileTemplate.Validation.REQUIRE_EXACT;
import static com.exasol.projectkeeper.validators.files.RequiredFileValidator.withContentEqualTo;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/** Validates the Maven Wrapper files required before Maven source analysis. */
// [impl->dsn~maven-wrapper~1]
public class MavenWrapperValidator implements Validator {
    private static final List<String> WRAPPER_PATHS = List.of("mvnw", "mvnw.cmd", ".mvn/wrapper/maven-wrapper.properties");
    private final Path projectDirectory;

    /**
     * Create a validator for a project root.
     *
     * @param projectDirectory project root directory
     */
    public MavenWrapperValidator(final Path projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    @Override
    public List<ValidationFinding> validate() {
        return WRAPPER_PATHS.stream()
                .map(this::createTemplate)
                .flatMap(this::validate)
                .toList();
    }

    private Stream<ValidationFinding> validate(final FileTemplate template) {
        return new RequiredFileValidator()
                .validateFile(this.projectDirectory, this.projectDirectory.resolve(template.getPathInProject()),
                        withContentEqualTo(template.getContent()), template.requiresExecutablePermission())
                .stream();
    }

    private FileTemplate createTemplate(final String path) {
        final FileTemplateFromResource template = new FileTemplateFromResource("templates/" + path, path, REQUIRE_EXACT);
        if ("mvnw".equals(path)) {
            template.executable();
        }
        return template;
    }
}
