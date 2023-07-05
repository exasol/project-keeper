package com.exasol.projectkeeper.shared.mavenprojectcrawler;

import java.util.Map;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lombok.*;

/**
 * Result of the maven-project-crawler.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor // Required for deserializing from json
public final class MavenProjectCrawlResult {
    private Map<String, CrawledMavenProject> crawledProjects;

    /**
     * Deserialize {@link DependencyChangeReport} from JSON string.
     *
     * @param json serialized JSON
     * @return deserialized {@link DependencyChangeReport}.
     */
    public static MavenProjectCrawlResult fromJson(final String json) {
        try (final Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.fromJson(json, MavenProjectCrawlResult.class);
        } catch (final Exception exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-SMC-2")
                    .message("Failed to deserialize MavenProjectCrawlResult").ticketMitigation().toString(), exception);
        }
    }

    /**
     * Convert this object to JSON.
     *
     * @return JSON string
     */
    public String toJson() {
        try (final Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(this);
        } catch (final Exception exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-SMC-1")
                    .message("Failed to serialize MavenProjectCrawlResult").ticketMitigation().toString(), exception);
        }
    }
}
