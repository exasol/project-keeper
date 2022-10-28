# Project Keeper 2.9.0, released 2022-??-??

Code name: Support for NPM projects and minor improvements.

## Summary

Support for NPM projects and some additional minor improvements.

## Features

* #387: Updated plugin version for generated files `pk_generated_parent.xml`.
* #373: Added support for NPM projects.
* #383: Generate configuration file for Integrated Development Environment Microsoft Visual Code.
* #384: Added element `<distributionManagement>` to file `pk_generated_parent.pom` required for deployments to maven central.

## Refactorings

* #380: Refactored `getProjectName()` for GolangSources.

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.8.0` to `2.9.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.8.0` to `2.9.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.8.0` to `2.9.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.8.0` to `2.9.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.8.0` to `2.9.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.8.0` to `2.9.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.8.0` to `2.9.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.8.0` to `2.9.0`
