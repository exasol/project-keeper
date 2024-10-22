package com.exasol.projectkeeper.shared.mavenprojectcrawler;

import java.util.Map;
import java.util.Objects;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

/**
 * Result of the maven-project-crawler.
 */
public final class MavenProjectCrawlResult {
    private Map<String, CrawledMavenProject> crawledProjects;

    /** Required for deserializing from JSON */
    public MavenProjectCrawlResult() {
        this(null);
    }

    /**
     * Create a new instance.
     * 
     * @param crawledProjects the crawled projects
     */
    public MavenProjectCrawlResult(final Map<String, CrawledMavenProject> crawledProjects) {
        this.crawledProjects = crawledProjects;
    }

    /**
     * Deserialize {@link DependencyChangeReport} from JSON string.
     *
     * @param json serialized JSON
     * @return deserialized {@link DependencyChangeReport}.
     */
    @SuppressWarnings("try") // Jsonb.close() might throw InterruptedException
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
    @SuppressWarnings("try") // Jsonb.close() might throw InterruptedException
    public String toJson() {
        try (final Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(this);
        } catch (final Exception exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-SMC-1")
                    .message("Failed to serialize MavenProjectCrawlResult").ticketMitigation().toString(), exception);
        }
    }

    @Override
    public String toString() {
        return "MavenProjectCrawlResult [crawledProjects=" + crawledProjects + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(crawledProjects);
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
        final MavenProjectCrawlResult other = (MavenProjectCrawlResult) obj;
        return Objects.equals(crawledProjects, other.crawledProjects);
    }

    /**
     * Get projects.
     * 
     * @return crawled projects
     */
    public Map<String, CrawledMavenProject> getCrawledProjects() {
        return crawledProjects;
    }

    /**
     * Set projects.
     * 
     * @param crawledProjects crawled projects
     */
    public void setCrawledProjects(final Map<String, CrawledMavenProject> crawledProjects) {
        this.crawledProjects = crawledProjects;
    }
}
