package com.exasol.projectkeeper.sources.analyze.npm;

import java.io.StringReader;
import java.nio.file.Path;

import jakarta.json.JsonObject;

class TestData {

    static final Path SAMPLE_PATH = Path.of("/projects/p1/package.json");

    static final String CURRENT = jsonString("{", //
            "  'name': 'module-name',", //
            "  'version': '2.0.0',", //
            "  'devDependencies': {", //
            "    'changed-plugin': '1.2.0',", //
            "    'new-plugin': '1.3.0'", //
            "  },", //
            "  'dependencies': {", //
            "    'changed-compile': '2.2.0',", //
            "    'new-compile': '2.3.0'", //
            "  }", //
            "}");

    static final String PREVIOUS = jsonString("{", //
            "  'name': 'module-name',", //
            "  'version': '1.0.0',", //
            "  'devDependencies': {", //
            "    'changed-plugin': '1.1.0',", //
            "    'removed-plugin': '1.2.0'", //
            "  },", //
            "  'dependencies': {", //
            "    'changed-compile': '2.1.0',", //
            "    'removed-compile': '2.2.0'", //
            "  }", //
            "}");

    static final String LICENSES = jsonString("{", //
            "  'changed-plugin@1.2.0': {", //
            "    'licenses': 'CP-License',", //
            "    'repository': 'https://changed-plugin'", //
            "  },", //
            "  'new-plugin@1.3.0': {", //
            "    'licenses': 'NP-License',", //
            "    'repository': 'https://new-plugin'", //
            "  },", //
            "  'changed-compile@2.2.0': {", //
            "    'licenses': 'CC-License',", //
            "    'repository': 'https://changed-compile'", //
            "  },", //
            "  'new-compile@2.3.0': {", //
            "    'licenses': 'NC-License',", //
            "    'repository': 'https://new-compile'", //
            "  }", //
            "}");

    static final String DEPENDENCIES = jsonString("{", //
            "  'name': 'module-name',", //
            "  'version': '2.0.0',", //
            "  'dependencies': {", //
            "    'changed-plugin': {", //
            "      'version': '1.2.0',", //
            "      'resolved': 'https://changed-plugin/-/1.2.0.tgz'", //
            "    },", //
            "    'new-plugin': {", //
            "      'version': '1.3.0',", //
            "      'resolved': 'https://new-plugin/-/1.3.0.tgz'", //
            "    },", //
            "    'changed-compile': {", //
            "      'version': '2.2.0',", //
            "      'resolved': 'https://changed-compile/-/2.2.0.tgz'", //
            "    },", //
            "    'new-compile': {", //
            "      'version': '2.3.0',", //
            "      'resolved': 'https://new-compile/-/2.3.0.tgz'", //
            "    }", //
            "  }", //
            "}");

    static final String DEPENDENCIES_MISSING_URL = jsonString("{", //
            "  'name': 'module-name',", //
            "  'version': '2.0.0',", //
            "  'dependencies': {", //
            "    'new-compile': {", //
            "      'version': '2.3.0'", //
            "    }", //
            "  }", //
            "}");

    static String jsonString(final String... s) {
        return String.join("\n", s).replace('\'', '"');
    }

    static PackageJson samplePackageJson() {
        return packageJson(CURRENT);
    }

    static PackageJson packageJson(final String content) {
        return packageJson(SAMPLE_PATH, content);
    }

    static PackageJson packageJson(final Path path, final String content) {
        return new PackageJsonReader().read(path, json(content));
    }

    static JsonObject json(final String string) {
        return JsonIo.read(new StringReader(string));
    }

    private TestData() {
        // only static usage
    }
}
