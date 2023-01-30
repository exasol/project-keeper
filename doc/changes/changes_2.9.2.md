# Project Keeper 2.9.2, released 2023-01-30

Code name: Fixed Self Update and Added Validation of Release Config

## Summary

PK checks for updates at Maven Central to update itself. With this release PK accesses XML file `maven-metadata.xml` rather than using the [Maven Central REST API](https://central.sonatype.org/search/rest-api-guide/) which showed sporadic failures lately.

Added validation for publication to Maven Central. PK now reports an error in the following cases

| File `release_config.yml`                                | File `.project-keeper.yml`                           |
|----------------------------------------------------------|------------------------------------------------------|
| exists and contains release platform `maven`             | none of the sources activates module `maven_central` |
| exists but does **not contain** release platform `maven` | any of the sources activates module `maven_central`  |

Updated template for build script `.github/workflows/broken_links_checker.yml` to exclude `mysql.com` as this site seems to block requests from `github.com` and fixed a bug in support for NPM projects.

Added template for file `.gitattributes` if the file does not exist, yet.

## Bugfixes

* #408: Changed method to inquire latest version of PK for self-update in file `pom.xml`.
* #409: Updated broken-links checker configuration
* #403: Added `npm ci` before checking npm licenses
* #411: Ignore line comments and `replace` directives in `go.mod` files
* #404: Added template for file `.gitattributes`

## Features

* #407: Added validation for publication to Maven Central
* #404: Added template for file `.gitattributes`

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `org.eclipse.jgit:org.eclipse.jgit:6.3.0.202209071007-r` to `6.4.0.202211300538-r`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.11` to `3.12.3`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.1` to `5.9.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.1` to `5.9.2`
* Updated `org.mockito:mockito-core:4.8.1` to `5.0.0`
* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.1` to `1.2.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.13.0` to `2.14.2`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.1` to `2.9.2`
* Updated `javax.xml.bind:jaxb-api:2.3.1` to `2.4.0-b180830.0359`
* Updated `org.xmlunit:xmlunit-core:2.9.0` to `2.9.1`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.1` to `2.9.2`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.1` to `2.9.2`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.11` to `3.12.3`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.1` to `5.9.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.1` to `5.9.2`
* Updated `org.mockito:mockito-junit-jupiter:4.8.1` to `5.0.0`
* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`
* Updated `org.xmlunit:xmlunit-matchers:2.9.0` to `2.9.1`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.1` to `1.2.2`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.2.2` to `3.3.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.13.0` to `2.14.2`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.1` to `2.9.2`
* Updated `org.apache.maven:maven-model:3.8.6` to `3.8.7`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.1` to `2.9.2`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.1` to `5.9.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.1` to `5.9.2`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.1` to `1.2.2`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.13.0` to `2.14.2`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.1` to `2.9.2`

#### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.1` to `5.9.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.1` to `5.9.2`
* Updated `org.mockito:mockito-core:4.8.1` to `5.0.0`
* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`
* Updated `org.xmlunit:xmlunit-matchers:2.9.0` to `2.9.1`

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

* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.1` to `5.9.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.1` to `5.9.2`
* Updated `org.mockito:mockito-core:4.8.1` to `5.0.0`
* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`
* Updated `org.xmlunit:xmlunit-matchers:2.9.0` to `2.9.1`

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
