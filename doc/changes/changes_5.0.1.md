# Project Keeper 5.0.1, released 2025-04-09

Code name: Improve reproducible builds.

## Summary

This release switches the reproducible builds from `io.github.zlika:reproducible-build-maven-plugin` to using the
`project.build.outputTimestamp` property with the last commit time.

It also configures the `versions-maven-plugin` to skip the reporting of unused transitive dependencies when using 
BOMs in dependency management.

## Features

* #635: Improve Maven reproducible builds
* #634: Skip showing unused dependencies from BOM on maven versions plugin

## Dependency Updates

### Project Keeper Shared Model Classes

#### Plugin Dependency Updates

* Added `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1`
* Removed `io.github.zlika:reproducible-build-maven-plugin:0.17`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.0.0` to `5.0.1`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.0.0` to `5.0.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.0.0` to `5.0.1`

#### Plugin Dependency Updates

* Added `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1`
* Removed `io.github.zlika:reproducible-build-maven-plugin:0.17`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.0.0` to `5.0.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.0.0` to `5.0.1`

#### Plugin Dependency Updates

* Added `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1`
* Removed `io.github.zlika:reproducible-build-maven-plugin:0.17`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.0.0` to `5.0.1`

#### Plugin Dependency Updates

* Added `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1`
* Removed `io.github.zlika:reproducible-build-maven-plugin:0.17`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.0.0` to `5.0.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.0.0` to `5.0.1`

#### Plugin Dependency Updates

* Added `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1`
* Removed `io.github.zlika:reproducible-build-maven-plugin:0.17`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.0.0` to `5.0.1`

#### Plugin Dependency Updates

* Added `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1`
* Removed `io.github.zlika:reproducible-build-maven-plugin:0.17`
