# Project Keeper 2.5.1, released 2022-??-??

Code name: Fix Golang dependencies

## Summary

This release fixes retrieving of Golang dependencies, it should now work for most Golang projects. It also generates a script for non-Maven projects that allows running PK in CI and locally.

## Features

* #321: Added default link replacement for Exasol JDBC driver
* #162: Omit heading for empty dependency updates section in changelog
* #132: Ensured that the reproducible-build-maven-plugin is the last one in the plugins list
* #338: Generate a script for verifying non-Maven projects

## Bugfixes

* #330: Fixed dependency check
* #331: Fixed retrieving Golang dependencies

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `jakarta.json:jakarta.json-api:2.1.0` to `2.1.1`
* Updated `org.eclipse.jgit:org.eclipse.jgit:6.1.0.202203080745-r` to `6.2.0.202206071550-r`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.10` to `3.10.1`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.8.2` to `5.9.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.8.2` to `5.9.0`
* Updated `org.mockito:mockito-core:4.6.0` to `4.6.1`
* Added `org.slf4j:slf4j-jdk14:1.7.36`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.1` to `1.1.2`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0` to `3.1.0`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.5.0` to `2.5.1`
* Updated `org.glassfish.jaxb:jaxb-runtime:3.0.2` to `4.0.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.5.0` to `2.5.1`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.1` to `1.1.2`
* Updated `com.exasol:project-keeper-shared-test-setup:2.5.0` to `2.5.1`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.10` to `3.10.1`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.8.2` to `5.9.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.8.2` to `5.9.0`
* Updated `org.mockito:mockito-junit-jupiter:4.6.0` to `4.6.1`
* Added `org.slf4j:slf4j-jdk14:1.7.36`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.1` to `1.1.2`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0` to `3.1.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.5.0` to `2.5.1`
* Updated `org.apache.maven:maven-model:3.8.5` to `3.8.6`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.5.0` to `2.5.1`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.8.2` to `5.9.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.8.2` to `5.9.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.1` to `1.1.2`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0` to `3.1.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.5.0` to `2.5.1`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.1` to `1.1.2`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.8.2` to `5.9.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.8.2` to `5.9.0`
* Updated `org.mockito:mockito-core:4.6.0` to `4.6.1`
* Added `org.slf4j:slf4j-jdk14:1.7.36`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.1` to `1.1.2`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0` to `3.1.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.5.0` to `2.5.1`
* Updated `org.eclipse.jgit:org.eclipse.jgit:6.1.0.202203080745-r` to `6.2.0.202206071550-r`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.1` to `1.1.2`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.8.2` to `5.9.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.8.2` to `5.9.0`
* Updated `org.mockito:mockito-core:4.6.0` to `4.6.1`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.1` to `1.1.2`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0` to `3.1.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.5.0` to `2.5.1`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.1` to `1.1.2`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0` to `3.1.0`
