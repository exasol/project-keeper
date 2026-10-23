package com.exasol.projectkeeper.pom;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Model;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MavenModelFromRepositoryReaderTest {
    @Mock
    ProjectBuilder mavenProjectBuilder;
    @Mock
    MavenSession session;
    @Mock
    ArtifactHandlerManager artifactHandlerManager;

    @Test
    void rejectsNullVersion() {
        final Model model = new Model();
        model.setName("test project");
        final var projectBuildingRequest = new DefaultProjectBuildingRequest();
        projectBuildingRequest.setProject(new MavenProject(model));
        when(this.session.getProjectBuildingRequest()).thenReturn(projectBuildingRequest);
        final var reader = new MavenModelFromRepositoryReader(this.mavenProjectBuilder, this.session,
                this.artifactHandlerManager);

        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> reader.readModel("artifact", "group", null, emptyList()));

        assertThat(exception.getMessage(),
                equalTo("E-PK-MPC-66: Version of dependency group:artifact is null in project 'test project'."));
        verifyNoInteractions(this.mavenProjectBuilder, this.artifactHandlerManager);
    }

    @Test
    void readModelReturnsBuiltModel() throws ProjectBuildingException {
        final Model dependencyModel = mockBuiltModel();
        final var reader = new MavenModelFromRepositoryReader(this.mavenProjectBuilder, this.session,
                this.artifactHandlerManager);

        assertThat(reader.readModel("artifact", "group", "1.0.0", emptyList()), sameInstance(dependencyModel));
    }

    @Test
    void readModelBuildsSameCoordinatesOnlyOnce() throws ProjectBuildingException {
        final Model dependencyModel = mockBuiltModel();
        final var reader = new MavenModelFromRepositoryReader(this.mavenProjectBuilder, this.session,
                this.artifactHandlerManager);

        reader.readModel("artifact", "group", "1.0.0", emptyList());
        final Model secondRead = reader.readModel("artifact", "group", "1.0.0", emptyList());

        assertThat(secondRead, sameInstance(dependencyModel));
        verify(this.mavenProjectBuilder, times(1)).build(any(Artifact.class), eq(true),
                any(ProjectBuildingRequest.class));
    }

    private Model mockBuiltModel() throws ProjectBuildingException {
        when(this.session.getProjectBuildingRequest()).thenReturn(new DefaultProjectBuildingRequest());
        when(this.artifactHandlerManager.getArtifactHandler("pom")).thenReturn(new DefaultArtifactHandler("pom"));
        final Model dependencyModel = new Model();
        final ProjectBuildingResult result = mock(ProjectBuildingResult.class);
        when(result.getProject()).thenReturn(new MavenProject(dependencyModel));
        when(this.mavenProjectBuilder.build(any(Artifact.class), eq(true), any(ProjectBuildingRequest.class)))
                .thenReturn(result);
        return dependencyModel;
    }
}
