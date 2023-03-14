# Project Keeper 2.9.4, released 2023-??-??

Code name: Non-maven Improvement

## Summary

Changed GitHub workflow file `project-keeper-verify.yml` for non-maven projects to include step `actions/setup-node` only if the project contains an NPM module.

Before the missing file `package-lock.json` caused an error message.

## Features

* #428: Fixed failure of GitHub Workflow `project-keeper-verify.yml` when no NPM modules are present
* #430: Updated dependencies

## Dependency Updates

### Project-Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:1.0.0` to `1.0.1`
* Updated `org.eclipse.jgit:org.eclipse.jgit:6.4.0.202211300538-r` to `6.5.0.202303070854-r`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.12.4` to `3.14`
* Updated `org.mockito:mockito-core:5.1.1` to `5.2.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.0.0` to `3.1.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.1.0` to `3.2.1`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:1.0.0` to `1.0.1`
* Updated `com.exasol:project-keeper-shared-model-classes:2.9.3` to `2.9.4`
* Updated `org.yaml:snakeyaml:1.33` to `2.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.3` to `2.9.4`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.3` to `2.9.4`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.12.4` to `3.14`
* Updated `org.mockito:mockito-junit-jupiter:5.1.1` to `5.2.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.0.0` to `3.1.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.1.0` to `3.2.1`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:1.0.0` to `1.0.1`
* Updated `com.exasol:project-keeper-core:2.9.3` to `2.9.4`
* Updated `org.apache.maven:maven-model:3.8.7` to `3.9.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.3` to `2.9.4`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.4.2` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.0.0` to `3.1.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.1.0` to `3.2.1`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:1.0.0` to `1.0.1`
* Updated `com.exasol:project-keeper-core:2.9.3` to `2.9.4`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.1.1` to `5.2.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.0.0` to `3.1.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.1.0` to `3.2.1`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.7.1` to `3.8.1`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:1.0.0` to `1.0.1`
* Updated `com.exasol:project-keeper-shared-model-classes:2.9.3` to `2.9.4`
* Updated `org.eclipse.jgit:org.eclipse.jgit:6.4.0.202211300538-r` to `6.5.0.202303070854-r`

#### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.1.1` to `5.2.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.0.0` to `3.1.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.1.0` to `3.2.1`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.7.1` to `3.8.1`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.3` to `2.9.4`
* Updated `org.yaml:snakeyaml:1.33` to `2.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.1.0` to `3.2.1`
