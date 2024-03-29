# Project Keeper 2.9.3, released 2023-02-01

Code name: Fix GitHub verify workflow

## Summary

This release fixes fetching dependencies for NPM modules. Dependencies were only fetched for the first NPM module, not for the others. The release also fixes the syntax of the GitHub verify workflow file and increases the timeout for Maven Central deployments.

PK's template for github workflow `ci-build-next-java.yml` now uses Maven profile `-P skipNativeImage` for projects using PK module [native_image](../user_guide/preparing_a_project_for_native_image_builds.md).

## Features

* #416: Updated template for build script `ci-build-next-java.yml` for projects using PK module `native_image`

## Bugfixes

* #421: Fixed syntax of GitHub verify workflow
* #422: Fixed fetching NPM dependencies for multiple modules.

## Refactoring

* #419: Increase timeout for Maven Central deployments

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Runtime Dependency Updates

* Removed `org.glassfish:jakarta.json:2.0.1`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.12.3` to `3.12.4`
* Updated `org.mockito:mockito-core:5.0.0` to `5.1.1`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.2` to `2.9.3`
* Removed `org.glassfish.jaxb:jaxb-runtime:4.0.1`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.2` to `2.9.3`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.2` to `2.9.3`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.12.3` to `3.12.4`
* Updated `org.mockito:mockito-junit-jupiter:5.0.0` to `5.1.1`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.2` to `2.9.3`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.2` to `2.9.3`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.3.0` to `3.4.2`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.2` to `2.9.3`

#### Test Dependency Updates

* Removed `org.jacoco:org.jacoco.core:0.8.8`
* Updated `org.mockito:mockito-core:5.0.0` to `5.1.1`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-jar-plugin:3.2.2` to `3.3.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.2` to `2.9.3`

#### Test Dependency Updates

* Removed `org.jacoco:org.jacoco.core:0.8.8`
* Updated `org.mockito:mockito-core:5.0.0` to `5.1.1`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.6.4` to `3.7.1`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.2` to `2.9.3`
