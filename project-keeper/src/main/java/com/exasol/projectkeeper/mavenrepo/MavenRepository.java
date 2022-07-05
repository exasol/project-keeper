package com.exasol.projectkeeper.mavenrepo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.exasol.projectkeeper.ProjectKeeper;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.Getter;

/**
 * See {@link ProjectKeeper}, field ownVersion = ProjectKeeper.class.getPackage().getImplementationVersion();
 */
//[impl->dsn~verify-own-version~1]
public class MavenRepository {

    public static MavenRepository cli() {
        return new MavenRepository(url("-cli"));
    }

    public static MavenRepository mavenPlugin() {
        return new MavenRepository(url("-maven-plugin"));
    }

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

    public MavenRepository(final String url) {
        this.url = url;
    }

    public String getLatestVersion() throws IOException, JsonContentException {
        try (InputStream stream = new URL(this.url).openStream()) {
            return getLatestVersion(Json.createReader(stream).readObject());
        }
    }

    public static class JsonContentException extends Exception {
        private static final long serialVersionUID = 1L;

        public JsonContentException(final String message) {
            super(message);
        }
    }

}
