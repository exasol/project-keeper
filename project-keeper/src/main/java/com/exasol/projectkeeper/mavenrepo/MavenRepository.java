package com.exasol.projectkeeper.mavenrepo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MavenRepository {

    public enum Artifact {
        CLI("-cli"),
        MAVEN_PLUGIN("-maven-plugin");

        private final String label;

        Artifact(String suffix) {
            this.label = ARTIFACT_PREFIX + suffix;
        }
    }

    private static final String ARTIFACT_PREFIX = "project-keeper";
    private static final String DEFAULT_REPOSITORY_URL = "https://search.maven.org";
    private static final String GROUP_ID = "com.exasol";
    private final String repositoryUrl;

    public MavenRepository() {
        this(DEFAULT_REPOSITORY_URL);
    }

    public MavenRepository(String url) {
        this.repositoryUrl = url;
    }

    public String getLatestVersion(Artifact artifact) throws IOException {
        URL url = new URL(DEFAULT_REPOSITORY_URL + "/solrsearch/select?q=g:"
                + GROUP_ID + "+AND+a:"
                + artifact.label + "&wt=json");
        JSONObject json = readJsonFromUrl(url);
        return json
                .getJSONObject("response")
                .getJSONArray("docs")
                .getJSONObject(0)
                .getString("latestVersion");
    }

    private static JSONObject readJsonFromUrl(URL url) throws IOException, JSONException {
        try (InputStream stream = url.openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String jsonText = readAll(reader);
            return new JSONObject(jsonText);
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
