# Project Keeper 2.4.5, released 2022-05-01

Code name: Bugfixes for Golang projects

## Bugfixes

* #279: Fixed getting license for Golang test dependencies
* #316: Added new builtin replacements to BrokenLinkReplacer
* #313: Fixed labelling of of dependency change type
* #314: Fixed issues with Golang modules in sub-directories

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:4.5.1` to `4.6.0`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.4.4` to `2.4.5`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.4.4` to `2.4.5`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.4.4` to `2.4.5`
* Removed `org.mockito:mockito-core:4.5.1`
* Added `org.mockito:mockito-junit-jupiter:4.6.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.4.4` to `2.4.5`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.4.4` to `2.4.5`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.4.4` to `2.4.5`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:4.5.1` to `4.6.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.4.4` to `2.4.5`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:4.5.1` to `4.6.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.4.4` to `2.4.5`
