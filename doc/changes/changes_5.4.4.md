# Project Keeper 5.4.4, released 2025-12-17

Code name: Java 21 support

## Summary

This release adds support for Java 21 removing a warning when running tests that use Mockito.

The release also fixes the following error with duplicate licenses in NPM and Go projects:

```
java.lang.IllegalStateException: Duplicate key string-width (attempted merging values [com.exasol.projectkeeper.sources.analyze.npm.NpmLicense@409c54f] and [com.exasol.projectkeeper.sources.analyze.npm.NpmLicense@3e74829])
```

The release also updates the Eclipse formatter to allow line length 160 in comments.

## Features

* #689: Configure Mockito agent
* #702: Adapted Eclipse formatter settings to allow wider comment lines

## Bugfixes

* #701: Fixed "Duplicate key" error when collecting NPM licenses
* #692: Fixed "Duplicate key" error when collecting Go licenses

## Dependency Updates

### Project Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:1.0.1` to `1.0.2`
* Updated `org.eclipse.jgit:org.eclipse.jgit:7.4.0.202509020913-r` to `7.5.0.202512021534-r`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:4.2` to `4.2.5`
* Updated `org.junit.jupiter:junit-jupiter-params:6.0.0` to `6.0.1`
* Updated `org.mockito:mockito-core:5.20.0` to `5.21.0`

#### Plugin Dependency Updates

* Added `org.apache.maven.plugins:maven-dependency-plugin:3.9.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.3.1` to `3.4.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.19.1` to `2.20.1`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.2.0.4988` to `5.5.0.6356`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:1.0.1` to `1.0.2`
* Updated `com.exasol:project-keeper-shared-model-classes:5.4.3` to `5.4.4`
* Updated `com.jcabi:jcabi-github:1.9.1` to `1.10.0`
* Updated `org.snakeyaml:snakeyaml-engine:2.10` to `3.0.1`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.4.3` to `5.4.4`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.3` to `1.1.4`
* Updated `com.exasol:maven-project-version-getter:1.2.1` to `1.2.2`
* Updated `com.exasol:project-keeper-shared-test-setup:5.4.3` to `5.4.4`
* Updated `nl.jqno.equalsverifier:equalsverifier:4.2` to `4.2.5`
* Updated `org.junit.jupiter:junit-jupiter-params:6.0.0` to `6.0.1`
* Updated `org.mockito:mockito-junit-jupiter:5.20.0` to `5.21.0`

#### Plugin Dependency Updates

* Added `org.apache.maven.plugins:maven-dependency-plugin:3.9.0`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.4.2` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.3.1` to `3.4.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.19.1` to `2.20.1`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.2.0.4988` to `5.5.0.6356`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:1.0.1` to `1.0.2`
* Updated `com.exasol:project-keeper-core:5.4.3` to `5.4.4`

#### Test Dependency Updates

* Updated `com.exasol:maven-project-version-getter:1.2.1` to `1.2.2`
* Updated `com.exasol:project-keeper-shared-test-setup:5.4.3` to `5.4.4`
* Updated `org.junit.jupiter:junit-jupiter-params:6.0.0` to `6.0.1`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.7.1` to `3.8.0`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.4.2` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.3.1` to `3.4.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.19.1` to `2.20.1`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.2.0.4988` to `5.5.0.6356`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:1.0.1` to `1.0.2`
* Updated `com.exasol:project-keeper-core:5.4.3` to `5.4.4`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.3` to `1.1.4`
* Updated `com.exasol:maven-project-version-getter:1.2.1` to `1.2.2`
* Updated `org.junit.jupiter:junit-jupiter-params:6.0.0` to `6.0.1`
* Updated `org.mockito:mockito-core:5.20.0` to `5.21.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-resources-plugin:3.3.1` to `3.4.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.19.1` to `2.20.1`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.2.0.4988` to `5.5.0.6356`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:1.0.1` to `1.0.2`
* Updated `com.exasol:project-keeper-shared-model-classes:5.4.3` to `5.4.4`
* Updated `org.eclipse.jgit:org.eclipse.jgit:7.4.0.202509020913-r` to `7.5.0.202512021534-r`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.3` to `1.1.4`
* Updated `com.exasol:maven-project-version-getter:1.2.1` to `1.2.2`
* Updated `com.exasol:project-keeper-shared-test-setup:5.4.3` to `5.4.4`
* Updated `org.junit.jupiter:junit-jupiter-params:6.0.0` to `6.0.1`
* Updated `org.mockito:mockito-core:5.20.0` to `5.21.0`
* Updated `org.mockito:mockito-junit-jupiter:5.20.0` to `5.21.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-resources-plugin:3.3.1` to `3.4.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.19.1` to `2.20.1`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.2.0.4988` to `5.5.0.6356`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.4.3` to `5.4.4`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-resources-plugin:3.3.1` to `3.4.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.19.1` to `2.20.1`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.2.0.4988` to `5.5.0.6356`
