package com.exasol.projectkeeper;

import com.exasol.projectkeeper.validators.pom.plugin.AbstractMavenPluginPomValidatorTest;
import com.exasol.projectkeeper.validators.pom.plugin.ArtifactReferenceCheckerPluginPomValidator;

class ArtifactReferenceCheckerPluginPomValidatorTest extends AbstractMavenPluginPomValidatorTest {

    ArtifactReferenceCheckerPluginPomValidatorTest() {
        super(new ArtifactReferenceCheckerPluginPomValidator());
    }
}