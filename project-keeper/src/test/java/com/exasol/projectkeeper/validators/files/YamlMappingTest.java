package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.snakeyaml.engine.v2.nodes.Node;

class YamlMappingTest {

    @Test
    void navigatesMappings() {
        final YamlMapping mapping = parse("""
                first:
                  value: one
                second:
                  value: two
                """);

        assertAll(() -> assertThat(mapping.keys(), contains("first", "second")),
                () -> assertTrue(mapping.contains("first")), () -> assertFalse(mapping.contains("missing")),
                () -> assertThat(mapping.mapping("first").string("value"), equalTo("one")),
                () -> assertThat(mapping.mappings().keySet(), contains("first", "second")),
                () -> assertThat(mapping.mappings().get("second").string("value"), equalTo("two")));
    }

    @Test
    void readsSequencesOfMappings() {
        final List<YamlMapping> items = parse("""
                items:
                  - id: first
                  - id: second
                empty:
                """).mappingList("items");

        assertAll(() -> assertThat(items.stream().map(item -> item.string("id")).toList(),
                contains("first", "second")), () -> assertThat(parse("empty:\n").mappingList("empty"), empty()),
                () -> assertThat(parse("value: one\n").mappingList("missing"), empty()));
    }

    @Test
    void readsOptionalAndNullStrings() {
        final YamlMapping mapping = parse("""
                present: value
                empty:
                """);

        assertAll(() -> assertThat(mapping.optionalString("present"), equalTo(Optional.of("value"))),
                () -> assertThat(mapping.optionalString("empty"), equalTo(Optional.empty())),
                () -> assertThat(mapping.optionalString("missing"), equalTo(Optional.empty())),
                () -> assertTrue(mapping.isNull("empty")), () -> assertTrue(mapping.isNull("missing")),
                () -> assertThrows(NoSuchElementException.class, () -> mapping.string("missing")));
    }

    @Test
    void convertsYamlTypesToObjects() {
        final YamlMapping mapping = parse("""
                text: value
                enabled: true
                integer: 42
                largeInteger: 2147483648
                decimal: 1.5
                nothing:
                list: [one, two]
                nested:
                  key: value
                """);

        assertThat(mapping.toObject(), equalTo(linkedMap(//
                "text", "value", //
                "enabled", true, //
                "integer", 42, //
                "largeInteger", 2147483648L, //
                "decimal", 1.5, //
                "nothing", null, //
                "list", List.of("one", "two"), //
                "nested", Map.of("key", "value"))));
        assertAll(() -> assertThat(mapping.objectMap("nested"), equalTo(Map.of("key", "value"))),
                () -> assertThat(mapping.objectMap("missing"), nullValue()),
                () -> assertThat(mapping.stringList("list"), contains("one", "two")));
    }

    @Test
    void createsMappingFromJavaObject() {
        final YamlMapping mapping = YamlMapping.from(Map.of("nested", Map.of("value", "content")));

        assertAll(() -> assertThat(mapping.mapping("nested").string("value"), equalTo("content")),
                () -> assertSame(mapping.node(), YamlMapping.from(mapping.node()).node()));
    }

    @Test
    void putsAndRemovesValues() {
        final YamlMapping mapping = parse("existing: old\n");

        mapping.put("existing", "new");
        mapping.put("added", 42);
        final Node removed = mapping.remove("existing");

        assertAll(() -> assertThat(GitHubWorkflowIO.create().dump(removed), equalTo("new\n")),
                () -> assertThat(mapping.toObject(), equalTo(linkedMap("added", 42))),
                () -> assertThat(mapping.remove("missing"), nullValue()));
    }

    @Test
    void putsValuesIntoNestedMappings() {
        final YamlMapping mapping = parse("""
                existing:
                  old: value
                empty:
                """);

        mapping.putInMapping("existing", "new", "value");
        mapping.putInMapping("empty", "created", true);
        mapping.putInMapping("missing", "created", 42);

        assertAll(() -> assertThat(mapping.objectMap("existing"),
                equalTo(linkedMap("old", "value", "new", "value"))),
                () -> assertThat(mapping.objectMap("empty"), equalTo(Map.of("created", true))),
                () -> assertThat(mapping.objectMap("missing"), equalTo(Map.of("created", 42))));
    }

    @Test
    void modifiesSequences() {
        final YamlMapping mapping = parse("""
                items:
                  - first
                  - second
                  - third
                """);

        mapping.replaceInSequence("items", 0, "replacement");
        mapping.insertIntoSequence("items", 1, "inserted");
        mapping.removeFromSequence("items", "second");
        mapping.removeFromSequence("missing", "ignored");

        assertThat(mapping.stringList("items"), contains("replacement", "inserted", "third"));
    }

    @Test
    void preservesCommentsWhenReplacingValue() {
        final YamlMapping mapping = parse("""
                value:
                  # nested block comment
                  nested: old # nested inline comment
                """);

        mapping.put("value", Map.of("replacement", "new"));

        assertThat(dump(mapping), allOf(containsString("# nested block comment"),
                containsString("# nested inline comment"), containsString("replacement: new")));
    }

    @Test
    void preservesCommentsWhenReplacingSequenceItem() {
        final YamlMapping mapping = parse("""
                items:
                  # item block comment
                  - value: old # item inline comment
                """);

        mapping.replaceInSequence("items", 0, Map.of("value", "new"));

        assertThat(dump(mapping), allOf(containsString("# item block comment"),
                containsString("# item inline comment"), containsString("value: new")));
    }

    @Test
    void reportsMissingRequiredMapping() {
        final YamlMapping yamlMapping = parse("value: content\n");
        final NullPointerException exception = assertThrows(NullPointerException.class, () -> yamlMapping.mapping("missing"));

        assertThat(exception.getMessage(), equalTo("Missing YAML key 'missing'"));
    }

    private static YamlMapping parse(final String yaml) {
        return YamlMapping.from(GitHubWorkflowIO.create().load(yaml));
    }

    private static String dump(final YamlMapping mapping) {
        return GitHubWorkflowIO.create().dump(mapping.node());
    }

    private static Map<String, Object> linkedMap(final Object... keyValues) {
        final Map<String, Object> result = new LinkedHashMap<>();
        for (int index = 0; index < keyValues.length; index += 2) {
            result.put((String) keyValues[index], keyValues[index + 1]);
        }
        return result;
    }
}
