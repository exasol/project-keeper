# Project Keeper 5.2.3, released 2025-07-30

Code name: Fix dependency update Pull Request creation

## Summary

This release fixes an issue that prevented Dependency Update Pull Requests to be automatically created when security
issues were detected. It also fixes CVE-2025-48924 in transitive dependency `org.apache.commons:commons-lang3:jar:3.14.0:compile`

## Bugfixes

* #663: Dependency update PR are not created

## Security

* #661: Fix CVE-2025-48924 in `org.apache.commons:commons-lang3:jar:3.14.0:compile`

## Dependency Updates

### Project Keeper Shared Model Classes

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:4.0` to `4.0.6`
* Updated `org.junit.jupiter:junit-jupiter-params:5.13.0` to `5.13.4`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.4`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.2.2` to `5.2.3`
* Updated `org.snakeyaml:snakeyaml-engine:2.9` to `2.10`
* Updated `org.xmlunit:xmlunit-core:2.10.2` to `2.10.3`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.2.2` to `5.2.3`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.2.2` to `5.2.3`
* Updated `nl.jqno.equalsverifier:equalsverifier:4.0` to `4.0.6`
* Updated `org.junit.jupiter:junit-jupiter-params:5.13.0` to `5.13.4`
* Updated `org.xmlunit:xmlunit-matchers:2.10.2` to `2.10.3`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.4`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.2.2` to `5.2.3`
* Updated `org.apache.maven:maven-model:3.9.10` to `3.9.11`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.2.2` to `5.2.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.13.0` to `5.13.4`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.4`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.2.2` to `5.2.3`

#### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter-params:5.13.0` to `5.13.4`
* Updated `org.xmlunit:xmlunit-matchers:2.10.2` to `2.10.3`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.4`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.2.2` to `5.2.3`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.2.2` to `5.2.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.13.0` to `5.13.4`
* Updated `org.xmlunit:xmlunit-matchers:2.10.2` to `2.10.3`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.4`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.2.2` to `5.2.3`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.4`
