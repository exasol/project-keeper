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

### Project-Keeper Shared Model Classes

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.4.0` to `5.5.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.3.0` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-source-plugin:3.2.1` to `3.3.0`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.10` to `2.9.11`
* Updated `org.yaml:snakeyaml:2.0` to `2.2`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.10` to `2.9.11`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.10` to `2.9.11`
* Updated `org.mockito:mockito-junit-jupiter:5.4.0` to `5.5.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.3.0` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-source-plugin:3.2.1` to `3.3.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.10` to `2.9.11`
* Updated `org.apache.maven:maven-model:3.9.1` to `3.9.4`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.10` to `2.9.11`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.5.0` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.3.0` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-source-plugin:3.2.1` to `3.3.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.10` to `2.9.11`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.4.0` to `5.5.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.3.0` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.8.1` to `3.9.0`
* Updated `org.apache.maven.plugins:maven-source-plugin:3.2.1` to `3.3.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.10` to `2.9.11`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.4.0` to `5.5.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.3.0` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.8.1` to `3.9.0`
* Updated `org.apache.maven.plugins:maven-source-plugin:3.2.1` to `3.3.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.10` to `2.9.11`
* Updated `org.yaml:snakeyaml:2.0` to `2.2`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.3.0` to `3.4.0`
