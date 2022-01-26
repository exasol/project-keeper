package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.ProjectKeeperModule.*;
import static com.exasol.projectkeeper.validators.files.FileTemplate.TemplateType.REQUIRE_EXACT;
import static com.exasol.projectkeeper.validators.files.FileTemplate.TemplateType.REQUIRE_EXIST;

import java.util.List;

class FileTemplates {
    static final List<FileTemplate> FILE_TEMPLATES = List.of(//
            // default
            FileTemplate.of(".settings/org.eclipse.jdt.ui.prefs", DEFAULT, REQUIRE_EXACT), //
            FileTemplate.of(".settings/org.eclipse.jdt.core.prefs", DEFAULT, REQUIRE_EXACT), //
            FileTemplate.of("src/test/resources/logging.properties", DEFAULT, REQUIRE_EXACT), //
            FileTemplate.of("versionsMavenPluginRules.xml", DEFAULT, REQUIRE_EXACT), //
            FileTemplate.of(".github/workflows/broken_links_checker.yml", DEFAULT, REQUIRE_EXACT), //
            FileTemplate.of(".github/workflows/ci-build-next-java.yml", DEFAULT, REQUIRE_EXACT), //
            FileTemplate.of(".github/workflows/dependencies_check.yml", DEFAULT, REQUIRE_EXACT), //
            FileTemplate.of(".github/workflows/release_droid_prepare_original_checksum.yml", DEFAULT, REQUIRE_EXACT), //
            FileTemplate.of(".github/workflows/release_droid_print_quick_checksum.yml", DEFAULT, REQUIRE_EXACT), //
            FileTemplate.of(".github/workflows/release_droid_upload_github_release_assets.yml", DEFAULT, REQUIRE_EXACT), //
            FileTemplate.of(".github/workflows/ci-build.yml", DEFAULT, REQUIRE_EXIST), //
            // jar_artifact
            FileTemplate.of("src/assembly/all-dependencies.xml", JAR_ARTIFACT, REQUIRE_EXACT), //
            // lombok
            FileTemplate.of("lombok.config", LOMBOK, REQUIRE_EXACT), //
            // maven_central
            FileTemplate.of(".github/workflows/release_droid_release_on_maven_central.yml", MAVEN_CENTRAL,
                    REQUIRE_EXACT)//
    );

    private FileTemplates() {
        //
    }
}
