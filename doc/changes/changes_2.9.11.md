# Project Keeper 2.9.11, released 2023-08-28

Code name: Four Small Improvements

## Summary

This adds four small improvements:

* Upgrade Go version in workflow files to `"1.20"`
* Replace deprecated Sonar property `sonar.login` with `sonar.token`
* Add property `<skipTests>${skip.surefire.tests}</skipTests>` to surefire plugin to allow skipping unit tests with `mvn -Dskip.surefire.tests=true verify`
* Add step to GitHub actions to free up ~14GB of space by deleting Android and Dotnet from GitHub runner

## Features

* ISSUE_NUMBER: description

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.10` to `2.9.11`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.10` to `2.9.11`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.10` to `2.9.11`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.10` to `2.9.11`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.10` to `2.9.11`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.10` to `2.9.11`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.10` to `2.9.11`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.10` to `2.9.11`
