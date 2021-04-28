package com.exasol.projectkeeper.validators.pom.dependencies;

import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.validators.pom.PomTesting.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Collections;

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
        assertThat(validationOf(this.validator, pom, Collections.emptyList()), hasNoMoreFindingsAfterApplyingFixes());
    }

    protected void applyFixes(final Document pom) {
        this.validator.validate(pom, Collections.emptyList(), finding -> finding.getFix().fixError(mock(Log.class)));
    }
}