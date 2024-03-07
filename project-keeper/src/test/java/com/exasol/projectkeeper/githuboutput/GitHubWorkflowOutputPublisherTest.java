package com.exasol.projectkeeper.githuboutput;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileIO;

@ExtendWith(MockitoExtension.class)
class GitHubWorkflowOutputPublisherTest {

    private static final Path PROJECT_DIR = null;
    private static final String PROJECT_VERSION = null;
    @Mock
    OutputPublisherFactory publisherFactoryMock;
    @Mock
    OutputPublisher publisherMock;
    @Mock
    ChangesFileIO changesFileIOMock;

    @Test
    void outputVersion() {
        testee(null).publish();
        verify(publisherMock).publish("version", PROJECT_VERSION);
    }

    @Test
    void closePublisher() {
        testee(null).publish();
        verify(publisherMock).close();
    }

    private GitHubWorkflowOutputPublisher testee(final ProjectKeeperConfig config) {
        when(publisherFactoryMock.create()).thenReturn(publisherMock);
        return new GitHubWorkflowOutputPublisher(config, PROJECT_DIR, PROJECT_VERSION, publisherFactoryMock,
                changesFileIOMock);
    }
}
