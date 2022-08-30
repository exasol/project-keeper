# Project Keeper 2.7.0, released 2022-??-??

Code name: Enhanced

## Summary

Interpret regular expressions for excludes in file `.project-keeper.yml` as multiline by default.

## Features

* #354: Interpret regular expressions for excludes as multiline by default. <br />
Some findings of PK have rather long output and contain multiple lines. In order to exclude such findings by regular expressions users needed to use the prefix `(?s)` up to now. PK now provides additional convenience by assuming by default the dot `.` in regular expressions to match any character, including a line terminator.
* #340: Compute sha256sum inside the target directory <br />
The checksum now does not include the folder name anymore which removes the need for users to replicate the folder structure on their machine by creating a folder `target` themselves.
* #360: Changed PK to add `language: Generic` when generating file `release_config.yml`.

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:4.6.1` to `4.7.0`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.6.2` to `2.7.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.6.2` to `2.7.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.6.2` to `2.7.0`
* Updated `org.mockito:mockito-junit-jupiter:4.6.1` to `4.7.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.6.2` to `2.7.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.6.2` to `2.7.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.6.2` to `2.7.0`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:4.6.1` to `4.7.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.6.2` to `2.7.0`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:4.6.1` to `4.7.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.6.2` to `2.7.0`
