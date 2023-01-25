# Project Keeper 2.9.2, released 2023-01-25

Code name: Fixed Self Update

## Summary

PK checks for updates at Maven Central to update itself. With this release PK accesses XML file `maven-metadata.xml` rather than using the [Maven Central REST API](https://central.sonatype.org/search/rest-api-guide/) which showed sporadic failures lately.

Additionally updated template for build script `.github/workflows/broken_links_checker.yml` to exclude `mysql.com` as this site seems to block requests from `github.com` and fixed a bug in support for NPM projects.

## Bugfixes

* #408: Changed method to inquire latest version of PK for self-update in file `pom.xml`.
* #409: Updated broken-links checker configuration
* #403: Added `npm ci` before checking npm licenses

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `org.eclipse.jgit:org.eclipse.jgit:6.3.0.202209071007-r` to `6.4.0.202211300538-r`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.11` to `3.12.3`
* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.1` to `1.2.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.13.0` to `2.14.2`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.1` to `2.9.2`
* Updated `javax.xml.bind:jaxb-api:2.3.1` to `2.4.0-b180830.0359`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.1` to `2.9.2`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.1` to `2.9.2`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.11` to `3.12.3`
* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.1` to `1.2.2`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.2.2` to `3.3.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.13.0` to `2.14.2`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.1` to `2.9.2`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.1` to `2.9.2`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.1` to `1.2.2`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.13.0` to `2.14.2`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.1` to `2.9.2`

#### Test Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.1` to `1.2.2`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.3.0` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.6.4` to `3.7.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.13.0` to `2.14.2`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.1` to `2.9.2`
* Updated `org.eclipse.jgit:org.eclipse.jgit:6.3.0.202209071007-r` to `6.4.0.202211300538-r`

#### Test Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.1` to `1.2.2`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.3.0` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.13.0` to `2.14.2`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.1` to `2.9.2`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.1` to `1.2.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.13.0` to `2.14.2`
