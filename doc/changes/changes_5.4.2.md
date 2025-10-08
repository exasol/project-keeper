# Project Keeper 5.4.2, released 2025-10-08

Code name: Fix release build for projects without Maven Central deployment

## Summary

This release fixes the release build for projects without Maven Central deployment. The GitHub workflow no longer fails with the following error message:

```
Error when evaluating 'secrets'. .github/workflows/ci-build.yml (Line: 327, Col: 11): Secret OSSRH_GPG_SECRET_KEY is required, but not provided while calling., .github/workflows/ci-build.yml (Line: 327, Col: 11): Secret OSSRH_GPG_SECRET_KEY_PASSWORD is required, but not provided while calling., .github/workflows/ci-build.yml (Line: 327, Col: 11): Secret MAVEN_CENTRAL_PORTAL_USERNAME is required, but not provided while calling., .github/workflows/ci-build.yml (Line: 327, Col: 11): Secret MAVEN_CENTRAL_PORTAL_TOKEN is required, but not provided while calling.
```

## Bugfixes

* #684: Fixed release build for projects without Maven Central deployment

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.4.1` to `5.4.2`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.4.1` to `5.4.2`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.4.1` to `5.4.2`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.4.1` to `5.4.2`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.4.1` to `5.4.2`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.4.1` to `5.4.2`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.4.1` to `5.4.2`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.4.1` to `5.4.2`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.4.1` to `5.4.2`
