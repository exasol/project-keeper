# Project Keeper 5.7.7, released 2026-10-15

Code name: Fix Maven Central Publishing

## Summary

This release fixes an issue with Maven 3.10 that cause publishing to Maven Central to fail by upgrading Maven plugin `org.sonatype.central:central-publishing-maven-plugin` to [0.12.0](https://github.com/sonatype-nexus-community/central-publishing-maven-plugin/releases/tag/release-0.12.0). See details in [central-publishing-maven-plugin#3](https://github.com/sonatype-nexus-community/central-publishing-maven-plugin/pull/3)

## Security

This release fixes vulnerabilities in the following dependencies:

* `org.ow2.asm:asm:jar:9.9.1`:
  * CVE-2026-110257
  * CVE-2026-110258
  * CVE-2026-110259
  * CVE-2026-110260
  * CVE-2026-110261
  * CVE-2026-110263
* `com.google.guava:guava:jar:33.6.0-jre`:
  * CVE-2026-102554

## Dependency Updates

### Project Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `jakarta.json.bind:jakarta.json.bind-api:3.0.3` to `3.0.4`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.23.0` to `5.24.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.19` to `2.0.20`

#### Plugin Dependency Updates

* Updated `org.codehaus.mojo:versions-maven-plugin:2.22.0` to `2.23.0`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.11.0` to `0.12.0`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.6` to `5.7.7`
* Updated `org.snakeyaml:snakeyaml-engine:3.1.1` to `3.2`
* Updated `org.xmlunit:xmlunit-core:2.13.0` to `2.14.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.7.6` to `5.7.7`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.6` to `5.7.7`
* Updated `org.mockito:mockito-junit-jupiter:5.23.0` to `5.24.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.19` to `2.0.20`
* Updated `org.xmlunit:xmlunit-matchers:2.13.0` to `2.14.0`

#### Plugin Dependency Updates

* Updated `org.codehaus.mojo:versions-maven-plugin:2.22.0` to `2.23.0`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.11.0` to `0.12.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.7.6` to `5.7.7`
* Updated `org.apache.maven:maven-model:3.9.16` to `3.10.0`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-api:2.0.19` to `2.0.20`
* Updated `org.slf4j:slf4j-jdk14:2.0.19` to `2.0.20`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.6` to `5.7.7`

#### Plugin Dependency Updates

* Updated `org.codehaus.mojo:versions-maven-plugin:2.22.0` to `2.23.0`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.11.0` to `0.12.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.7.6` to `5.7.7`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.23.0` to `5.24.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.19` to `2.0.20`
* Updated `org.xmlunit:xmlunit-matchers:2.13.0` to `2.14.0`

#### Plugin Dependency Updates

* Updated `org.codehaus.mojo:versions-maven-plugin:2.22.0` to `2.23.0`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.11.0` to `0.12.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.6` to `5.7.7`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.6` to `5.7.7`
* Updated `org.mockito:mockito-core:5.23.0` to `5.24.0`
* Updated `org.mockito:mockito-junit-jupiter:5.23.0` to `5.24.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.19` to `2.0.20`
* Updated `org.xmlunit:xmlunit-matchers:2.13.0` to `2.14.0`

#### Plugin Dependency Updates

* Updated `org.codehaus.mojo:versions-maven-plugin:2.22.0` to `2.23.0`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.11.0` to `0.12.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.6` to `5.7.7`

#### Plugin Dependency Updates

* Updated `org.codehaus.mojo:versions-maven-plugin:2.22.0` to `2.23.0`
