package com.exasol.projectkeeper.shared.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.config.VersionConfig.Visitor;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class ProjectKeeperConfigTest {

    @Test
    void createConfig() {
        final ProjectKeeperConfig config = ProjectKeeperConfig.builder()
                .sources(List.of(Source.builder().advertise(true).path(Path.of("pom.xml")).type(SourceType.MAVEN)
                        .modules(Set.of(ProjectKeeperModule.DEFAULT))
                        .parentPom(new ParentPomRef("parentGroupId", "parentArtifactId", "parentVersion",
                                "parentRelativePath"))
                        .build()))
                .excludes(List.of("exclude1")).linkReplacements(List.of("linkReplacement1"))
                .versionConfig(new VersionFromSource(Path.of("version-pom.xml"))).ciBuildRunnerOS("runner-os").build();
        assertAll(() -> assertThat(config.getExcludes(), contains("exclude1")),
                () -> assertThat(config.getLinkReplacements(), contains("linkReplacement1")),
                () -> assertThat(config.getVersionConfig(), instanceOf(VersionFromSource.class)),
                () -> assertThat(config.getCiBuildRunnerOS(), equalTo("runner-os")),
                () -> assertThat(config.getSources(), hasSize(1)),
                () -> assertThat(config.getSources().get(0).getType(), equalTo(SourceType.MAVEN)),
                () -> assertThat(config.getSources().get(0).getPath(), equalTo(Path.of("pom.xml"))),
                () -> assertThat(config.getSources().get(0).getModules(), equalTo(Set.of(ProjectKeeperModule.DEFAULT))),
                () -> assertThat(config.getSources().get(0).getParentPom().getGroupId(), equalTo("parentGroupId")),
                () -> assertThat(config.getSources().get(0).getParentPom().getArtifactId(),
                        equalTo("parentArtifactId")),
                () -> assertThat(config.getSources().get(0).getParentPom().getVersion(), equalTo("parentVersion")),
                () -> assertThat(config.getSources().get(0).getParentPom().getRelativePath(),
                        equalTo("parentRelativePath")));
    }

    @Test
    void createConfigWithDefaultRunnerOS() {
        final ProjectKeeperConfig config = ProjectKeeperConfig.builder().build();
        assertThat(config.getCiBuildRunnerOS(), equalTo("ubuntu-latest"));
    }

    @Test
    void createConfigWithNullRunnerOS() {
        final ProjectKeeperConfig config = ProjectKeeperConfig.builder().ciBuildRunnerOS(null).build();
        assertThat(config.getCiBuildRunnerOS(), equalTo("ubuntu-latest"));
    }

    @Test
    void versionFromSourceAcceptsVisitor() {
        final Visitor visitor = mock(Visitor.class);
        final VersionFromSource version = new VersionFromSource(Path.of("pom.xml"));
        version.accept(visitor);
        verify(visitor).visit(same(version));
    }

    @Test
    void fixedVersionAcceptsVisitor() {
        final Visitor visitor = mock(Visitor.class);
        final FixedVersion version = new FixedVersion("fixed");
        version.accept(visitor);
        verify(visitor).visit(same(version));
    }

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ProjectKeeperConfig.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(ProjectKeeperConfig.class).verify();
    }
}
