package com.exasol.projectkeeper.sources.analyze.golang;

import com.exasol.projectkeeper.shared.dependencies.License;

class GolangDependencyLicense {
    private final String licenseUrl;
    private final String moduleName;
    private final String licenseName;

    GolangDependencyLicense(final String moduleName, final String licenseUrl, final String licenseName) {
        this.moduleName = moduleName;
        this.licenseUrl = licenseUrl;
        this.licenseName = licenseName;
    }

    String getModuleName() {
        return this.moduleName;
    }

    String getLicenseUrl() {
        return this.licenseUrl;
    }

    String getLicenseName() {
        return this.licenseName;
    }

    License toLicense() {
        return new License(this.getLicenseName(), this.getLicenseUrl());
    }
}
