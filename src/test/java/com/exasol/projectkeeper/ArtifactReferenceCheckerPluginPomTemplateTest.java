package com.exasol.projectkeeper;

import com.exasol.projectkeeper.pom.plugin.AbstractMavenPluginPomTemplateTest;
import com.exasol.projectkeeper.pom.plugin.ArtifactReferenceCheckerPluginPomTemplate;

class ArtifactReferenceCheckerPluginPomTemplateTest extends AbstractMavenPluginPomTemplateTest {

    ArtifactReferenceCheckerPluginPomTemplateTest() {
        super(new ArtifactReferenceCheckerPluginPomTemplate());
    }
}