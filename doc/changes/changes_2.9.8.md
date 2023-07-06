# Project Keeper 2.9.8, released 2023-07-06

Code name: Broken Links Checker and Security Update

## Summary

This release fixes security issues CVE-2023-28840, CVE-2023-28842, and CVE-2023-28841 reported by dependabot, all caused by vulnerable versions of components referenced in test resource `project-keeper/src/test/resources/go.mod`. The current release fixes these issues by renaming the test resource to `sample-contents-for-go.mod`.

Also the release adds two more exceptions for broken links checker as `eclipse.org` seems to block requests.

## Bugfixes

* #451: Fixed issues reported by dependabot
* #453: Added exceptions for broken links checker

## Refactoring

* #449: Migrated to latest yasson version

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `jakarta.json:jakarta.json-api:2.1.1` to `2.1.2`
* Updated `org.eclipse.jgit:org.eclipse.jgit:6.5.0.202303070854-r` to `6.6.0.202305301015-r`
* Updated `org.eclipse:yasson:2.0.4` to `3.0.3`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.14.1` to `3.14.3`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.2` to `5.9.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.2` to `5.9.3`
* Updated `org.mockito:mockito-core:5.3.0` to `5.4.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.3` to `1.2.4`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.0.1` to `3.1.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0` to `3.1.2`
* Updated `org.basepom.maven:duplicate-finder-maven-plugin:1.5.1` to `2.0.1`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.4.1` to `1.5.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.15.0` to `2.16.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.9` to `0.8.10`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.7` to `2.9.8`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.7` to `2.9.8`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.7` to `2.9.8`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.14.1` to `3.14.3`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.2` to `5.9.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.2` to `5.9.3`
* Updated `org.mockito:mockito-junit-jupiter:5.3.0` to `5.4.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.3` to `1.2.4`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0` to `3.1.2`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.0.1` to `3.1.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0` to `3.1.2`
* Updated `org.basepom.maven:duplicate-finder-maven-plugin:1.5.1` to `2.0.1`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.4.1` to `1.5.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.15.0` to `2.16.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.9` to `0.8.10`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.7` to `2.9.8`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.7` to `2.9.8`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.2` to `5.9.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.2` to `5.9.3`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.3` to `1.2.4`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0` to `3.1.2`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.0.1` to `3.1.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0` to `3.1.2`
* Updated `org.basepom.maven:duplicate-finder-maven-plugin:1.5.1` to `2.0.1`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.4.1` to `1.5.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.15.0` to `2.16.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.9` to `0.8.10`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.7` to `2.9.8`

#### Test Dependency Updates

* Updated `org.jacoco:org.jacoco.agent:0.8.9` to `0.8.10`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.2` to `5.9.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.2` to `5.9.3`
* Updated `org.mockito:mockito-core:5.3.0` to `5.4.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.3` to `1.2.4`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.5.0` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0` to `3.1.2`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.0.1` to `3.1.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0` to `3.1.2`
* Updated `org.basepom.maven:duplicate-finder-maven-plugin:1.5.1` to `2.0.1`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.4.1` to `1.5.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.15.0` to `2.16.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.9` to `0.8.10`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.7` to `2.9.8`
* Updated `org.eclipse.jgit:org.eclipse.jgit:6.5.0.202303070854-r` to `6.6.0.202305301015-r`

#### Test Dependency Updates

* Updated `org.jacoco:org.jacoco.agent:0.8.9` to `0.8.10`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.2` to `5.9.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.2` to `5.9.3`
* Updated `org.mockito:mockito-core:5.3.0` to `5.4.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.3` to `1.2.4`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.5.0` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0` to `3.1.2`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.0.1` to `3.1.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0` to `3.1.2`
* Updated `org.basepom.maven:duplicate-finder-maven-plugin:1.5.1` to `2.0.1`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.4.1` to `1.5.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.15.0` to `2.16.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.9` to `0.8.10`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.7` to `2.9.8`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.3` to `1.2.4`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0` to `3.1.2`
* Updated `org.basepom.maven:duplicate-finder-maven-plugin:1.5.1` to `2.0.1`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.4.1` to `1.5.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.15.0` to `2.16.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.9` to `0.8.10`
