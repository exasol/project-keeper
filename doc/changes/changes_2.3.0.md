# Project Keeper Project 2.3.0, released 2022-??-??

Code name:

## Features

* #262: Added automatic fix for the version of parent pom reference in the pom.xml
* #213: Replaced `actions/cache@v3` action in workflows with `actions/setup-java@v2` / `cache: 'maven'`

## Refactoring:

* #216: Added sonar-plugin to pk_generated_parent.pom to make maven in the CI pick the one from the correct org

## Bug Fixes

* #251: Added error-code-report again

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:0.7.1` to `1.1.0`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.2.0` to `2.3.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.2.0` to `2.3.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:0.7.1` to `1.1.0`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.2.0` to `2.3.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:0.7.1` to `1.1.0`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.2.0` to `2.3.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:0.7.1` to `1.1.0`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`
