package com.exasol.projectkeeper.shared;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.dependencies.*;
import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.*;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.CrawledMavenProject;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.MavenProjectCrawlResult;

class MavenProjectCrawlResultTest {
    private static final ProjectDependency DEPENDENCY = ProjectDependency.builder() //
            .type(BaseDependency.Type.COMPILE) //
            .name("My Project") //
            .websiteUrl("https://license.example.com") //
            .licenses(List.of(new License("My License", "https://license.example.com"))) //
            .build();
    private static final ProjectDependencies PROJECT_DEPENDENCIES = new ProjectDependencies(List.of(DEPENDENCY));
    private static final DependencyChangeReport REPORT = DependencyChangeReport.builder() //
            .typed(Type.RUNTIME, List.of( //
                    new NewDependency("com.example", "my-test", "1.2.3"), //
                    new RemovedDependency("com.example", "my-other-lib", "1.2.3"), //
                    new UpdatedDependency("com.example", "my-updated-dependency", "1.0.0", "1.0.1"))) //
            .build();
    private static final CrawledMavenProject CRAWLED_MAVEN_PROJECT = new CrawledMavenProject(REPORT,
            PROJECT_DEPENDENCIES, "1.2.3");
    private static final MavenProjectCrawlResult CRAWL_RESULT = new MavenProjectCrawlResult(
            Map.of("pom.xml", CRAWLED_MAVEN_PROJECT));

    @Test
    void testSerializeAndDeserialize() {
        assertThat(MavenProjectCrawlResult.fromJson(CRAWL_RESULT.toJson()), Matchers.equalTo(CRAWL_RESULT));
    }
}