# Project Keeper Project 2.3.0, released 2022-??-??

Code name:

Note: in #130 we changed file `.github/workflows/ci-build.yml` from `REQUIRE_EXIST` to `REQUIRE_EXACT`. If you have customized this file you will need to add an exclusion for this.

## Features

* #262: Added automatic fix for the version of parent pom reference in the pom.xml
* #130: Added validation for Release-Droid configuration `release_config.yml`.
* #213: Replaced `actions/cache@v3` action in workflows with `actions/setup-java@v2` / `cache: 'maven'`

## Refactoring:

* #216: Added sonar-plugin to pk_generated_parent.pom to make maven in the CI pick the one from the correct org

## Bug Fixes

* #251: Added error-code-report again

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `org.eclipse.jgit:org.eclipse.jgit:6.0.0.202111291000-r` to `6.1.0.202203080745-r`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:0.7.1` to `1.1.0`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.2.0` to `2.3.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.2.0` to `2.3.0`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.0` to `1.1.1`
* Updated `org.mockito:mockito-core:4.3.1` to `4.4.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:0.7.1` to `1.1.0`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.2.0` to `2.3.0`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.0` to `1.1.1`
* Updated `org.mockito:mockito-core:4.3.1` to `4.4.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:0.7.1` to `1.1.0`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.2.0` to `2.3.0`
* Updated `org.eclipse.jgit:org.eclipse.jgit:6.0.0.202111291000-r` to `6.1.0.202203080745-r`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.0` to `1.1.1`
* Updated `org.mockito:mockito-core:4.3.1` to `4.4.0`
* Updated `org.slf4j:slf4j-jdk14:1.7.35` to `1.7.36`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:0.7.1` to `1.1.0`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`
