# Project Keeper 3.0.0, released 2023-12-18

Code name: Support Java 17 builds

## Summary

This release adds support for using different Java versions for running Maven and for compiling/testing projects. We use the `maven-toolchains-plugin` to implement this. See [requirements](../system_requirements.md#support-building-with-multiple-java-versions) and [software design](../design.md#use-maven-toolchain) for implementation details.

This is a breaking change as it requires PK users to install both JDK versions 11 and 17 and create `~/.m2/toolchains.xml`. See the [user guide](../user_guide/user_guide.md#prerequisites-for-using-project-keeper) and [troubleshooting](../user_guide/user_guide.md#troubleshooting) for details.

## Features

* #508: Use Toolchain Plugin for Maven builds

## Dependency Updates

### Project Keeper Shared Model Classes

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.3` to `3.15.4`
* Updated `org.mockito:mockito-core:5.7.0` to `5.8.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.2` to `3.2.3`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.17` to `3.0.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.17` to `3.0.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.17` to `3.0.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.3` to `3.15.4`
* Updated `org.mockito:mockito-junit-jupiter:5.7.0` to `5.8.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.2` to `3.2.3`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.2` to `3.2.3`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.17` to `3.0.0`
* Updated `org.apache.maven:maven-model:3.9.5` to `3.9.6`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.17` to `3.0.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.2` to `3.2.3`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.2` to `3.2.3`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.17` to `3.0.0`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.7.0` to `5.8.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.2` to `3.2.3`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.2` to `3.2.3`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.17` to `3.0.0`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.7.0` to `5.8.0`
* Updated `org.mockito:mockito-junit-jupiter:5.7.0` to `5.8.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.2` to `3.2.3`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.2` to `3.2.3`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.17` to `3.0.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.2` to `3.2.3`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`
