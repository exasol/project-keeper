# Project Keeper 2.9.10, released 2023-08-04

Code name: Minor fixes

## Summary

This release improves handling of failed calls `npm ci` and `go-licenses` by proposing possible workarounds. The release also escapes pipe symbol in the dependencies table to fix broken rendering.

## Bugfixes

* #461: Improved handling of failed `npm ci`
* #458: Escape pipe symbol in dependencies table
* #465: Improved error message of failed `go-licenses`
* #467: Updated CI templates to run Sonar with Java 17

## Refactoring

* #341: Remove lombok

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.14.3` to `3.15.1`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.3` to `5.10.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.3` to `5.10.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.7` to `1.7.36`

#### Plugin Dependency Updates

* Removed `org.projectlombok:lombok-maven-plugin:1.18.20.0`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.9` to `2.9.10`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.9` to `2.9.10`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.9` to `2.9.10`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.14.3` to `3.15.1`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.3` to `5.10.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.3` to `5.10.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.7` to `1.7.36`

#### Plugin Dependency Updates

* Removed `org.projectlombok:lombok-maven-plugin:1.18.20.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.9` to `2.9.10`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.7` to `1.7.36`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.9` to `2.9.10`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.3` to `5.10.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.3` to `5.10.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.9` to `2.9.10`

#### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.3` to `5.10.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.3` to `5.10.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.7` to `1.7.36`

#### Plugin Dependency Updates

* Removed `org.projectlombok:lombok-maven-plugin:1.18.20.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.9` to `2.9.10`

#### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.3` to `5.10.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.3` to `5.10.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.7` to `1.7.36`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.9` to `2.9.10`

#### Plugin Dependency Updates

* Removed `org.projectlombok:lombok-maven-plugin:1.18.20.0`
