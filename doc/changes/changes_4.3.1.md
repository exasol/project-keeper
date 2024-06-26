# Project Keeper 4.3.1, released 2024-05-13

Code name: Environment for GitHub workflow `ci-build.yml`

## Summary

This release fixes vulnerability CVE-2024-31573 in `org.xmlunit:xmlunit-core:jar:2.9.1:test`.

## Security

* #570: Fixed CVE-2024-31573 in `org.xmlunit:xmlunit-core:jar:2.9.1:test`

## Features

* #566: Allowed specifying an environment for GitHub workflow `ci-build.yml`

## Bugfixes

* #571: Fixed failing version increment during dependency update
* #567: Increased timeout for installing go-licenses

## Dependency Updates

### Project Keeper Root Project

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.1` to `2.0.3`

### Project Keeper Shared Model Classes

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.11.0` to `5.12.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.2` to `2.0.3`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.1` to `3.1.2`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.2` to `3.2.4`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0` to `3.2.0`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.3.0` to `4.3.1`
* Updated `org.xmlunit:xmlunit-core:2.9.1` to `2.10.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:4.3.0` to `4.3.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.3.0` to `4.3.1`
* Updated `org.mockito:mockito-junit-jupiter:5.11.0` to `5.12.0`
* Updated `org.xmlunit:xmlunit-matchers:2.9.1` to `2.10.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.2` to `2.0.3`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.1` to `3.1.2`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.2` to `3.2.4`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.3.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0` to `3.2.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.3.0` to `4.3.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.3.0` to `4.3.1`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.2` to `2.0.3`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.1` to `3.1.2`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.2` to `3.2.4`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.3.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0` to `3.2.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.3.0` to `4.3.1`

#### Test Dependency Updates

* Updated `org.jacoco:org.jacoco.agent:0.8.11` to `0.8.12`
* Updated `org.mockito:mockito-core:5.11.0` to `5.12.0`
* Updated `org.xmlunit:xmlunit-matchers:2.9.1` to `2.10.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.2` to `2.0.3`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.1` to `3.1.2`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.2` to `3.2.4`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0` to `3.2.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.3.0` to `4.3.1`

#### Test Dependency Updates

* Updated `org.jacoco:org.jacoco.agent:0.8.11` to `0.8.12`
* Updated `org.mockito:mockito-core:5.11.0` to `5.12.0`
* Updated `org.mockito:mockito-junit-jupiter:5.11.0` to `5.12.0`
* Updated `org.xmlunit:xmlunit-matchers:2.9.1` to `2.10.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.2` to `2.0.3`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.1` to `3.1.2`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.2` to `3.2.4`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0` to `3.2.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.3.0` to `4.3.1`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.2` to `2.0.3`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0` to `3.2.0`
