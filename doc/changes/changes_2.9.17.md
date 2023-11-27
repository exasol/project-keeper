# Project Keeper 2.9.17, released 2023-11-27

Code name: Simplify matrix job configuration

## Summary

This release improves matrix builds, adding a new job `build` that depends on the matrix build job. This simplifies configuring branch protection rules because it's not necessary any more to update the job names when upgrading the tested database version.

The release also adds documentation describing how Project Keeper works with non-Maven projects extracts the user guide into a separate file.

## Feature

* #504: Used single job name for Matrix builds

## Documentation

* #501: Documented usage for non-Maven projects
* #493: Created user guide and cleanup Readme.md

## Dependency Updates

### Project Keeper Shared Model Classes

#### Plugin Dependency Updates

* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.16` to `2.9.17`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.16` to `2.9.17`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.16` to `2.9.17`

#### Plugin Dependency Updates

* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.16` to `2.9.17`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.16` to `2.9.17`

#### Plugin Dependency Updates

* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.16` to `2.9.17`

#### Plugin Dependency Updates

* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.16` to `2.9.17`

#### Plugin Dependency Updates

* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.16` to `2.9.17`

#### Plugin Dependency Updates

* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`
