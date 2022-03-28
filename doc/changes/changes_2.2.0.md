# Project Keeper Project 2.2.0, released 2022-03-28

Code name: Added IDs to executions

## Features

* #259: Added ids to maven plugin executions
* #242: Added cancelling of GitHub action in-progress

## Bug Fixes:

* #256: Fixed that changelog file was only updated on second run

## Refactoring

* #258: Cleaned up non generated poms
* #246: Renamed repository to project-keeper

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Plugin Dependency Updates

* Removed `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M3`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.1.0` to `2.2.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.1.0` to `2.2.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.1.0` to `2.2.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.1.0` to `2.2.0`

#### Test Dependency Updates

* Updated `org.jacoco:org.jacoco.agent:0.8.7` to `0.8.5`
