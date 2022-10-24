package com.exasol.projectkeeper.sources.analyze.npm;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.dependencies.License;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.sources.analyze.generic.CommandExecutor;
import com.exasol.projectkeeper.validators.changesfile.NamedDependencyChangeReport;
import com.exasol.projectkeeper.validators.changesfile.dependencies.DependencyChangeReportRenderer;

public class NpmServicesTest {

    private static final Logger LOGGER = Logger.getLogger(NpmServicesTest.class.getName());

    private static final Path SMALL_SAMPLE = Paths.get("c:/HOME/Doc/221020-NPM-PK-issue-373/sample-project/");
    private static final Path EMI = Paths.get("c:/Huge/Workspaces/Git/extension-manager-interface/");

    @Test
    void changes() throws IOException {
        final PackageJson packageJson = PackageJsonReader.read(SMALL_SAMPLE);
        final DependencyChangeReport cReport = new NpmDependencyChanges(packageJson).getReport();

        final NamedDependencyChangeReport nReport = new NamedDependencyChangeReport("sample project", cReport);
        final DependencyChangeReportRenderer renderer = new DependencyChangeReportRenderer();
        renderer.render(List.of(nReport)).forEach(LOGGER::fine);
    }

    @Test
    void dependencies() throws IOException {
        final PackageJson packageJson = PackageJsonReader.read(EMI);
        final List<ProjectDependency> dependencies = new NpmDependencies(new CommandExecutor(), packageJson)
                .getDependencies();
        for (final ProjectDependency d : dependencies) {
            LOGGER.fine(String.format("%s %s %s", d.getType(), d.getName(), d.getWebsiteUrl()));
            for (final License license : d.getLicenses()) {
                LOGGER.fine("- " + license.getName() + ": " + license.getUrl());
            }
        }
    }
}
