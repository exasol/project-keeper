package com.exasol.projectkeeper.validators.changelog;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.io.StringReader;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelBuildingException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.jupiter.api.Test;

class MavenModelReaderTest {

    @Test
    void testPropertyIsResolved() throws IOException, ModelBuildingException, XmlPullParserException {
        final TestMavenModel model = new TestMavenModel();
        model.addProperty("myVersion", "1.0.0");
        final Dependency dependency = new Dependency();
        dependency.setArtifactId("my-lib");
        dependency.setVersion("${myVersion}");
        dependency.setGroupId("com.example");
        model.addDependency(dependency);
        final String pomString = model.asPomString();
        try (final StringReader pomReader = new StringReader(pomString)) {
            final Model readModel = new MavenModelReader().readModel(pomReader);
            assertThat(readModel.getDependencies().get(0).getVersion(), equalTo("1.0.0"));
        }
    }
}