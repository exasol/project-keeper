# Project Keeper 2.9.6, released 2023-03-22

Code name: Remove Dependencies Workaround

## Summary

This release removes the workaround for inconsistencies in file `dependencies.md` introduced with release 2.9.5 as the developers preferred to install on the local machines the same maven version as on GitHub which should make the workaround obsolete.

Additionally generated file `pk_generated_parent.pom` now will require at least Maven version 3.8.7 to be used.

## Features

* #442: Removed workaround for inconsistencies in file `dependencies.md`
* #444: Changed minimum required version of Maven tool to be used from `3.6.3` to `3.8.7`.

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.5` to `2.9.6`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.5` to `2.9.6`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.5` to `2.9.6`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.5` to `2.9.6`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.5` to `2.9.6`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.5` to `2.9.6`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.5` to `2.9.6`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.5` to `2.9.6`
