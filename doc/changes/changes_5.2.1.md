# Project Keeper 5.2.1, released 2025-06-06

Code name: Auto-publish to Maven Central

## Summary

This release fixes an issue with the `release.yml` workflow that required manually publishing of Maven Central deployments. Maven Central Deployments no are done automatically during the release build without manual steps.

## Bugfixes

* #654: Fixed automatic Maven Central deplyoment

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.2.0` to `5.2.1`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.2.0` to `5.2.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.2.0` to `5.2.1`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.2.0` to `5.2.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.2.0` to `5.2.1`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.2.0` to `5.2.1`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.2.0` to `5.2.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.2.0` to `5.2.1`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.2.0` to `5.2.1`
