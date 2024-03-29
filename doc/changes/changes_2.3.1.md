# Project Keeper 2.3.1, released 2022-04-20

Code name: Support native_image

## Summary

## Features

* #290: Added module for native_image

## Bugfixes

* #282: Fixed launching `go-licenses` when it is not on the `PATH`
* #295: Pinned version of `gaurav-nelson/github-action-markdown-link-check` to fix issues with `v1`

## Documentation

* #284: Described how to use PK with Golang projects

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `jakarta.json.bind:jakarta.json.bind-api:2.0.0` to `3.0.0`
* Updated `jakarta.json:jakarta.json-api:2.0.1` to `2.1.0`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.3.0` to `2.3.1`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.3.0` to `2.3.1`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:4.4.0` to `4.5.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.3.0` to `2.3.1`

#### Test Dependency Updates

* Updated `org.jacoco:org.jacoco.core:0.8.7` to `0.8.8`
* Updated `org.mockito:mockito-core:4.4.0` to `4.5.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.3.0` to `2.3.1`

#### Test Dependency Updates

* Updated `org.jacoco:org.jacoco.core:0.8.7` to `0.8.8`
* Updated `org.mockito:mockito-core:4.4.0` to `4.5.0`
