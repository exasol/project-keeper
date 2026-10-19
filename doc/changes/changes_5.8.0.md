# Project Keeper 5.8.0, released 2026-10-??

Code name: Maven Wrapper

## Summary

Project Keeper now generates and verifies Maven Wrapper in its default `only-script` mode for Maven-root projects. Generated GitHub workflows and Project Keeper's internal Maven commands use the wrapper, pinning Maven 3.10.0 for local and CI builds.

## Features

* #791: Add Maven Wrapper generation and verification.

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.7` to `5.8.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.7.7` to `5.8.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.7` to `5.8.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.7.7` to `5.8.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.7` to `5.8.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.7.7` to `5.8.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.7` to `5.8.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.7` to `5.8.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.7` to `5.8.0`
