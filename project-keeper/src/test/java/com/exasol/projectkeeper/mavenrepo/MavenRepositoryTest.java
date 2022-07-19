package com.exasol.projectkeeper.mavenrepo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.mavenrepo.MavenRepository.JsonContentException;

import jakarta.json.*;

//[utest->dsn~verify-own-version~1]
class MavenRepositoryTest {

    @Test
    void url() throws MalformedURLException {
        final String url = MavenRepository.cli().getUrl();
        assertThat(url, startsWith(MavenRepository.DEFAULT_REPOSITORY_URL));
        assertThat(url, containsString(MavenRepository.GROUP_ID));
        assertThat(url, containsString(MavenRepository.ARTIFACT_PREFIX + "-cli"));
    }

    @Test
    void emptyJson_ThrowsException() throws IOException {
        final JsonObject json = json("{}");
        assertThrows(JsonContentException.class, () -> MavenRepository.getLatestVersion(json));
    }

    @Test
    void jsonWithoutVersionInformation_ThrowsException() throws IOException {
        final JsonObject json = json("{\"response\": {\"docs\": [{\"other\": \"2.4.6\"}]}}");
        assertThrows(JsonContentException.class, () -> MavenRepository.getLatestVersion(json));
    }

    @Test
    void localResource_Success() throws IOException, JsonContentException {
        final String url = MavenRepositoryTest.class //
                .getResource("/simulated-maven-central-version-response.json") //
                .toExternalForm();
        final MavenRepository testee = new MavenRepository(url);
        assertThat(testee.getLatestVersion(), equalTo("2.4.6"));
    }

    @Tag("integration")
    @Test
    // [itest->dsn~verify-own-version~1]
    void integrationTest() throws IOException, JsonContentException {
        assertThat(MavenRepository.cli().getLatestVersion(), matchesRegex("[0-9]+\\.[0-9]+\\.[0-9]+"));
        assertThat(MavenRepository.mavenPlugin().getLatestVersion(), matchesRegex("[0-9]+\\.[0-9]+\\.[0-9]+"));
    }

    private JsonObject json(final String content) {
        try (JsonReader jr = Json.createReader(new StringReader(content))) {
            return jr.readObject();
        }
    }
}
