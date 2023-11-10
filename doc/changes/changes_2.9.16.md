# Project Keeper 2.9.16, released 2023-??-??

Code name: Support Matrix builds & enable compiler warnings

## Summary

This release enables linter warnings for the Java compiler by adding argument `-Xlint:all`. This will log warnings but the build won't fail.

## Features

* #497: Enabled linter warnings

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.2` to `3.15.3`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.0` to `5.10.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.0` to `5.10.1`
* Updated `org.mockito:mockito-core:5.6.0` to `5.7.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.6.0` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.1.2` to `3.2.2`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.15` to `2.9.16`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.15` to `2.9.16`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.15` to `2.9.16`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.2` to `3.15.3`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.0` to `5.10.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.0` to `5.10.1`
* Updated `org.mockito:mockito-junit-jupiter:5.6.0` to `5.7.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.1.2` to `3.2.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.6.0` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.1.2` to `3.2.2`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.15` to `2.9.16`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.15` to `2.9.16`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.0` to `5.10.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.0` to `5.10.1`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.1.2` to `3.2.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.6.0` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.1.2` to `3.2.2`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.15` to `2.9.16`

#### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.0` to `5.10.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.0` to `5.10.1`
* Updated `org.mockito:mockito-core:5.6.0` to `5.7.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.1.2` to `3.2.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.6.0` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.10.1` to `3.10.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.1.2` to `3.2.2`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.15` to `2.9.16`

#### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.0` to `5.10.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.0` to `5.10.1`
* Updated `org.mockito:mockito-core:5.6.0` to `5.7.0`
* Updated `org.mockito:mockito-junit-jupiter:5.6.0` to `5.7.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.1.2` to `3.2.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.6.0` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.10.1` to `3.10.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.1.2` to `3.2.2`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.15` to `2.9.16`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.1.2` to `3.2.2`
