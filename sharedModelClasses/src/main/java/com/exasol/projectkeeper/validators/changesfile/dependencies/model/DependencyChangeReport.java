package com.exasol.projectkeeper.validators.changesfile.dependencies.model;

import java.lang.reflect.Type;
import java.util.List;

import org.eclipse.yasson.YassonJsonb;

import com.exasol.errorreporting.ExaError;

import jakarta.json.bind.*;
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

    public static DependencyChangeReport fromJson(String json) {
        JsonbConfig config = new JsonbConfig().withDeserializers(new DependencyChangeDeserializer());
        try (final Jsonb jsonb = JsonbBuilder.create(config)) {
            return jsonb.fromJson(json, DependencyChangeReport.class);
        } catch (final Exception exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-SMC-2")
                    .message("Failed to deserialize DependencyChangeReport").ticketMitigation().toString(), exception);
        }
    }

    /**
     * Convert this object to JSON.
     * 
     * @return JSON string
     */
    public String toJson() {
        JsonbConfig config = new JsonbConfig().withSerializers(new DependencyChangeSerializer());
        try (final Jsonb jsonb = JsonbBuilder.create(config)) {
            return jsonb.toJson(this);
        } catch (final Exception exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-SMC-1")
                    .message("Failed to serialize DependencyChangeReport").ticketMitigation().toString(), exception);
        }
    }

    /*
     * Custom serialization can be replaced once https://github.com/eclipse-ee4j/jsonb-api/pull/284 is available.
     */
    public static class DependencyChangeSerializer implements JsonbSerializer<DependencyChange> {
        @Override
        public void serialize(DependencyChange dependencyChange, JsonGenerator generator, SerializationContext ctx) {
            generator.writeStartObject();
            /* Using Yasson here is quite hacky since it breaks the changeability of the JSON implementation */
            generator.writeKey(dependencyChange.getClass().getSimpleName());
            try (final YassonJsonb jsonb = (YassonJsonb) JsonbBuilder.create()) {
                jsonb.toJson(dependencyChange, generator);
            } catch (Exception exception) {
                throw new JsonbException(ExaError.messageBuilder("F-PK-SMC-4")
                        .message("Failed to serialize DependencyChange class.").toString(), exception);
            }
            generator.writeEnd();
        }
    }

    public static class DependencyChangeDeserializer implements JsonbDeserializer<DependencyChange> {
        @Override
        public DependencyChange deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
            parser.next();
            String className = parser.getString();
            Class<? extends DependencyChange> dependencyChangeClass = getDependencyChangeClassForName(className);
            try (final YassonJsonb jsonb = (YassonJsonb) JsonbBuilder.create()) {
                return jsonb.fromJson(parser, dependencyChangeClass);
            } catch (Exception exception) {
                throw new JsonbException(ExaError.messageBuilder("F-PK-SMC-6")
                        .message("Failed to deserialize DependencyChange class.").toString(), exception);
            }
        }

        private Class<? extends DependencyChange> getDependencyChangeClassForName(String className) {
            final List<Class<? extends DependencyChange>> classes = List.of(NewDependency.class,
                    RemovedDependency.class, UpdatedDependency.class);
            for (Class<? extends DependencyChange> dependencyChangeClass : classes) {
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
