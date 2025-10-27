# Project Keeper 5.4.3, released 2025-10-27

Code name: Projects without sources

## Summary

This release adds support for projects without sources. PK failed for these projects with error message `No sources, can't get java versions`.

## Bugfixes

* #687: Add support for projects without sources

## Dependency Updates

### Project Keeper Root Project

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.5`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.3` to `3.1.4`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.1` to `3.6.2`

### Project Keeper Shared Model Classes

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.1` to `3.6.2`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.13` to `0.8.14`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.8.0` to `0.9.0`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.4.2` to `5.4.3`
* Updated `org.xmlunit:xmlunit-core:2.10.4` to `2.11.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.4.2` to `5.4.3`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.4.2` to `5.4.3`
* Updated `org.xmlunit:xmlunit-matchers:2.10.4` to `2.11.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.1` to `3.6.2`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.13` to `0.8.14`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.8.0` to `0.9.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.4.2` to `5.4.3`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.4.2` to `5.4.3`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.1` to `3.6.2`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.13` to `0.8.14`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.8.0` to `0.9.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.4.2` to `5.4.3`

#### Test Dependency Updates

* Updated `org.jacoco:org.jacoco.agent:0.8.13` to `0.8.14`
* Updated `org.xmlunit:xmlunit-matchers:2.10.4` to `2.11.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.8.1` to `3.9.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.1` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.4.1` to `3.4.2`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.15.1` to `3.15.2`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.13` to `0.8.14`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.8.0` to `0.9.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.4.2` to `5.4.3`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.4.2` to `5.4.3`
* Updated `org.jacoco:org.jacoco.agent:0.8.13` to `0.8.14`
* Updated `org.xmlunit:xmlunit-matchers:2.10.4` to `2.11.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.8.1` to `3.9.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.1` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.15.1` to `3.15.2`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.13` to `0.8.14`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.8.0` to `0.9.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.4.2` to `5.4.3`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.1` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.11.1` to `3.12.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.13` to `0.8.14`
