# Project Keeper 5.8.1, released 2026-10-23

Code name: Crawler Timeout

## Summary

This release makes the Maven project crawler faster for multi-module projects by reading each dependency POM only once. It also raises the default crawler timeout from 90 to 120 seconds and makes it configurable with system property `project-keeper.crawlerTimeout` (in seconds), e.g. `-Dproject-keeper.crawlerTimeout=300`.

## Features

* #803: Improved crawler performance and made the crawler timeout configurable

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.8.0` to `5.8.1`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.8.0` to `5.8.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.8.0` to `5.8.1`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.8.0` to `5.8.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.8.0` to `5.8.1`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.8.0` to `5.8.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.8.0` to `5.8.1`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.8.0` to `5.8.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.8.0` to `5.8.1`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.8.0` to `5.8.1`
