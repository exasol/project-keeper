# Project Keeper 5.7.4, released 2026-07-16

Code name: Upgrade SonarQube analysis runtime

## Summary

This release upgrade the Java version for the Maven build from 17 to 21. Background: SonarQube analysis now requires runtime Java 21.

Please note that the Java version used for compiling and building the projects using project keeper will not change.

## Bugfixes

* #756: Upgrade SonarQube analysis runtime

## Dependency Updates

### Project Keeper Shared Model Classes

#### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter:6.1.1` to `6.1.2`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.3` to `5.7.4`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.7.3` to `5.7.4`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.3` to `5.7.4`
* Updated `org.junit.jupiter:junit-jupiter:6.1.1` to `6.1.2`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.7.3` to `5.7.4`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.3` to `5.7.4`
* Updated `org.junit.jupiter:junit-jupiter:6.1.1` to `6.1.2`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.7.3` to `5.7.4`

#### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter:6.1.1` to `6.1.2`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.3` to `5.7.4`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.3` to `5.7.4`
* Updated `org.junit.jupiter:junit-jupiter:6.1.1` to `6.1.2`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.3` to `5.7.4`

#### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter:6.1.1` to `6.1.2`
