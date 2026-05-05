# Project Keeper 5.6.1, released 2026-05-05

Code name: Relax exclusion for GitHub action linter

## Summary

This release avoids failing GitHub action linter rule `obfuscation` caused by constructions like `if: ${{ false }}` in `ci-build-db-version-matrix.yml`.

## Bugfixes

* #732: Relaxed exclusion for GitHub action linter

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.6.0` to `5.6.1`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.6.0` to `5.6.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.6.0` to `5.6.1`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.6.0` to `5.6.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.6.0` to `5.6.1`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.6.0` to `5.6.1`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.6.0` to `5.6.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.6.0` to `5.6.1`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.6.0` to `5.6.1`
