package com.exasol.projectkeeper.validators.files;

import java.util.*;

import org.snakeyaml.engine.v2.comments.CommentLine;
import org.snakeyaml.engine.v2.nodes.*;
import org.snakeyaml.engine.v2.representer.StandardRepresenter;

final class YamlMapping {
    private final MappingNode node;

    private YamlMapping(final MappingNode node) {
        this.node = node;
    }

    static YamlMapping from(final Object value) {
        final Node node = value instanceof final Node yamlNode ? yamlNode : represent(value);
        return new YamlMapping((MappingNode) node);
    }

    Node node() {
        return node;
    }

    YamlMapping mapping(final String key) {
        return new YamlMapping((MappingNode) required(key));
    }

    Map<String, YamlMapping> mappings() {
        final Map<String, YamlMapping> result = new LinkedHashMap<>();
        node.getValue().forEach(entry -> result.put(scalar(entry.getKeyNode()),
                new YamlMapping((MappingNode) entry.getValueNode())));
        return result;
    }

    List<YamlMapping> mappingList(final String key) {
        final Node value = value(key);
        if (value == null || isNull(value)) {
            return List.of();
        }
        return ((SequenceNode) value).getValue().stream().map(item -> new YamlMapping((MappingNode) item)).toList();
    }

    String string(final String key) {
        return optionalString(key).orElseThrow();
    }

    Optional<String> optionalString(final String key) {
        final Node value = value(key);
        if (value == null || isNull(value)) {
            return Optional.empty();
        }
        return Optional.of(scalar(value));
    }

    @SuppressWarnings("unchecked")
    Map<String, Object> objectMap(final String key) {
        return (Map<String, Object>) toObject(value(key));
    }

    @SuppressWarnings("unchecked")
    List<String> stringList(final String key) {
        return (List<String>) toObject(value(key));
    }

    Object toObject() {
        return toObject(node);
    }

    Set<String> keys() {
        final Set<String> result = new LinkedHashSet<>();
        node.getValue().forEach(entry -> result.add(scalar(entry.getKeyNode())));
        return result;
    }

    boolean contains(final String key) {
        return find(key).isPresent();
    }

    boolean isNull(final String key) {
        final Node value = value(key);
        return value == null || isNull(value);
    }

    Node remove(final String key) {
        final Iterator<NodeTuple> iterator = node.getValue().iterator();
        while (iterator.hasNext()) {
            final NodeTuple entry = iterator.next();
            if (hasKey(entry, key)) {
                iterator.remove();
                return entry.getValueNode();
            }
        }
        return null;
    }

    void put(final String key, final Object value) {
        putNode(key, represent(value));
    }

    void putInMapping(final String mappingKey, final String key, final Object value) {
        Node mapping = value(mappingKey);
        if (mapping == null || isNull(mapping)) {
            mapping = represent(new LinkedHashMap<>());
            putNode(mappingKey, mapping);
        }
        new YamlMapping((MappingNode) mapping).put(key, value);
    }

    void replaceInSequence(final String key, final int index, final Object replacement) {
        final List<Node> values = sequence(key).getValue();
        final Node replacementNode = represent(replacement);
        final Node previous = values.set(index, replacementNode);
        copyComments(previous, replacementNode);
    }

    void insertIntoSequence(final String key, final int index, final Object value) {
        sequence(key).getValue().add(index, represent(value));
    }

    void removeFromSequence(final String key, final String value) {
        final Node sequence = value(key);
        if (sequence != null && !isNull(sequence)) {
            ((SequenceNode) sequence).getValue().removeIf(item -> value.equals(scalar(item)));
        }
    }

    private SequenceNode sequence(final String key) {
        return (SequenceNode) required(key);
    }

    private Node required(final String key) {
        return Objects.requireNonNull(value(key), () -> "Missing YAML key '" + key + "'");
    }

    private Node value(final String key) {
        return find(key).map(NodeTuple::getValueNode).orElse(null);
    }

    private Optional<NodeTuple> find(final String key) {
        return node.getValue().stream().filter(entry -> hasKey(entry, key)).findFirst();
    }

    private void putNode(final String key, final Node value) {
        final List<NodeTuple> entries = node.getValue();
        for (int index = 0; index < entries.size(); index++) {
            final NodeTuple entry = entries.get(index);
            if (hasKey(entry, key)) {
                copyComments(entry.getValueNode(), value);
                entries.set(index, new NodeTuple(entry.getKeyNode(), value));
                return;
            }
        }
        entries.add(new NodeTuple(represent(key), value));
    }

    private static Node represent(final Object value) {
        return new StandardRepresenter(GitHubWorkflowIO.createDumpSettings()).represent(value);
    }

    private static boolean hasKey(final NodeTuple entry, final String key) {
        return key.equals(scalar(entry.getKeyNode()));
    }

    private static boolean isNull(final Node node) {
        return node.getTag().equals(Tag.NULL);
    }

    private static String scalar(final Node node) {
        return ((ScalarNode) node).getValue();
    }

    private static void copyComments(final Node source, final Node target) {
        copyDirectComments(source, target);
        if (source instanceof final MappingNode sourceMapping
                && target instanceof final MappingNode targetMapping) {
            targetMapping.getValue().forEach(targetEntry -> sourceMapping.getValue().stream()
                    .filter(sourceEntry -> scalar(sourceEntry.getKeyNode())
                            .equals(scalar(targetEntry.getKeyNode())))
                    .findFirst()
                    .ifPresent(sourceEntry -> {
                        copyComments(sourceEntry.getKeyNode(), targetEntry.getKeyNode());
                        copyComments(sourceEntry.getValueNode(), targetEntry.getValueNode());
                    }));
        }
    }

    private static void copyDirectComments(final Node source, final Node target) {
        target.setBlockComments(copy(source.getBlockComments()));
        target.setInLineComments(copy(source.getInLineComments()));
        target.setEndComments(copy(source.getEndComments()));
    }

    private static List<CommentLine> copy(final List<CommentLine> comments) {
        return comments == null ? List.of() : new ArrayList<>(comments);
    }

    private static Object toObject(final Node node) {
        if (node == null || isNull(node)) {
            return null;
        }
        if (node instanceof final MappingNode mapping) {
            final Map<String, Object> result = new LinkedHashMap<>();
            mapping.getValue().forEach(entry -> result.put(scalar(entry.getKeyNode()), toObject(entry.getValueNode())));
            return result;
        }
        if (node instanceof final SequenceNode sequence) {
            return sequence.getValue().stream().map(YamlMapping::toObject).toList();
        }
        final ScalarNode scalar = (ScalarNode) node;
        final String value = scalar.getValue();
        if (scalar.getTag().equals(Tag.BOOL)) {
            return Boolean.valueOf(value);
        }
        if (scalar.getTag().equals(Tag.INT)) {
            return parseInteger(value);
        }
        if (scalar.getTag().equals(Tag.FLOAT)) {
            return Double.valueOf(value);
        }
        return value;
    }

    private static Number parseInteger(final String value) {
        try {
            return Integer.valueOf(value);
        } catch (final NumberFormatException exception) {
            return Long.valueOf(value);
        }
    }
}
