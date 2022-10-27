package com.exasol.projectkeeper.sources.analyze.npm;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.dependencies.*;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.sources.analyze.generic.CommandExecutor;
import com.exasol.projectkeeper.validators.changesfile.NamedDependencyChangeReport;
import com.exasol.projectkeeper.validators.changesfile.dependencies.DependencyChangeReportRenderer;

public class NpmServicesTest {

    NpmServices npmServices = new NpmServices(new CommandExecutor());

    private static final Logger LOGGER = Logger.getLogger(NpmServicesTest.class.getName());

    private static final Path PJ = Paths.get("package.json");
    private static final Path SMALL_SAMPLE = Paths.get("c:/HOME/Doc/221020-NPM-PK-issue-373/sample-project");
    private static final Path EMI = Paths.get("c:/Huge/Workspaces/Git/extension-manager-interface");

    @Test
    void changes() throws IOException {
        final PackageJson current = PackageJsonReader.read(SMALL_SAMPLE, PJ);
        final Optional<PackageJson> previous = this.npmServices.retrievePrevious(EMI, current);
        final DependencyChangeReport cReport = NpmDependencyChanges.report(current, previous);
        final NamedDependencyChangeReport nReport = new NamedDependencyChangeReport("sample project", cReport);
        final DependencyChangeReportRenderer renderer = new DependencyChangeReportRenderer();
        renderer.render(List.of(nReport)).forEach(LOGGER::fine);
    }

    @Test
    void dependencies() throws IOException {
        final PackageJson packageJson = PackageJsonReader.read(EMI, PJ);
        final ProjectDependencies dependencies = this.npmServices.getDependencies(packageJson);
        for (final ProjectDependency d : dependencies.getDependencies()) {
            LOGGER.fine(String.format("%s %s %s", d.getType(), d.getName(), d.getWebsiteUrl()));
            for (final License license : d.getLicenses()) {
                LOGGER.fine("- " + license.getName() + ": " + license.getUrl());
            }
        }
    }
}
