package com.exasol.projectkeeper.sources.analyze.npm;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exasol.projectkeeper.shared.dependencies.License;

import jakarta.json.JsonObject;

class NpmLicense {

    private static final Pattern PATTERN = Pattern.compile("^(.+)@([0-9]+(\\.[0-9]+)*)$");

    static NpmLicense from(final String moduleAndVersion, final JsonObject licenseInfos) {
        final JsonObject o = licenseInfos.getJsonObject(moduleAndVersion);
        final String name = jString(o, "licenses");
        final String mainUrl = jString(o, "repository");
        final String[] p = split(moduleAndVersion);
        return new NpmLicense(p[0], p[1], name, mainUrl);
    }

    /**
     * Problem: for URL "https://github.com/a/b" and file ".../node_modules/a/b/LICENSE.txt" this method returns full
     * URL "https://github.com/a/b/LICENSE.txt" which creates a 404 (file not found). Correct would be
     * "https://github.com/a/b/blob/main/LICENSE.txt" but this would require internal knowledge about URLs and file
     * organization in GitHub and would fail for other hosting platforms.
     *
     * @param projectDir project folder containing folder "node_modules"
     * @param mainUrl    main URL of the dependency
     * @param file       license file inside projectDir/node_modules
     * @return full URL of license file
     */
    static String url(final Path projectDir, final String mainUrl, final String file) {
        Path rel = projectDir.relativize(Paths.get(file));
        rel = rel.subpath(3, rel.getNameCount());
        return mainUrl + "/" + rel.toString();
    }

    private static String jString(final JsonObject o, final String key) {
        return o.containsKey(key) ? o.getString(key) : null;
    }

    private static String[] split(final String moduleAndVersion) {
        final Matcher matcher = PATTERN.matcher(moduleAndVersion);
        return matcher.matches() //
                ? new String[] { matcher.group(1), matcher.group(2) }
                : new String[] { moduleAndVersion, null };
    }

    private final String module;
    private final String version;
    private final String name;
    private final String url;

    private NpmLicense(final String module, final String version, final String name, final String url) {
        this.module = module;
        this.version = version;
        this.name = name;
        this.url = url;
    }

    String getModule() {
        return this.module;
    }

    String getVersion() {
        return this.version;
    }

    License toLicense() {
        return new License(this.name, this.url);
    }
}