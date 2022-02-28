package com.exasol.projectkeeper.shared.dependencychanges;

import java.lang.reflect.Type;
import java.util.List;

import org.eclipse.yasson.YassonJsonb;

import com.exasol.errorreporting.ExaError;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.serializer.*;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;
import lombok.*;

/**
 * This class represents a report of changed dependencies.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DependencyChangeReport {
    /** Dependency changes in compile scope */
    private List<DependencyChange> compileDependencyChanges;
    /** Dependency changes in runtime scope */
    private List<DependencyChange> runtimeDependencyChanges;
    /** Dependency changes in test scope */
    private List<DependencyChange> testDependencyChanges;
    /** Dependency changes in plugin scope */
    private List<DependencyChange> pluginDependencyChanges;

    /**
     * Custom serialization can be replaced once https://github.com/eclipse-ee4j/jsonb-api/pull/284 is available.
     */
    public static class DependencyChangeSerializer implements JsonbSerializer<DependencyChange> {
        @Override
        public void serialize(final DependencyChange dependencyChange, final JsonGenerator generator,
                final SerializationContext ctx) {
            generator.writeStartObject();
            /* Using Yasson here is quite hacky since it breaks the changeability of the JSON implementation */
            generator.writeKey(dependencyChange.getClass().getSimpleName());
            try (final YassonJsonb jsonb = (YassonJsonb) JsonbBuilder.create()) {
                jsonb.toJson(dependencyChange, generator);
            } catch (final Exception exception) {
                throw new JsonbException(ExaError.messageBuilder("F-PK-SMC-4")
                        .message("Failed to serialize DependencyChange class.").toString(), exception);
            }
            generator.writeEnd();
        }
    }

    /**
     * A JSON deserializer for a {@link DependencyChange}.
     */
    public static class DependencyChangeDeserializer implements JsonbDeserializer<DependencyChange> {
        @Override
        public DependencyChange deserialize(final JsonParser parser, final DeserializationContext ctx,
                final Type rtType) {
            parser.next();
            final String className = parser.getString();
            final Class<? extends DependencyChange> dependencyChangeClass = getDependencyChangeClassForName(className);
            try (final YassonJsonb jsonb = (YassonJsonb) JsonbBuilder.create()) {
                return jsonb.fromJson(parser, dependencyChangeClass);
            } catch (final Exception exception) {
                throw new JsonbException(ExaError.messageBuilder("F-PK-SMC-6")
                        .message("Failed to deserialize DependencyChange class.").toString(), exception);
            }
        }

        private Class<? extends DependencyChange> getDependencyChangeClassForName(final String className) {
            final List<Class<? extends DependencyChange>> classes = List.of(NewDependency.class,
                    RemovedDependency.class, UpdatedDependency.class);
            for (final Class<? extends DependencyChange> dependencyChangeClass : classes) {
                if (dependencyChangeClass.getSimpleName().equals(className)) {
                    return dependencyChangeClass;
                }
            }
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-SMC-3")
                    .message("Failed to find matching class for name {{name}}.", className).ticketMitigation()
                    .toString());
        }
    }
}
