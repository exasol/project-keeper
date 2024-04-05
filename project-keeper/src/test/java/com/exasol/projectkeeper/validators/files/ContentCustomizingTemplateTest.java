package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.validators.files.ContentCustomizingTemplate.ContentCustomizer;
import com.exasol.projectkeeper.validators.files.FileTemplate.Validation;

@ExtendWith(MockitoExtension.class)
class ContentCustomizingTemplateTest {

    @Mock
    FileTemplate delegateMock;
    @Mock
    ContentCustomizer customizerMock;

    @Test
    void getPathInProject() {
        when(delegateMock.getPathInProject()).thenReturn(Path.of("test"));
        assertThat(testee().getPathInProject(), equalTo(Path.of("test")));
    }

    @Test
    void getValidation() {
        when(delegateMock.getValidation()).thenReturn(Validation.REQUIRE_EXACT);
        assertThat(testee().getValidation(), equalTo(Validation.REQUIRE_EXACT));
    }

    @Test
    void getContent() {
        when(delegateMock.getContent()).thenReturn("original");
        when(customizerMock.customizeContent("original")).thenReturn("customized");
        assertThat(testee().getContent(), equalTo("customized"));
    }

    private FileTemplate testee() {
        return new ContentCustomizingTemplate(delegateMock, customizerMock);
    }
}
