# Project Keeper 2.9.3, released 2023-??-??

Code name: Updated Broken-Links Checker Configuration

## Summary

Updated template for build script `.github/workflows/broken_links_checker.yml` to exclude `mysql.com` as this site seems to block requests from `github.com`.

Additionally changed strategy for inquiry of latest version of PK for self-update as [Maven Central REST API](https://central.sonatype.org/search/rest-api-guide/) showed to be very unreliable lately.

## Bugfixes

* #409: Updated broken-links checker configuration
* #408: Changed method to inquire latest version of PK for self-update in file `pom.xml`.

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `org.eclipse.jgit:org.eclipse.jgit:6.3.0.202209071007-r` to `6.4.0.202211300538-r`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.11` to `3.12.3`
* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.1` to `2.9.3`
* Updated `javax.xml.bind:jaxb-api:2.3.1` to `2.4.0-b180830.0359`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.1` to `2.9.3`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.1` to `2.9.3`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.11` to `3.12.3`
* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-jar-plugin:3.2.2` to `3.3.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.1` to `2.9.3`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.1` to `2.9.3`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.1` to `2.9.3`

#### Test Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.6.4` to `3.7.1`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.1` to `2.9.3`
* Updated `org.eclipse.jgit:org.eclipse.jgit:6.3.0.202209071007-r` to `6.4.0.202211300538-r`

#### Test Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.1` to `2.9.3`
