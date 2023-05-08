# Project Keeper 2.9.8, released 2023-??-??

Code name:

## Summary

This release fixes security issues CVE-2023-28840, CVE-2023-28842, and CVE-2023-28841 reported by dependabot, all caused by vulnerable versions of components referenced in test resource `project-keeper/src/test/resources/go.mod`. The current release fixes these issues by renaming the test resource to `sample-contents-for-go.mod`.

## Bugfixes

* #451: Fixed issues reported by dependabot

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.7` to `2.9.8`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.7` to `2.9.8`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.7` to `2.9.8`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.7` to `2.9.8`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.7` to `2.9.8`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.7` to `2.9.8`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.7` to `2.9.8`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.7` to `2.9.8`
