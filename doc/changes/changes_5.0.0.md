# Project Keeper 5.0.0, released 2025-03-10

Code name: Maven 3.8.7, custom artifact clarifications and security policy

## Summary

Breaking change:

From release 5.0.0 on this project requires a minimum Maven version of 3.8.7. 

This release we improved the documentation on the configuration of custom artifacts that project keeper can build into GitHub workflows.

We also added a security policy to let our contributors know how to best report security problems.

## Documentation

* #618: Added security policy
* #621: Added clarifications for custom artifacts

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.5.0` to `5.0.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:4.5.0` to `5.0.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.5.0` to `5.0.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.5.0` to `5.0.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.5.0` to `5.0.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.5.0` to `5.0.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.5.0` to `5.0.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.5.0` to `5.0.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.5.0` to `5.0.0`
