package com.exasol.projectkeeper.validators.pom.dependencies;

import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.validators.pom.PomTesting.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.validators.finding.FindingsFixer;

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
        this.validator.validate(pom, Collections.emptyList(),
                finding -> new FindingsFixer(mock(Logger.class)).fixFindings(List.of(finding)));
    }
}