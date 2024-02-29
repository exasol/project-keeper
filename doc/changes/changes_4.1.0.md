# Project Keeper 4.1.0, released 2024-02-29

Code name: Trigger PR CI build

## Summary

This release updates the comment of the dependency updating Pull Request to instruct the user how to trigger the CI build for the Pull Request. It also sends Slack notifications when the dependency check or update fails.

### Migration Guide

When upgrading a repository to the new version make sure that the repository has access to GitHub secret `INTEGRATION_TEAM_SLACK_NOTIFICATION_WEBHOOK`. This is required for Slack notifications for new Pull Requests. The workflows will still run but won't send notifications if the secret is missing.

## Features

* #536: Added Slack notification when dependency check or update fails

## Bugfixes

* #534: Fixed running checks for dependency update PR

## Dependency Updates

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.0.0` to `4.1.0`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:4.0.0` to `4.1.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.0.0` to `4.1.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.0.0` to `4.1.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.0.0` to `4.1.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.0.0` to `4.1.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.0.0` to `4.1.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.0.0` to `4.1.0`
