# Project Keeper 2.6.0, released 2022-08-05

Code name: Fix Golang dependencies

## Summary

This release fixes retrieving of Golang dependencies, it should now work for most Golang projects. It also generates a script for non-Maven projects that allows to run PK in continuous integration (CI) as well as local builds.

## Features

* #321: Added default link replacement for Exasol JDBC driver
* #162: Omit heading for empty dependency updates section in changelog
* #132: Ensured that the reproducible-build-maven-plugin is the last one in the plugins list
* #338: Generate a script for verifying non-Maven projects

## Bugfixes

* #330: Fixed dependency check
* #331: Fixed retrieving Golang dependencies

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.5.1` to `2.6.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.5.1` to `2.6.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.5.1` to `2.6.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.5.1` to `2.6.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.5.1` to `2.6.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.5.1` to `2.6.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.5.1` to `2.6.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.5.1` to `2.6.0`
