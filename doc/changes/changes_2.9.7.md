# Project Keeper 2.9.7, released 2023-04-13

Code name: Find duplicate classes

## Summary

This release adds duplicate-finder-maven-plugin to all Maven projects which finds duplicate classes in dependencies. This may cause the build to fail. In this case please fix your dependencies. You can run the plugin manually with `mvn duplicate-finder:check`.

This release also adds a default link replacement for the parsson library and changes the warning log message `Created 'doc/changes/changelog.md'. Don't forget to update it's content!` to level INFO.

## Features

* #447: Added duplicate-finder-maven-plugin to all Maven projects
* #448: Added default link replacement for parsson library

## Bugfixes

* #446: Changed warning log message "Created 'doc/changes/changelog.md'. Don't forget to update it's content!" to level INFO

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.14` to `3.14.1`
* Updated `org.mockito:mockito-core:5.2.0` to `5.3.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.6` to `2.0.7`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.6` to `2.9.7`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.6` to `2.9.7`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.6` to `2.9.7`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.14` to `3.14.1`
* Updated `org.mockito:mockito-junit-jupiter:5.2.0` to `5.3.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.6` to `2.0.7`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.6` to `2.9.7`
* Updated `org.apache.maven:maven-model:3.9.0` to `3.9.1`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.6` to `2.0.7`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.6` to `2.9.7`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.6` to `2.9.7`

#### Test Dependency Updates

* Updated `org.jacoco:org.jacoco.agent:0.8.8` to `0.8.9`
* Updated `org.mockito:mockito-core:5.2.0` to `5.3.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.6` to `2.0.7`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.6` to `2.9.7`

#### Test Dependency Updates

* Updated `org.jacoco:org.jacoco.agent:0.8.8` to `0.8.9`
* Updated `org.mockito:mockito-core:5.2.0` to `5.3.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.6` to `2.0.7`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.6` to `2.9.7`
