# Project Keeper 4.3.0, released 2024-??-??

Code name: Bugfixes for validation and `update-dependencies`

## Summary

This release contains many bugfixes for the new modes `update-dependencies` and `verify-release`.

## Features

* #523: Added validation steps for changes file
* #556: Updated release process to create tags for Go projects

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
