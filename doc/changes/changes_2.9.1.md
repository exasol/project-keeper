# Project Keeper 2.9.1, released 2022-??-??

Code name: Minor Changes

## Summary

Fixed some bugs.

## Bug Fix

* #397: Categorized Go dependencies as 'unknown' if module name does not contain version number
* #398: Fixed dependency change report if file `package.json` or `go.mod` is missing in previous release.

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:0.4.1` to `1.0.0`
* Updated `org.eclipse.jgit:org.eclipse.jgit:6.2.0.202206071550-r` to `6.3.0.202209071007-r`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.10.1` to `3.11`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.0` to `5.9.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.0` to `5.9.1`
* Updated `org.mockito:mockito-core:4.7.0` to `4.8.1`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:0.4.1` to `1.0.0`
* Updated `com.exasol:project-keeper-shared-model-classes:2.9.0` to `2.9.1`
* Updated `org.glassfish.jaxb:jaxb-runtime:4.0.0` to `4.0.1`
* Updated `org.yaml:snakeyaml:1.32` to `1.33`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.0` to `2.9.1`

#### Test Dependency Updates

* Updated `com.exasol:maven-project-version-getter:1.1.1` to `1.2.0`
* Updated `com.exasol:project-keeper-shared-test-setup:2.9.0` to `2.9.1`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.10.1` to `3.11`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.0` to `5.9.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.0` to `5.9.1`
* Updated `org.mockito:mockito-junit-jupiter:4.7.0` to `4.8.1`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:0.4.1` to `1.0.0`
* Updated `com.exasol:project-keeper-core:2.9.0` to `2.9.1`

#### Test Dependency Updates

* Updated `com.exasol:maven-project-version-getter:1.1.1` to `1.2.0`
* Updated `com.exasol:project-keeper-shared-test-setup:2.9.0` to `2.9.1`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.0` to `5.9.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.0` to `5.9.1`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:0.4.1` to `1.0.0`
* Updated `com.exasol:project-keeper-core:2.9.0` to `2.9.1`

#### Test Dependency Updates

* Updated `com.exasol:maven-project-version-getter:1.1.1` to `1.2.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.0` to `5.9.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.0` to `5.9.1`
* Updated `org.mockito:mockito-core:4.7.0` to `4.8.1`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:0.4.1` to `1.0.0`
* Updated `com.exasol:project-keeper-shared-model-classes:2.9.0` to `2.9.1`
* Updated `org.eclipse.jgit:org.eclipse.jgit:6.2.0.202206071550-r` to `6.3.0.202209071007-r`

#### Test Dependency Updates

* Updated `com.exasol:maven-project-version-getter:1.1.1` to `1.2.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.0` to `5.9.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.0` to `5.9.1`
* Updated `org.mockito:mockito-core:4.7.0` to `4.8.1`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.0` to `2.9.1`
* Updated `org.yaml:snakeyaml:1.32` to `1.33`
