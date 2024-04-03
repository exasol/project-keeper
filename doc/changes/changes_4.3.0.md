# Project Keeper 4.3.0, released 2024-??-??

Code name: Custom Release Artifacts

## Summary

This release contains many new features and improvements:
* It allows customizing the workflow steps in some GitHub workflows, see the [user guide](https://github.com/exasol/project-keeper/blob/main/doc/user_guide/user_guide.md#customize-workflow-steps) for details.
* It allows specifying custom release artifacts like extension files. See the [user guide](https://github.com/exasol/project-keeper/blob/main/doc/user_guide/user_guide.md#custom-release-artifacts) for details.
* PK now automatically creates the required Git tags for Go projects.
* The release also contains many bugfixes for the new modes `update-dependencies` and `verify-release`.

## Features

* #523: Added validation steps for changes file
* #556: Updated release process to create tags for Go projects
* #517: Added configuration of custom release artifacts
* #519: Added configuration of custom build steps in `ci-build-yml`

## Bugfixes

* #546: Updated template for file `.settings/org.eclipse.jdt.core.prefs`
* #542: Prefixed release letter on GitHub with version number
* #545: Fix parsing of Go version numbers with a `v` prefix
* #548: Skip release build when preconditions are not fulfilled
* #540: Improved speed of validating mentioned issues in changes file
* #553: Reduced diff in `pom.xml` for mode `update-dependencies`
* #558: Fixed `update-dependencies` running with a released version

## Dependency Updates

### Project Keeper Shared Model Classes

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.8` to `3.16`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.1` to `2.0.2`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.12.1` to `3.13.0`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.1.0` to `3.2.2`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.11` to `0.8.12`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.10.0.2594` to `3.11.0.3922`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.2.0` to `4.3.0`
* Added `org.snakeyaml:snakeyaml-engine:2.7`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:4.2.0` to `4.3.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.2.0` to `4.3.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.8` to `3.16`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.1` to `2.0.2`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.12.1` to `3.13.0`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.1.0` to `3.2.2`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.11` to `0.8.12`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.10.0.2594` to `3.11.0.3922`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.2.0` to `4.3.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.2.0` to `4.3.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.1` to `2.0.2`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.12.1` to `3.13.0`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.1.0` to `3.2.2`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.11` to `0.8.12`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.10.0.2594` to `3.11.0.3922`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.2.0` to `4.3.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.1` to `2.0.2`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.12.1` to `3.13.0`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.1.0` to `3.2.2`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.11` to `0.8.12`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.10.0.2594` to `3.11.0.3922`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.2.0` to `4.3.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.1` to `2.0.2`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.12.1` to `3.13.0`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.1.0` to `3.2.2`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.11` to `0.8.12`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.10.0.2594` to `3.11.0.3922`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.2.0` to `4.3.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.1` to `2.0.2`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.12.1` to `3.13.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.11` to `0.8.12`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.10.0.2594` to `3.11.0.3922`
