# Project Keeper 5.5.0, released 2026-??-??

Code name:

## Summary

This release updates Maven configuration to disable telemetry via [telemetry-java](https://github.com/exasol/telemetry-java/) for unit and integration tests and when executing other processes.

The release also updates the versions of Go and Node.JS used in GitHub workflows to 1.26 resp. 24.

## Features

* #717: Disable telemetry for Maven projects

## Dependency Updates

### Project Keeper Root Project

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.6` to `2.0.7`

### Project Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `org.eclipse.jgit:org.eclipse.jgit:7.5.0.202512021534-r` to `7.6.0.202603022253-r`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:4.3.1` to `4.5`
* Updated `org.mockito:mockito-core:5.21.0` to `5.23.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.6` to `2.0.7`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.2` to `10.0.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.4.0` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.4` to `3.5.5`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.4.6` to `5.5.0`
* Updated `org.yaml:snakeyaml:2.5` to `2.6`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.4.6` to `5.5.0`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.4` to `1.1.5`
* Updated `com.exasol:project-keeper-shared-test-setup:5.4.6` to `5.5.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:4.3.1` to `4.5`
* Updated `org.mockito:mockito-junit-jupiter:5.21.0` to `5.23.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.6` to `2.0.7`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.2` to `10.0.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.4` to `3.5.5`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.4.0` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.4` to `3.5.5`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.4.6` to `5.5.0`
* Updated `org.apache.maven:maven-model:3.9.11` to `3.9.15`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.4.6` to `5.5.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.6` to `2.0.7`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.2` to `10.0.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.4` to `3.5.5`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.4.0` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.4` to `3.5.5`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.4.6` to `5.5.0`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.4` to `1.1.5`
* Updated `org.mockito:mockito-core:5.21.0` to `5.23.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.6` to `2.0.7`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.2` to `10.0.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.4` to `3.5.5`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.4.0` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.4` to `3.5.5`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.4.6` to `5.5.0`
* Updated `org.eclipse.jgit:org.eclipse.jgit:7.5.0.202512021534-r` to `7.6.0.202603022253-r`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.4` to `1.1.5`
* Updated `com.exasol:project-keeper-shared-test-setup:5.4.6` to `5.5.0`
* Updated `org.mockito:mockito-core:5.21.0` to `5.23.0`
* Updated `org.mockito:mockito-junit-jupiter:5.21.0` to `5.23.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.6` to `2.0.7`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.2` to `10.0.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.4` to `3.5.5`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.4.0` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.4` to `3.5.5`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.4.6` to `5.5.0`
* Updated `org.yaml:snakeyaml:2.5` to `2.6`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.6` to `2.0.7`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.2` to `10.0.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.4.0` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.4` to `3.5.5`
