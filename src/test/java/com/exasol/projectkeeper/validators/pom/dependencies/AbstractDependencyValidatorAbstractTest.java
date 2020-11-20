package com.exasol.projectkeeper.validators.pom.dependencies;

import static com.exasol.projectkeeper.validators.pom.PomTesting.POM_WITH_NO_PLUGINS;
import static com.exasol.projectkeeper.validators.pom.PomTesting.readXmlFromResources;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

abstract class AbstractDependencyValidatorAbstractTest {
    private final AbstractDependencyValidator validator;

    AbstractDependencyValidatorAbstractTest(final AbstractDependencyValidator validator) {
        this.validator = validator;
    }

    @Test
    void testFixFixesValidation() throws ParserConfigurationException, SAXException, IOException {
        final Document pom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        applyFixes(pom);
        final AtomicBoolean success = new AtomicBoolean(true);
        this.validator.validate(pom, List.of(), finding -> success.set(false));
        assertThat(success.get(), equalTo(true));
    }

    protected void applyFixes(final Document pom) {
        this.validator.validate(pom, List.of(), finding -> finding.getFix().fixError(mock(Log.class)));
    }
}