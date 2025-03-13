package com.exasol.projectkeeper.sources.analyze.golang;

import com.exasol.projectkeeper.shared.dependencies.License;

record GolangDependencyLicense(String moduleName, String licenseName, String licenseUrl) {

    License toLicense() {
        return new License(this.licenseName, this.licenseUrl);
    }

    String getModuleName() {
        return moduleName;
    }

    String getLicenseName() {
        return licenseName;
    }

    String getLicenseUrl() {
        return licenseUrl;
    }
}
