# Project Keeper 5.5.1, released 2026-04-28

Code name: Fix plugin execution order

## Summary

Fixed the generated Maven plugin configuration so the Exec Maven Plugin is managed without affecting plugin execution order.

The exec plugin and other plugins added only for version pinning are now moved to the `pluginManagement` element in the generated parent pom.

## Bugfixes

* #723: Fixed plugin execution order

## Dependency Updates

### Project Keeper Shared Model Classes

#### Plugin Dependency Updates

* Removed `org.codehaus.mojo:exec-maven-plugin:3.6.3`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.5.0` to `5.5.1`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.5.0` to `5.5.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.5.0` to `5.5.1`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.5.0` to `5.5.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.5.0` to `5.5.1`

#### Plugin Dependency Updates

* Removed `org.codehaus.mojo:exec-maven-plugin:3.6.3`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.5.0` to `5.5.1`

#### Plugin Dependency Updates

* Removed `org.codehaus.mojo:exec-maven-plugin:3.6.3`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.5.0` to `5.5.1`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.5.0` to `5.5.1`

#### Plugin Dependency Updates

* Removed `org.codehaus.mojo:exec-maven-plugin:3.6.3`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.5.0` to `5.5.1`

#### Plugin Dependency Updates

* Removed `org.codehaus.mojo:exec-maven-plugin:3.6.3`
