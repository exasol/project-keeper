# Project Keeper 5.4.0, released 2025-09-25

Code name: Allow patch releases

## Summary

The release process has been extended to support maintenance and patch branches.
It will now be triggered not only when merging into the `main` branch, but also when merging into any branch matching the `release/*` pattern. 

**Warning**: Because branches matching `release/*` trigger the release process, they should be protected with the same rules as the `main` branch.

## Features

* #672: Allow patch releases from branches

## Documentation

* #680: Update documentation to include a warning about overriding build steps due to new OSS Index auth

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.3.0` to `5.4.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.3.0` to `5.4.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.3.0` to `5.4.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.3.0` to `5.4.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.3.0` to `5.4.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.3.0` to `5.4.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.3.0` to `5.4.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.3.0` to `5.4.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.3.0` to `5.4.0`
