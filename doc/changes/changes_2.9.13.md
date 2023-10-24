# Project Keeper 2.9.13, released 2023-10-24

Code name: Remove Enforcer Plugin

## Summary

This release removes plugins from the generated `dependencies.md` file that are defined by Maven itself. This avoids a dependency on the Maven version which allows using any version you want.

The release also reduces build verbosity by setting the `quiet` option of `maven-javadoc-plugin` to `true`.

## Features

* #480: Removed indirect plugins from `dependencies.md` & remove enforcer plugin

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `jakarta.json:jakarta.json-api:2.1.2` to `2.1.3`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.5.0` to `5.6.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.3.0` to `1.3.1`
* Removed `org.apache.maven.plugins:maven-enforcer-plugin:3.4.0`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.5.0` to `3.6.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.0` to `2.16.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.10` to `0.8.11`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184` to `3.10.0.2594`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.12` to `2.9.13`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.12` to `2.9.13`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.12` to `2.9.13`
* Updated `org.mockito:mockito-junit-jupiter:5.5.0` to `5.6.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.3.0` to `1.3.1`
* Removed `org.apache.maven.plugins:maven-enforcer-plugin:3.4.0`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.5.0` to `3.6.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.0` to `2.16.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.10` to `0.8.11`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184` to `3.10.0.2594`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.12` to `2.9.13`
* Updated `org.apache.maven:maven-model:3.9.4` to `3.9.5`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.12` to `2.9.13`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.3.0` to `1.3.1`
* Removed `org.apache.maven.plugins:maven-enforcer-plugin:3.4.0`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.5.0` to `3.6.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.0` to `2.16.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.10` to `0.8.11`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184` to `3.10.0.2594`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.12` to `2.9.13`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.5.0` to `5.6.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.3.0` to `1.3.1`
* Removed `org.apache.maven.plugins:maven-enforcer-plugin:3.4.0`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.5.0` to `3.6.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.0` to `2.16.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.10` to `0.8.11`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184` to `3.10.0.2594`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.12` to `2.9.13`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.5.0` to `5.6.0`
* Added `org.mockito:mockito-junit-jupiter:5.6.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.3.0` to `1.3.1`
* Removed `org.apache.maven.plugins:maven-enforcer-plugin:3.4.0`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.5.0` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.9.0` to `3.10.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.0` to `2.16.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.10` to `0.8.11`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184` to `3.10.0.2594`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.12` to `2.9.13`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.3.0` to `1.3.1`
* Removed `org.apache.maven.plugins:maven-enforcer-plugin:3.4.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.0` to `2.16.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.10` to `0.8.11`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184` to `3.10.0.2594`
