# Project Keeper 5.8.2, released 2026-10-??

Code name: Dependabot Monitoring

## Summary

This release removes automated dependency issues, pull requests, and PK's `update-dependencies` mode. The scheduled vulnerability scan fails on findings, and `fix` removes obsolete `dependencies_update.yml` workflows.

Project Keeper now uses a minimal Java HTTP client to read GitHub issue state during release verification.

## Features

* #807: Remove the automatic dependency updater and use Dependabot for vulnerability monitoring.
* #799: Replace `jcabi-github` with a minimal GitHub REST client and remove its transitive JAXB dependency.

## Security

* #796: Removed the temporary OSS Index suppression for CVE-2026-92877 after removing the affected `jcabi-github` dependency.

## Dependency Updates

### Project Keeper Shared Model Classes

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.2` to `2.1.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.6.0` to `3.6.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.15` to `0.8.16`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.8.1` to `5.8.2`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.8.1` to `5.8.2`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.8.1` to `5.8.2`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.2` to `2.1.3`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.6.0` to `3.6.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.15` to `0.8.16`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.8.1` to `5.8.2`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.8.1` to `5.8.2`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.2` to `2.1.3`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.6.0` to `3.6.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.15` to `0.8.16`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.8.1` to `5.8.2`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.8.1` to `5.8.2`
* Updated `org.jacoco:org.jacoco.agent:0.8.15` to `0.8.16`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.2` to `2.1.3`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.6.0` to `3.6.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.15` to `0.8.16`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.8.1` to `5.8.2`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.8.1` to `5.8.2`
* Updated `org.jacoco:org.jacoco.agent:0.8.15` to `0.8.16`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.2` to `2.1.3`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.6.0` to `3.6.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.15` to `0.8.16`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.8.1` to `5.8.2`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.2` to `2.1.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.6.0` to `3.6.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.15` to `0.8.16`
