# Project Keeper 4.5.0, released 2024-11-22

Code name: Fix java customization

## Summary

This release allows customization of the java version in `actions/setup-java` steps. It also migrates generated workflows `broken_links_checker.yml` and `ci-build-next-java.yml` into `ci-build.yml`. This allows simplifying branch protection rules. Now only `build` is required, you can remove `next-java-compatibility` and `linkChecker`.

## Features

* #602: Fixed customization of java version in `actions/setup-java`
* #597: Migrate workflows `broken_links_checker.yml` and `ci-build-next-java.yml` into `ci-build.yml`

## Bugfixes

* #598: Reverted changes to PK's template for IDE Eclipse `javax.annotation.Nonnull`

## Refactoring

* #596: Fixed build with Java 21

## Dependency Updates

### Project Keeper Root Project

#### Plugin Dependency Updates

* Updated `org.itsallcode:openfasttrace-maven-plugin:2.2.0` to `2.3.0`

### Project Keeper Shared Model Classes

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.17.1` to `3.17.3`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.2` to `3.1.3`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.10.1` to `3.11.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.9.1` to `3.21.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.1` to `3.5.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.17.1` to `2.18.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `5.0.0.4389`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.4.0` to `4.5.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:4.4.0` to `4.5.0`

#### Test Dependency Updates

* Updated `com.exasol:maven-project-version-getter:1.2.0` to `1.2.1`
* Updated `com.exasol:project-keeper-shared-test-setup:4.4.0` to `4.5.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.17.1` to `3.17.3`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.2` to `3.1.3`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.1` to `3.5.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.10.1` to `3.11.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.9.1` to `3.21.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.1` to `3.5.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.17.1` to `2.18.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `5.0.0.4389`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.4.0` to `4.5.0`

#### Test Dependency Updates

* Updated `com.exasol:maven-project-version-getter:1.2.0` to `1.2.1`
* Updated `com.exasol:project-keeper-shared-test-setup:4.4.0` to `4.5.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.2` to `3.1.3`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.1` to `3.5.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.10.1` to `3.11.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.9.1` to `3.21.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.1` to `3.5.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.17.1` to `2.18.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `5.0.0.4389`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.4.0` to `4.5.0`

#### Test Dependency Updates

* Updated `com.exasol:maven-project-version-getter:1.2.0` to `1.2.1`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.8.0` to `3.8.1`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.2` to `3.1.3`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.1` to `3.5.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.10.1` to `3.11.1`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.15.0` to `3.15.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.9.1` to `3.21.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.1` to `3.5.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.17.1` to `2.18.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `5.0.0.4389`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.4.0` to `4.5.0`

#### Test Dependency Updates

* Updated `com.exasol:maven-project-version-getter:1.2.0` to `1.2.1`
* Added `com.exasol:project-keeper-shared-test-setup:4.5.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.8.0` to `3.8.1`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.2` to `3.1.3`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.1` to `3.5.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.10.1` to `3.11.1`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.15.0` to `3.15.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.9.1` to `3.21.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.1` to `3.5.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.17.1` to `2.18.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `5.0.0.4389`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.4.0` to `4.5.0`

#### Plugin Dependency Updates

* Added `org.apache.maven.plugins:maven-javadoc-plugin:3.11.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.9.1` to `3.21.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.1` to `3.5.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.17.1` to `2.18.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `5.0.0.4389`
