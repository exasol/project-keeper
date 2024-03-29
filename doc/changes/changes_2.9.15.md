# Project Keeper 2.9.15, released 2023-10-30

Code name: Create issues for vulnerabilities

## Summary

This release updates the `dependencies_check.yml` GitHub workflow to use new [security_issues](https://exasol.github.io/python-toolbox/github_actions/security_issues.html) action. This action will automatically create GitHub issues for vulnerable dependencies found by the [ossindex-maven plugin](https://sonatype.github.io/ossindex-maven/maven-plugin/).

The release also adds an option that allows selecting the operating system for the GitHub runner. This is useful when the default `ubuntu-latest` does not work in a project.

## Features

* #489: Added security_issues workflow
* #492: Added option for selecting the operating system for GitHub workflows

## Documentation

* #485: Added note about `--projects .` command line option to user guide

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.14` to `2.9.15`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:2.9.14` to `2.9.15`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.14` to `2.9.15`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.14` to `2.9.15`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:2.9.14` to `2.9.15`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:2.9.14` to `2.9.15`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.14` to `2.9.15`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:2.9.14` to `2.9.15`
