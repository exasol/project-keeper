# Project Keeper 5.7.6, released 2026-??-??

Code name:

## Summary

This release fixes customization of the generated `project-keeper-verify.yml` workflow for non-Maven projects. Instead of validating workflow names in `.project-keeper.yml` against an allow-list, PK now verifies that each customization is actually used. This ensures that newly added workflows are automatically allowed.

The release also allows disabling the Eclipse formatter for certain code regions by adding comments `// @formatter:off` and `// @formatter:on`. The relevant setting `org.eclipse.jdt.core.formatter.use_on_off_tags` was `false` before.

The release also fixes linter warnings for generated GitHub workflows in the latest zizmor version 1.30.1 and reduces permissions for the linter job.

**Note:** This release upgrades the `maven-failsafe-plugin` to 3.6.0. Builds that skip tests with `-DskipTests` must now also set `-DskipITs` to skip Failsafe integration tests.

## Features

## Bug Fixes

* #775: Fixed customizing GitHub workflow `project-keeper-verify.yml`
* #780: Fixed linter warnings with latest zizmor version 1.30.1

## Features

* #766: Update Maven template plugins and Project Keeper's self-version check to the latest stable release only.
* #778: Allowed disabling Eclipse formatter via tags

## Dependency Updates

### Project Keeper Root Project

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.0` to `2.1.2`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.4` to `3.2.0`
* Updated `org.itsallcode:openfasttrace-maven-plugin:2.3.1` to `3.0.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`

### Project Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `org.eclipse.jgit:org.eclipse.jgit:7.7.1.202607240634-r` to `7.8.0.202609011348-r`

#### Test Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.19`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.1` to `2.1.2`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.1` to `3.7.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.codehaus.mojo:build-helper-maven-plugin:3.6.1` to `3.6.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.21.0` to `2.22.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.5` to `5.7.6`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.7.5` to `5.7.6`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.5` to `5.7.6`
* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.19`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.1` to `2.1.2`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.1` to `3.7.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.codehaus.mojo:build-helper-maven-plugin:3.6.1` to `3.6.2`
* Updated `org.codehaus.mojo:exec-maven-plugin:3.6.3` to `3.6.4`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.21.0` to `2.22.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.7.5` to `5.7.6`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-api:2.0.18` to `2.0.19`
* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.19`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.5` to `5.7.6`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.1` to `2.1.2`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.1` to `3.7.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.codehaus.mojo:build-helper-maven-plugin:3.6.1` to `3.6.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.21.0` to `2.22.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.7.5` to `5.7.6`

#### Test Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.19`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.1` to `2.1.2`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.1` to `3.7.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.codehaus.mojo:build-helper-maven-plugin:3.6.1` to `3.6.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.21.0` to `2.22.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.5` to `5.7.6`
* Updated `org.eclipse.jgit:org.eclipse.jgit:7.7.1.202607240634-r` to `7.8.0.202609011348-r`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.5` to `5.7.6`
* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.19`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.1` to `2.1.2`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.1` to `3.7.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.codehaus.mojo:build-helper-maven-plugin:3.6.1` to `3.6.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.21.0` to `2.22.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.5` to `5.7.6`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.1` to `2.1.2`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.1` to `3.7.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.21.0` to `2.22.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`
