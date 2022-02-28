package com.exasol.projectkeeper.shared;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.dependencies.*;
import com.exasol.projectkeeper.shared.dependencychanges.*;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.CrawledMavenProject;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.MavenProjectCrawlResult;

class MavenProjectCrawlResultTest {
    private static final ProjectDependencies PROJECT_DEPENDENCIES = new ProjectDependencies(
            List.of(new ProjectDependency("My Project", "https://example.com",
                    List.of(new License("My License", "https://license.example.com")),
                    ProjectDependency.Type.COMPILE)));

    private static final DependencyChangeReport REPORT = new DependencyChangeReport(Collections.emptyList(),
            List.of(new NewDependency("com.example", "my-test", "1.2.3"),
                    new RemovedDependency("com.example", "my-other-lib", "1.2.3"),
                    new UpdatedDependency("com.example", "my-updated-dependency", "1.0.0", "1.0.1")),
            Collections.emptyList(), Collections.emptyList());
    private static final CrawledMavenProject CRAWLED_MAVEN_PROJECT = new CrawledMavenProject(REPORT,
            PROJECT_DEPENDENCIES, "1.2.3");
    private static final MavenProjectCrawlResult CRAWL_RESULT = new MavenProjectCrawlResult(
            Map.of("pom.xml", CRAWLED_MAVEN_PROJECT));

    @Test
    void testSerializeAndDeserialize() {
        assertThat(MavenProjectCrawlResult.fromJson(CRAWL_RESULT.toJson()), Matchers.equalTo(CRAWL_RESULT));
    }
}