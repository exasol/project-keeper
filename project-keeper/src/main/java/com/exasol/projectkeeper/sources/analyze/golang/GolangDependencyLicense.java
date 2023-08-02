package com.exasol.projectkeeper.sources.analyze.golang;

import java.util.Objects;

import com.exasol.projectkeeper.shared.dependencies.License;

final class GolangDependencyLicense {
    private final String moduleName;
    private final String licenseName;
    private final String licenseUrl;

    GolangDependencyLicense(final String moduleName, final String licenseName, final String licenseUrl) {
        this.moduleName = moduleName;
        this.licenseName = licenseName;
        this.licenseUrl = licenseUrl;
    }

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

    @Override
    public String toString() {
        return "GolangDependencyLicense [moduleName=" + moduleName + ", licenseName=" + licenseName + ", licenseUrl="
                + licenseUrl + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleName, licenseName, licenseUrl);
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
        final GolangDependencyLicense other = (GolangDependencyLicense) obj;
        return Objects.equals(moduleName, other.moduleName) && Objects.equals(licenseName, other.licenseName)
                && Objects.equals(licenseUrl, other.licenseUrl);
    }
}
