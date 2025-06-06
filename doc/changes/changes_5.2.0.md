# Project Keeper 5.2.0, released 2025-??-??

Code name: Maven Central Portal Deployment

## Summary

This release updates the Maven Central release process to use the new Central Portal instead of the deprecated OSSRH. It also updates the next Java build from Java 23 to 24.

## Features

* #647: Migrate to Central Portal for Maven Central publishing

## Dependency Updates

### Project Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `org.eclipse.jgit:org.eclipse.jgit:7.2.0.202503040940-r` to `7.3.0.202506031305-r`

#### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter-params:5.12.2` to `5.13.0`
* Updated `org.mockito:mockito-core:5.17.0` to `5.18.0`

#### Plugin Dependency Updates

* Added `org.sonatype.central:central-publishing-maven-plugin:0.7.0`
* Removed `org.sonatype.plugins:nexus-staging-maven-plugin:1.7.0`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.1.0` to `5.2.0`
* Updated `org.xmlunit:xmlunit-core:2.10.0` to `2.10.2`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.1.0` to `5.2.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.1.0` to `5.2.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.12.2` to `5.13.0`
* Updated `org.mockito:mockito-junit-jupiter:5.17.0` to `5.18.0`
* Updated `org.xmlunit:xmlunit-matchers:2.10.0` to `2.10.2`

#### Plugin Dependency Updates

* Added `org.sonatype.central:central-publishing-maven-plugin:0.7.0`
* Removed `org.sonatype.plugins:nexus-staging-maven-plugin:1.7.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.1.0` to `5.2.0`
* Updated `org.apache.maven:maven-model:3.9.9` to `3.9.10`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.1.0` to `5.2.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.12.2` to `5.13.0`

#### Plugin Dependency Updates

* Added `org.sonatype.central:central-publishing-maven-plugin:0.7.0`
* Removed `org.sonatype.plugins:nexus-staging-maven-plugin:1.7.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.1.0` to `5.2.0`

#### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter-params:5.12.2` to `5.13.0`
* Updated `org.mockito:mockito-core:5.17.0` to `5.18.0`
* Updated `org.xmlunit:xmlunit-matchers:2.10.0` to `2.10.2`

#### Plugin Dependency Updates

* Added `org.sonatype.central:central-publishing-maven-plugin:0.7.0`
* Removed `org.sonatype.plugins:nexus-staging-maven-plugin:1.7.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.1.0` to `5.2.0`
* Updated `org.eclipse.jgit:org.eclipse.jgit:7.2.0.202503040940-r` to `7.3.0.202506031305-r`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.1.0` to `5.2.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.12.2` to `5.13.0`
* Updated `org.mockito:mockito-core:5.17.0` to `5.18.0`
* Updated `org.mockito:mockito-junit-jupiter:5.17.0` to `5.18.0`
* Updated `org.xmlunit:xmlunit-matchers:2.10.0` to `2.10.2`

#### Plugin Dependency Updates

* Added `org.sonatype.central:central-publishing-maven-plugin:0.7.0`
* Removed `org.sonatype.plugins:nexus-staging-maven-plugin:1.7.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.1.0` to `5.2.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.2` to `3.1.4`
