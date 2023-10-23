package com.exasol.projectkeeper.dependencies;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.maven.model.*;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.pom.MavenModelFromRepositoryReader;
import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencies.License;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.validators.dependencies.ProjectDependencyReader;

@ExtendWith(MockitoExtension.class)
class ProjectDependencyReaderTest {

    @Mock
    MavenModelFromRepositoryReader artifactModelReaderMock;

    private List<ProjectDependency> readDependencies(final List<Plugin> plugins) {
        final MavenProject project = new MavenProject(new Model());
        project.getModel().setBuild(new Build());
        project.getModel().getBuild().setPlugins(plugins);
        return new ProjectDependencyReader(artifactModelReaderMock, project).readDependencies().getDependencies();
    }

    @Test
    void noDependencies() {
        assertThat(readDependencies(emptyList()), empty());
    }

    @Test
    void explicitPlugin() throws ProjectBuildingException {
        simulateDependencies("group", "art", "ver", "website", List.of(mavenLicense("license", "licenseUrl")));

        assertThat(readDependencies(List.of(plugin("group", "art", "ver", "no-null"))),
                contains(ProjectDependency.builder().type(Type.PLUGIN).name("name:group:art").websiteUrl("website")
                        .licenses(List.of(new License("license", "licenseUrl"))).build()));
    }

    // [utest -> dsn~dependency.md-file-validator-excludes-implicit-plugins~1]
    @Test
    void implicitPlugin() throws ProjectBuildingException {
        assertThat(readDependencies(List.of(plugin("group", "art", "ver", null))), empty());
    }

    private void simulateDependencies(final String groupId, final String artifactId, final String version,
            final String url, final List<org.apache.maven.model.License> licenses) throws ProjectBuildingException {
        final Model model = new Model();
        model.setArtifactId(artifactId);
        model.setGroupId(groupId);
        model.setVersion(version);
        model.setName("name:" + groupId + ":" + artifactId);
        model.setUrl(url);
        model.setLicenses(licenses);
        when(artifactModelReaderMock.readModel(eq(artifactId), eq(groupId), eq(version), anyList())).thenReturn(model);
    }

    private org.apache.maven.model.License mavenLicense(final String name, final String url) {
        final org.apache.maven.model.License license = new org.apache.maven.model.License();
        license.setName(name);
        license.setUrl(url);
        return license;
    }

    private Plugin plugin(final String groupId, final String artifactId, final String version,
            final String sourceLocation) {
        final Plugin plugin = new Plugin();
        plugin.setGroupId(groupId);
        plugin.setArtifactId(artifactId);
        plugin.setVersion(version);
        final InputSource source = new InputSource();
        source.setLocation(sourceLocation);
        plugin.setLocation("", new InputLocation(0, 0, source));
        return plugin;
    }
}
