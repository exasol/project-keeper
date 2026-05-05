# Project Keeper 5.6.0, released 2026-05-05

Code name: Reduce Ossindex usage

## Summary

This release reduces the number of consumed Ossindex credits by
* running ossindex only in a dedicated job
* skipping ossindex in all other jobs
* reducing the frequency of the `dependencies_check.yml` workflow from daily to weekly

The release also adds a linter workflow for GitHub actions.

## Features

* #728: Reduce usage of Ossindex tokens
* #727: Added linter for GitHub actions

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.5.2` to `5.6.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.5.2` to `5.6.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.5.2` to `5.6.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.5.2` to `5.6.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.5.2` to `5.6.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.5.2` to `5.6.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.5.2` to `5.6.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.5.2` to `5.6.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.5.2` to `5.6.0`
