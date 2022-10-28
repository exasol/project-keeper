package com.exasol.projectkeeper.sources.analyze.npm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exasol.projectkeeper.shared.dependencies.License;

import jakarta.json.JsonObject;

class NpmLicense {

    private static final Pattern PATTERN = Pattern.compile("^(.+)@([0-9]+(\\.[0-9]+)*+)$");

    static NpmLicense from(final String moduleAndVersion, final JsonObject licenseInfos) {
        final JsonObject o = licenseInfos.getJsonObject(moduleAndVersion);
        final String name = jString(o, "licenses");
        final String mainUrl = jString(o, "repository");
        final String[] p = split(moduleAndVersion);
        return new NpmLicense(p[0], p[1], name, mainUrl);
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