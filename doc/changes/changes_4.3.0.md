# Project Keeper 4.3.0, released 2024-??-??

Code name: Custom Release Artifacts

## Summary

This release contains many new features and improvements:
* It allows customizing the workflow steps in `ci-build.yml` and `release.yml`, see the [user guide](https://github.com/exasol/project-keeper/blob/main/doc/user_guide/user_guide.md#customize-workflow-steps) for details.
* It allows specifying custom release artifacts like extension files. See the [user guide](https://github.com/exasol/project-keeper/blob/main/doc/user_guide/user_guide.md#custom-release-artifacts) for details.
* PK now automatically creates the required Git tags for Go projects.
* The release also contains many bugfixes for the new modes `update-dependencies` and `verify-release`.

## Features

* #523: Added validation steps for changes file
* #556: Updated release process to create tags for Go projects
* #517: Added configuration of custom release artifacts
* #519: Added configuration of custom build steps in `ci-build-yml`

## Bugfixes

* #546: Updated template for file `.settings/org.eclipse.jdt.core.prefs`
* #542: Prefixed release letter on GitHub with version number
* #545: Fix parsing of Go version numbers with a `v` prefix
* #548: Skip release build when preconditions are not fulfilled
* #540: Improved speed of validating mentioned issues in changes file
* #553: Reduced diff in `pom.xml` for mode `update-dependencies`
* #558: Fixed `update-dependencies` running with a released version

## Dependency Updates

### Project Keeper Shared Model Classes

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.8` to `3.16`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.2.0` to `4.3.0`
* Added `org.snakeyaml:snakeyaml-engine:2.7`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:4.2.0` to `4.3.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.2.0` to `4.3.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.8` to `3.16`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.2.0` to `4.3.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.2.0` to `4.3.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.2.0` to `4.3.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.2.0` to `4.3.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.2.0` to `4.3.0`
