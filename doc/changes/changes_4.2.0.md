# Project Keeper 4.2.0, released 2024-03-11

Code name: Automated Release Process

## Summary

This release replaces Release Droid workflows `release_droid_.yml` with workflow `release.yml`. This will automatically release the project when `ci-build.yml` succeeded on `main` branch and the changes file contains an up-to-date release date. In case of problems you can start the workflow manually on GitHub and skip the release to Maven Central or GitHub if necessary.

## Features

* #516: Added automated release workflow

## Dependency Updates

### Project Keeper Root Project

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.0` to `2.0.1`

### Project Keeper Shared Model Classes

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.7` to `3.15.8`
* Updated `org.mockito:mockito-core:5.10.0` to `5.11.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.0` to `2.0.1`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.1.0` to `4.2.0`
* Added `com.jcabi:jcabi-github:1.8.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:4.1.0` to `4.2.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.1.0` to `4.2.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.7` to `3.15.8`
* Updated `org.mockito:mockito-junit-jupiter:5.10.0` to `5.11.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.0` to `2.0.1`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.1.0` to `4.2.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.1.0` to `4.2.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.0` to `2.0.1`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.1.0` to `4.2.0`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.10.0` to `5.11.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.0` to `2.0.1`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.1.0` to `4.2.0`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.10.0` to `5.11.0`
* Updated `org.mockito:mockito-junit-jupiter:5.10.0` to `5.11.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.0` to `2.0.1`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.1.0` to `4.2.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.0` to `2.0.1`
