# Project Keeper 2.8.0, released 2022-09-21

Code name: Minor changes

## Summary

## Bug Fixes

* #371: Fixed generation of sha256sum files broken by ticket #340.
* #376: Updated `org.yaml:snakeyaml:jar` to version 1.32 to fix vulnerability CVE-2022-38751 reported by oss index. Vulnerability CVE-2022-38752 is still excluded from check, though, as still unfixed by snakeyaml. 

## Features

* #374: If `pom.xml` does not specify a version for current artifact then use version from parent.
* #375: For Golang sources deriving name of project for file `changes.md` from GitHub repository rather than from local folder.

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.7.0` to `2.8.0`
* Updated `org.yaml:snakeyaml:1.31` to `1.32`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.7.0` to `2.8.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.7.0` to `2.8.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.7.0` to `2.8.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.7.0` to `2.8.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.7.0` to `2.8.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.7.0` to `2.8.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.7.0` to `2.8.0`
* Updated `org.yaml:snakeyaml:1.31` to `1.32`
