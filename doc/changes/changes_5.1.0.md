# Project Keeper 5.1.0, released 2025-05-??

Code name: Customize more workflows

## Summary

This release allows customizing GitHub workflows `project-keeper-verify.yml` and `broken_links_checker.yml`.

## Features

* #642: Allowed customizing GitHub workflows `project-keeper-verify.yml` and `broken_links_checker.yml`

## Bugfixes

* #638: Renamed step "Report Security Issues" in GitHub workflow `dependencies_check.yml`

## Dependency Updates

### Project Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `org.eclipse.jgit:org.eclipse.jgit:7.1.0.202411261347-r` to `7.2.0.202503040940-r`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.19.1` to `4.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.12.0` to `5.12.2`
* Updated `org.mockito:mockito-core:5.16.0` to `5.17.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.2` to `3.5.3`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.12` to `0.8.13`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.0.0.4389` to `5.1.0.4751`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.0.1` to `5.1.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.0.1` to `5.1.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.0.1` to `5.1.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.19.1` to `4.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.12.0` to `5.12.2`
* Updated `org.mockito:mockito-junit-jupiter:5.16.0` to `5.17.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.2` to `3.5.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.2` to `3.5.3`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.12` to `0.8.13`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.0.0.4389` to `5.1.0.4751`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.0.1` to `5.1.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.0.1` to `5.1.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.12.0` to `5.12.2`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.2` to `3.5.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.2` to `3.5.3`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.12` to `0.8.13`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.0.0.4389` to `5.1.0.4751`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.0.1` to `5.1.0`

#### Test Dependency Updates

* Updated `org.jacoco:org.jacoco.agent:0.8.12` to `0.8.13`
* Updated `org.junit.jupiter:junit-jupiter-params:5.12.0` to `5.12.2`
* Updated `org.mockito:mockito-core:5.16.0` to `5.17.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.2` to `3.5.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.2` to `3.5.3`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.12` to `0.8.13`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.0.0.4389` to `5.1.0.4751`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.0.1` to `5.1.0`
* Updated `org.eclipse.jgit:org.eclipse.jgit:7.1.0.202411261347-r` to `7.2.0.202503040940-r`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.0.1` to `5.1.0`
* Updated `org.jacoco:org.jacoco.agent:0.8.12` to `0.8.13`
* Updated `org.junit.jupiter:junit-jupiter-params:5.12.0` to `5.12.2`
* Updated `org.mockito:mockito-core:5.16.0` to `5.17.0`
* Updated `org.mockito:mockito-junit-jupiter:5.16.0` to `5.17.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.2` to `3.5.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.2` to `3.5.3`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.12` to `0.8.13`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.0.0.4389` to `5.1.0.4751`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.0.1` to `5.1.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.2` to `3.5.3`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.12` to `0.8.13`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.0.0.4389` to `5.1.0.4751`
