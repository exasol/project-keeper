package com.exasol.projectkeeper.mavenrepo;

import java.io.IOException;
import java.net.URL;

import com.exasol.projectkeeper.ProjectKeeper;

import jakarta.json.*;
import lombok.Getter;

/**
 * This class allows getting the latest version of project-keeper from Maven Central.
 */
//[impl->dsn~verify-own-version~1]
public class MavenRepository {

    /**
     * @return Maven repository using URL for cli artifacts of project-keeper.
     */
    public static MavenRepository cli() {
        return new MavenRepository(url("-cli"));
    }

    /**
     * @return Maven repository using URL for project-keeper maven-plugin.
     */
    public static MavenRepository mavenPlugin() {
        return new MavenRepository(url("-maven-plugin"));
    }

    /**
     * Accessor for Json object, called internally and in unit tests.
     *
     * @param json json object to retrieve latest version from.
     * @return latest version.
     * @throws JsonContentException if json does not contain the expected keys.
     */
    public static String getLatestVersion(final JsonObject json) throws JsonContentException {
        try {
            return json.getJsonObject("response") //
                    .getJsonArray("docs") //
                    .getJsonObject(0) //
                    .getString("latestVersion");
        } catch (final NullPointerException e) {
            throw new JsonContentException("Could not find /response/docs[0]/latestVersion in json document.");
        }
    }

    private static String url(final String root, final String artifact) {
        return root + "/solrsearch/select?q=g:" + GROUP_ID + "+AND+a:" + artifact + "&wt=json";
    }

    private static String url(final String artifactSuffix) {
        return url(DEFAULT_REPOSITORY_URL, ARTIFACT_PREFIX + artifactSuffix);
    }

    static final String DEFAULT_REPOSITORY_URL = "https://search.maven.org";
    static final String GROUP_ID = "com.exasol";
    static final String ARTIFACT_PREFIX = "project-keeper";

    @Getter
    private final String url;

    /**
     * Productive code is expected to use the static methods {@link MavenRepository#cli} and
     * {@link MavenRepository#mavenPlugin}.
     *
     * <p>
     * This constructor is designated for tests, but as tests are in a different package the visibility needs to be
     * public.
     * </p>
     *
     * @param url URL of maven repository.
     */
    public MavenRepository(final String url) {
        this.url = url;
    }

    /**
     * @return latest version of project-keeper in the flavor addressed by the URL of this repository.
     * @throws IOException          if URL cannot be connected
     * @throws JsonContentException if json does not contain the expected keys.
     */
    public String getLatestVersion() throws IOException, JsonContentException {
        try (JsonReader reader = Json.createReader(new URL(this.url).openStream())) {
            return getLatestVersion(reader.readObject());
        }
    }

    /**
     * This exception is thrown if response of maven repository in json format does not contain the expected keys.
     */
    public static class JsonContentException extends Exception {
        private static final long serialVersionUID = 1L;

        /**
         * @param message the detail message.
         */
        public JsonContentException(final String message) {
            super(message);
        }
    }

}
