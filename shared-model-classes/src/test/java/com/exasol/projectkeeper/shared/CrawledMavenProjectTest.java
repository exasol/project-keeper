package com.exasol.projectkeeper.shared;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.mavenprojectcrawler.CrawledMavenProject;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class CrawledMavenProjectTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(CrawledMavenProject.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
