# Project Keeper 2.4.2, released 2022-??-??

Code name: Command line interface

## Summary

This release adds a standalone mode with a command line interface that allows analyzing non-Maven projects on the command line without using the Maven-plugin.

**Note:** Analyzing Maven projects in standalone mode is not supported. Use `project-keeper-maven-plugin` as usual for Maven projects.

## Features

* #277: Added command line interface

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Test Dependency Updates

* Added `org.mockito:mockito-core:4.5.1`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.0` to `1.1.1`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.3.2` to `2.4.2`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.3.2` to `2.4.2`

#### Test Dependency Updates

* Added `com.exasol:project-keeper-shared-test-setup:2.4.2`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.0` to `1.1.1`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Added `com.exasol:error-reporting-java:0.4.1`
* Added `com.exasol:project-keeper-core:2.4.2`

#### Runtime Dependency Updates

* Added `org.slf4j:slf4j-jdk14:1.7.36`

#### Test Dependency Updates

* Added `com.exasol:project-keeper-shared-test-setup:2.4.2`
* Added `org.apache.maven:maven-model:3.8.5`
* Added `org.hamcrest:hamcrest:2.2`
* Added `org.junit.jupiter:junit-jupiter-engine:5.8.2`
* Added `org.junit.jupiter:junit-jupiter-params:5.8.2`

#### Plugin Dependency Updates

* Added `com.exasol:artifact-reference-checker-maven-plugin:0.4.0`
* Added `com.exasol:error-code-crawler-maven-plugin:1.1.1`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.15`
* Added `org.apache.maven.plugins:maven-assembly-plugin:3.3.0`
* Added `org.apache.maven.plugins:maven-clean-plugin:2.5`
* Added `org.apache.maven.plugins:maven-compiler-plugin:3.9.0`
* Added `org.apache.maven.plugins:maven-deploy-plugin:3.0.0-M1`
* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0`
* Added `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M5`
* Added `org.apache.maven.plugins:maven-gpg-plugin:3.0.1`
* Added `org.apache.maven.plugins:maven-install-plugin:2.4`
* Added `org.apache.maven.plugins:maven-jar-plugin:3.2.0`
* Added `org.apache.maven.plugins:maven-javadoc-plugin:3.3.1`
* Added `org.apache.maven.plugins:maven-resources-plugin:2.6`
* Added `org.apache.maven.plugins:maven-site-plugin:3.3`
* Added `org.apache.maven.plugins:maven-source-plugin:3.2.1`
* Added `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M5`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.2.7`
* Added `org.codehaus.mojo:versions-maven-plugin:2.8.1`
* Added `org.jacoco:jacoco-maven-plugin:0.8.7`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`
* Added `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.1.0`
* Added `org.sonatype.plugins:nexus-staging-maven-plugin:1.6.8`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.3.2` to `2.4.2`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.0` to `1.1.1`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.3.2` to `2.4.2`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.0` to `1.1.1`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Added `com.exasol:project-keeper-shared-model-classes:2.4.2`
* Added `org.hamcrest:hamcrest:2.2`
* Added `org.yaml:snakeyaml:1.30`

#### Plugin Dependency Updates

* Added `com.exasol:error-code-crawler-maven-plugin:1.1.1`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.15`
* Added `org.apache.maven.plugins:maven-clean-plugin:2.5`
* Added `org.apache.maven.plugins:maven-compiler-plugin:3.9.0`
* Added `org.apache.maven.plugins:maven-deploy-plugin:2.7`
* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0`
* Added `org.apache.maven.plugins:maven-install-plugin:2.4`
* Added `org.apache.maven.plugins:maven-jar-plugin:2.4`
* Added `org.apache.maven.plugins:maven-resources-plugin:2.6`
* Added `org.apache.maven.plugins:maven-site-plugin:3.3`
* Added `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M5`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.2.7`
* Added `org.codehaus.mojo:versions-maven-plugin:2.8.1`
* Added `org.jacoco:jacoco-maven-plugin:0.8.7`
* Added `org.projectlombok:lombok-maven-plugin:1.18.20.0`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`
* Added `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.1.0`
