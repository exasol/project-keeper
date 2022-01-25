package com.exasol.projectkeeper.plugin;

import java.nio.file.Path;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.ProjectKeeper;

/**
 * Abstract basis for Mojos in this project.
 */
public abstract class AbstractProjectKeeperMojo extends AbstractMojo {

    @Parameter(property = "project-keeper.skip", defaultValue = "false")
    private String skip;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession session;

    protected ProjectKeeper getProjectKeeper() {
        return ProjectKeeper.createProjectKeeper(new MvnLogger(getLog()), this.project.getBasedir().toPath(),
                this.project.getName(), this.project.getArtifactId(), this.project.getVersion(),
                Path.of(this.session.getLocalRepository().getBasedir()));
    }

    /**
     * Check if the project-keeper is enabled.
     * 
     * @return {@code true} if the plugin is enabled, else {@code false}
     */
    protected boolean isEnabled() {
        if ("true".equals(this.skip)) {
            getLog().info("Skipping project-keeper.");
            return false;
        } else if ("false".equals(this.skip)) {
            return true;
        } else {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-MVNP-75")
                    .message("Invalid value {{value}} for property 'project-keeper.skip'.", this.skip)
                    .mitigation("Please set the property to 'true' or 'false'.").toString());
        }
    }

    public static class MvnLogger implements Logger {
        private final Log mvnLog;

        public MvnLogger(final Log mvnLog) {
            this.mvnLog = mvnLog;
        }

        @Override
        public void info(final String message) {
            this.mvnLog.info(message);
        }

        @Override
        public void warn(final String message) {
            this.mvnLog.warn(message);
        }

        @Override
        public void error(final String message) {
            this.mvnLog.error(message);
        }
    }
}
