# Project Keeper 5.7.0, released 2026-??-??

Code name: Generate SBOM

## Summary

This release generates and attaches SBOMs for all artifacts published to Maven Central and for JAR artifacts attached to GitHub releases.

The release also relaxes requirements for the release date. Release builds will now also run when the release date in the current changelog is not older than five days.

The release also replaces the old Java compiler flags `-source` and `-target` with `--release`. See the [maven plugin documentation](https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-release.html) for details.

The release also removes the deprecated Maven Plugin `quality-summarizer-maven-plugin` we used to generate `metrics.json`. This file is no longer added to releases.

The release also deactivates the new JavaDoc option `--no-fonts` that is only supported with JDK >= 23. The release also specifies the new `--release` option for JavaDoc instead of `-source` and explicitly specifies `-locale en`.

The release also removes the deprecated Maven Plugin `quality-summarizer-maven-plugin` we used to generate `metrics.json`. This file is no longer added to releases.

## Features

* #739: Generate and attach SBOM
* #698: Allowed `verify-release` changelog dates from today minus 5 days through today.

## Bugfixes

* #738: Use compiler flag `--release` instead of `-source` and `-target`
* #745: Fix javadoc error caused by unsupported `--no-fonts` option.

## Refactoring

* #741: Removed deprecated `quality-summarizer-maven-plugin`

## Dependency Updates

### Project Keeper Root Project

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.2` to `3.6.3`
* Updated `org.itsallcode:openfasttrace-maven-plugin:2.3.0` to `2.3.1`

### Project Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `jakarta.json.bind:jakarta.json.bind-api:3.0.1` to `3.0.2`
* Updated `org.eclipse.jgit:org.eclipse.jgit:7.6.0.202603022253-r` to `7.7.0.202606012155-r`

#### Test Dependency Updates

* Updated `org.itsallcode:junit5-system-extensions:1.2.2` to `1.2.3`
* Updated `org.junit.jupiter:junit-jupiter:6.0.3` to `6.1.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.17` to `2.0.18`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.7` to `2.1.0`
* Removed `com.exasol:quality-summarizer-maven-plugin:0.2.1`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.10.0` to `3.11.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.21.0` to `3.22.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.5` to `3.5.6`
* Added `org.codehaus.mojo:build-helper-maven-plugin:3.6.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.14` to `0.8.15`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.5.0.6356` to `5.7.0.6970`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.10.0` to `0.11.0`
* Added `org.spdx:spdx-maven-plugin:1.0.3`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.6.2` to `5.7.0`
* Updated `com.jcabi:jcabi-github:1.10.0` to `1.11.1`
* Updated `org.xmlunit:xmlunit-core:2.11.0` to `2.12.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.6.2` to `5.7.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.6.2` to `5.7.0`
* Updated `org.junit.jupiter:junit-jupiter:6.0.3` to `6.1.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.17` to `2.0.18`
* Updated `org.xmlunit:xmlunit-matchers:2.11.0` to `2.12.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.7` to `2.1.0`
* Removed `com.exasol:quality-summarizer-maven-plugin:0.2.1`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.10.0` to `3.11.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.5` to `3.5.6`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.21.0` to `3.22.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.5` to `3.5.6`
* Added `org.codehaus.mojo:build-helper-maven-plugin:3.6.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.14` to `0.8.15`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.5.0.6356` to `5.7.0.6970`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.10.0` to `0.11.0`
* Added `org.spdx:spdx-maven-plugin:1.0.3`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.6.2` to `5.7.0`
* Updated `org.apache.maven:maven-model:3.9.15` to `3.9.16`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-api:2.0.17` to `2.0.18`
* Updated `org.slf4j:slf4j-jdk14:2.0.17` to `2.0.18`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.6.2` to `5.7.0`
* Updated `org.junit.jupiter:junit-jupiter:6.0.3` to `6.1.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.7` to `2.1.0`
* Removed `com.exasol:quality-summarizer-maven-plugin:0.2.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.5` to `3.5.6`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.21.0` to `3.22.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.5` to `3.5.6`
* Added `org.codehaus.mojo:build-helper-maven-plugin:3.6.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.14` to `0.8.15`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.5.0.6356` to `5.7.0.6970`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.10.0` to `0.11.0`
* Added `org.spdx:spdx-maven-plugin:1.0.3`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.6.2` to `5.7.0`

#### Test Dependency Updates

* Updated `org.jacoco:org.jacoco.agent:0.8.14` to `0.8.15`
* Updated `org.junit.jupiter:junit-jupiter:6.0.3` to `6.1.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.17` to `2.0.18`
* Updated `org.xmlunit:xmlunit-matchers:2.11.0` to `2.12.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.7` to `2.1.0`
* Removed `com.exasol:quality-summarizer-maven-plugin:0.2.1`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.10.0` to `3.11.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.5` to `3.5.6`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.21.0` to `3.22.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.5` to `3.5.6`
* Added `org.codehaus.mojo:build-helper-maven-plugin:3.6.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.14` to `0.8.15`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.5.0.6356` to `5.7.0.6970`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.10.0` to `0.11.0`
* Added `org.spdx:spdx-maven-plugin:1.0.3`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.6.2` to `5.7.0`
* Updated `org.eclipse.jgit:org.eclipse.jgit:7.6.0.202603022253-r` to `7.7.0.202606012155-r`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.6.2` to `5.7.0`
* Updated `org.jacoco:org.jacoco.agent:0.8.14` to `0.8.15`
* Updated `org.junit.jupiter:junit-jupiter:6.0.3` to `6.1.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.17` to `2.0.18`
* Updated `org.xmlunit:xmlunit-matchers:2.11.0` to `2.12.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.7` to `2.1.0`
* Removed `com.exasol:quality-summarizer-maven-plugin:0.2.1`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.10.0` to `3.11.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.5` to `3.5.6`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.21.0` to `3.22.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.5` to `3.5.6`
* Added `org.codehaus.mojo:build-helper-maven-plugin:3.6.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.14` to `0.8.15`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.5.0.6356` to `5.7.0.6970`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.10.0` to `0.11.0`
* Added `org.spdx:spdx-maven-plugin:1.0.3`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.6.2` to `5.7.0`

#### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter:6.0.3` to `6.1.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.7` to `2.1.0`
* Removed `com.exasol:quality-summarizer-maven-plugin:0.2.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.21.0` to `3.22.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.5` to `3.5.6`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.14` to `0.8.15`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.5.0.6356` to `5.7.0.6970`
