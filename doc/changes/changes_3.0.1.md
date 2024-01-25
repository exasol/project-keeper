# Project Keeper 3.0.1, released 2024-01-25

Code name: Fixed timeout exception handling

## Summary

This release fixes the exception handling for process timeouts. When a started process timed out, PK threw exception `E-PK-CORE-99: Stream reading did not finish after timeout of PT5S` instead of the correct `E-PK-CORE-128: Timeout while waiting 10ms for command '...'.`, hiding the root cause of the problem.

## Bugfixes

* #518: Fixed exception handling for process timeouts

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:3.0.0` to `3.0.1`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:3.0.0` to `3.0.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:3.0.0` to `3.0.1`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:3.0.0` to `3.0.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:3.0.0` to `3.0.1`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:3.0.0` to `3.0.1`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:3.0.0` to `3.0.1`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:3.0.0` to `3.0.1`
