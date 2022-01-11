package com.exasol.projectkeeper.dependencies;

import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.validators.changesfile.dependencies.model.DependencyChangeReport;

import jakarta.json.bind.*;
import lombok.*;

/**
 * The dependencies of a project.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDependencies {
    private List<ProjectDependency> dependencies;

    /**
     * Deserialize {@link ProjectDependencies} from JSON string.
     * 
     * @param json serialized JSON
     * @return deserialized {@link ProjectDependencies}.
     */
    public static ProjectDependencies fromJson(String json) {
        JsonbConfig config = new JsonbConfig()
                .withDeserializers(new DependencyChangeReport.DependencyChangeDeserializer());
        try (final Jsonb jsonb = JsonbBuilder.create(config)) {
            return jsonb.fromJson(json, ProjectDependencies.class);
        } catch (final Exception exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-SMC-2")
                    .message("Failed to deserialize DependencyChangeReport").ticketMitigation().toString(), exception);
        }
    }

    /**
     * Serialize to JSON string.
     * 
     * @return JSON string
     */
    public String toJson() {
        try (final Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(this);
        } catch (final Exception exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-SMC-5")
                    .message("Failed to serialize ProjectDependencies").ticketMitigation().toString(), exception);
        }
    }
}
