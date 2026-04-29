package com.exasol.projectkeeper.sources.analyze.npm;

import static java.util.stream.Collectors.toMap;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exasol.projectkeeper.shared.dependencies.License;

import jakarta.json.*;

//[impl -> dsn~npm-dependency-licenses~1]
record NpmLicense(String module, String version, String name, String url) {

    private static final Pattern PATTERN = Pattern.compile("^(.+)@([0-9]+(\\.[0-9]+)*+)$");

    static Map<String, List<NpmLicense>> from(final JsonObject json) {
        return json.keySet().stream()
                .collect(toMap(NpmLicense::moduleName, key -> extract(key, json), NpmLicense::merge));
    }

    private static List<NpmLicense> extract(final String moduleAndVersion, final JsonObject licenseInfos) {
        final JsonObject o = licenseInfos.getJsonObject(moduleAndVersion);
        final String mainUrl = jString(o, "repository");
        final String[] p = split(moduleAndVersion);
        final String module = p[0];
        final String version = p[1];
        return jStringList(o, "licenses").stream()
                .map(name -> new NpmLicense(module, version, name, mainUrl))
                .toList();
    }

    private static String moduleName(final String moduleAndVersion) {
        return split(moduleAndVersion)[0];
    }

    private static List<NpmLicense> merge(final List<NpmLicense> left, final List<NpmLicense> right) {
        final List<NpmLicense> merged = new ArrayList<>(left);
        merged.addAll(right);
        return merged;
    }

    private static String jString(final JsonObject o, final String key) {
        return o.containsKey(key) ? o.getString(key) : null;
    }

    private static List<String> jStringList(final JsonObject o, final String key) {
        if (!o.containsKey(key)) {
            return Collections.singletonList(null);
        }
        final JsonValue value = o.get(key);
        if (value.getValueType() == JsonValue.ValueType.ARRAY) {
            return value.asJsonArray().getValuesAs(JsonString.class).stream()
                    .map(JsonString::getString)
                    .toList();
        } else {
            return List.of(((JsonString) value).getString());
        }
    }

    private static String[] split(final String moduleAndVersion) {
        final Matcher matcher = PATTERN.matcher(moduleAndVersion);
        return matcher.matches() //
                ? new String[] { matcher.group(1), matcher.group(2) }
                : new String[] { moduleAndVersion, null };
    }

    String getVersion() {
        return this.version;
    }

    License toLicense() {
        return new License(this.name, this.url);
    }
}
