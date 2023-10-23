# Project Keeper 2.9.13, released 2023-??-??

Code name: Remove Enforcer Plugin

## Summary

This release removes plugins from the generated `dependencies.md` file that are defined by Maven itself. This avoids a dependency on the Maven version which allows using any version you want.

## Features

* #480: Removed indirect plugins from `dependencies.md` & remove enforcer plugin

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.12` to `2.9.13`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.12` to `2.9.13`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.12` to `2.9.13`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.12` to `2.9.13`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.12` to `2.9.13`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.12` to `2.9.13`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.12` to `2.9.13`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.12` to `2.9.13`
