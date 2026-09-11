# Project Keeper 5.7.6, released 2026-??-??

Code name:

## Summary

**Note:** This release upgrades the `maven-failsafe-plugin` to 3.6.0. Builds that skip tests with `-DskipTests` must now also set `-DskipITs` to skip Failsafe integration tests.

## Features

* #766: Update Maven template plugins and Project Keeper's self-version check to the latest stable release only.

## Dependency Updates

### Project Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `org.eclipse.jgit:org.eclipse.jgit:7.7.1.202607240634-r` to `7.8.0.202609011348-r`

#### Test Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.19`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.5` to `5.7.6`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.7.5` to `5.7.6`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.5` to `5.7.6`
* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.19`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.7.5` to `5.7.6`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-api:2.0.18` to `2.0.19`
* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.19`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.5` to `5.7.6`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.7.5` to `5.7.6`

#### Test Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.19`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.5` to `5.7.6`
* Updated `org.eclipse.jgit:org.eclipse.jgit:7.7.1.202607240634-r` to `7.8.0.202609011348-r`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.5` to `5.7.6`
* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.19`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.5` to `5.7.6`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`
