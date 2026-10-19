# Project Keeper 5.8.0, released 2026-10-??

Code name: Maven Wrapper

## Summary

Project Keeper now generates and verifies Maven Wrapper in its default `only-script` mode for Maven-root projects. Generated GitHub workflows and Project Keeper's internal Maven commands use the wrapper, pinning Maven 3.10.0 for local and CI builds.

## Security

We supporessed unresolved vulnerability CVE-2026-92877 in transitive dependency `com.sun.xml.bind:jaxb-core` of `jcabi-github`. See [#799](https://github.com/exasol/project-keeper/issues/) for the proposed solution.

## Features

* #791: Add Maven Wrapper generation and verification.

## Dependency Updates

### Project Keeper Root Project

#### Plugin Dependency Updates

* Updated `org.itsallcode:openfasttrace-maven-plugin:3.0.0` to `3.1.0`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.7` to `5.8.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.7.7` to `5.8.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.7` to `5.8.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.7.7` to `5.8.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.7` to `5.8.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.7.7` to `5.8.0`

#### Test Dependency Updates

* Added `com.exasol:project-keeper-shared-test-setup:5.8.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.7` to `5.8.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.7` to `5.8.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.7` to `5.8.0`
