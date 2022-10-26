package com.exasol.projectkeeper.shared.dependencychanges;

import java.util.*;
import java.util.function.Function;

import org.eclipse.yasson.YassonJsonb;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.serializer.*;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;
import lombok.AllArgsConstructor;

/**
 * This class represents a report of changed dependencies.
 */
// @Data
@AllArgsConstructor
public class DependencyChangeReport {

    /**
     * @return builder for {@link DependencyChangeReport}
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Dependency changes in various scopes */
    private Map<Type, List<DependencyChange>> changes = new EnumMap<>(Type.class);

    /**
     * Default constructor for a new instance of {@link DependencyChangeReport}.
     */
    public DependencyChangeReport() {
        for (final Type type : Type.values()) {
            this.changes.put(type, new ArrayList<>());
        }
    }

    /**
     * @param type {@link Type} of dependencies to get changes for
     * @return list of changes for the specified type
     */
    public List<DependencyChange> getChanges(final Type type) {
        return this.changes.get(type);
    }

    /**
     * @return map containing list changes for each {@link Type} of dependencies
     */
    // only for serialization and deserialization
    public Map<Type, List<DependencyChange>> getChanges() {
        return this.changes;
    }

    /**
     * @param changes map containing list changes for each {@link Type} of dependencies
     */
    // only for serialization and deserialization
    public void setChanges(final Map<Type, List<DependencyChange>> changes) {
        this.changes = changes;
    }

    // old ordering: compile, runtime, test, plugin

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
                final java.lang.reflect.Type rtType) {
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

    /**
     * Builder for new instances of class{@link DependencyChangeReport}
     */
    public static final class Builder {
        private final DependencyChangeReport report = new DependencyChangeReport();

        /**
         * Adds a list of changes for the specified {@link Type} of the changed dependencies.
         *
         * @param type    {@link Type} of the changed dependencies.
         * @param changes list of changes
         * @return this for fluent programming
         */
        public Builder typed(final Type type, final List<DependencyChange> changes) {
            this.report.changes.put(type, changes);
            return this;
        }

        /**
         * Adds a list of changes each with an individual {@link Type} of the changed dependency.
         *
         * @param changes      list of changes
         * @param typeDetector type detector to identify the {@link Type} of the dependency for each change
         * @return this for fluent programming
         */
        public Builder mixed(final List<DependencyChange> changes,
                final Function<DependencyChange, Type> typeDetector) {
            for (final DependencyChange c : changes) {
                this.report.changes.get(typeDetector.apply(c)).add(c);
            }
            return this;
        }

        /**
         * @return new instance of {@link DependencyChangeReport}.
         */
        public DependencyChangeReport build() {
            return this.report;
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DependencyChangeReport other = (DependencyChangeReport) obj;
        return Objects.equals(this.changes, other.changes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.changes);
    }
}
