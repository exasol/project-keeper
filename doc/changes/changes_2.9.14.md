# Project Keeper 2.9.14, released 2023-10-24

Code name: Add enforcer-plugin

## Summary

This release adds the enforcer-plugin to generated parent POMs to avoid error messages during build. The minimum Maven version is set to 3.6.3 so that builds also work on Ubuntu 20.04.

## Features

* #483: Added enforcer-plugin

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Plugin Dependency Updates

* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.4.1`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.13` to `2.9.14`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.13` to `2.9.14`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.13` to `2.9.14`

#### Plugin Dependency Updates

* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.4.1`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.13` to `2.9.14`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.13` to `2.9.14`

#### Plugin Dependency Updates

* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.4.1`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.13` to `2.9.14`

#### Test Dependency Updates

* Updated `org.jacoco:org.jacoco.agent:0.8.10` to `0.8.11`

#### Plugin Dependency Updates

* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.4.1`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.9.0` to `3.10.1`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.13` to `2.9.14`

#### Test Dependency Updates

* Updated `org.jacoco:org.jacoco.agent:0.8.10` to `0.8.11`

#### Plugin Dependency Updates

* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.4.1`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.13` to `2.9.14`

#### Plugin Dependency Updates

* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.4.1`
