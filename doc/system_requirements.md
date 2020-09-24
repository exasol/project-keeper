# System Requirement Specification Exasol Test Container

## Introduction

This maven plugin checks and unifies a project's structure according to the Exasol integration team's repository standards.

## About This Document

### Goal

Goals of this plugin:

* Fasten repository creation
* Keep repository structure and common files up to date

## Stakeholders

* Exasol Integration Team members

## Features

Features are the highest level requirements in this document that describe the main functionality of ETC.

### Verify repository structure
`feat~verify-repo-structure~1`

This plugin can verify if the repository structure fits the internal standards.

There are two types of verifications:

* Files that must exist (for example README.md)
* Files that must exist and have a predefined content (for example GitHub actions)

Needs: req

### Fit repository structure
`feat~fit-repo-structure~1`

This plugin can create or update the required files or directories.

Needs: req

## Requirements

### Defined repository structure
`req~repository-structure~1`

This plugin defines the required structure of the repositories.

For that, it provides a convenient way that can easily be adapted.

Covers:

* [feat~verify-repo-structure~1](#verify-repository-structure)
* [feat~fit-repo-structure~1](#fit-repository-structure)

Needs: dsn

### Checking repository structure during build
`req~verify-repo-structure~1`

This plugin checks the repository structure during the build.

Rationale:
As a build-breaker, the checks are automatically executed locally and in the CI process.

Covers:

* [feat~verify-repo-structure~1](#verify-repository-structure)

Needs: dsn

### Fitting repository structure
`req~fit-repo-structure~1`

This plugin provides a command that automatically creates or updates the files so that they fit 
the required project structure.

Covers:

* [feat~fit-repo-structure~1](#fit-repository-structure)

Needs: dsn