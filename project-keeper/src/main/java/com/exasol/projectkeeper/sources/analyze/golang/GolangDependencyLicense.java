package com.exasol.projectkeeper.sources.analyze.golang;

import com.exasol.projectkeeper.shared.dependencies.License;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
class GolangDependencyLicense {
    private final String licenseUrl;
    private final String moduleName;
    private final String licenseName;

    License toLicense() {
        return new License(this.getLicenseName(), this.getLicenseUrl());
    }
}
