package com.exasol.projectkeeper.sources.analyze.golang;

import com.exasol.projectkeeper.shared.dependencies.License;

public class GolangDependencyLicense {
    private final String licenseUrl;
    private final String moduleName;
    private final String licenseName;

    public GolangDependencyLicense(final String moduleName, final String licenseUrl, final String licenseName) {
        this.moduleName = moduleName;
        this.licenseUrl = licenseUrl;
        this.licenseName = licenseName;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public String getLicenseUrl() {
        return this.licenseUrl;
    }

    public String getLicenseName() {
        return this.licenseName;
    }

    public License toLicense() {
        return new License(this.getLicenseName(), this.getLicenseUrl());
    }
}
