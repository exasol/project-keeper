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

### Verify Repository
`feat~verify-repo-setup~1`

This plugin can verify if the repository setup. 

Needs: req

### Fix Repository
`feat~fix-repo-setup~1`

This plugin can fix the repository setup.

Needs: req

## Requirements

### Defined Repository Structure
`req~repository-structure~1`

We define the structure of the repositories by implementing this plugin.
That means that each change of the template structure is rolled out over a new release of this plugin. 

Covers:

* [feat~verify-repo-setup~1](#verify-repository)
* [feat~fix-repo-setup~1](#fix-repository)

Needs: dsn

### Convenient Repository Structure Definition
`req~convenient-repository-structure~1`

Since the template is updated often and by different team members, the way, 
in that we define the template inside of this plugin, must be convenient and easily adaptable.

Covers:

* [feat~verify-repo-setup~1](#verify-repository-structure)
* [feat~fix-repo-setup~1](#fix-repository-structure)

Needs: dsn

### Checking Repository Structure During Build
`req~verify-repo-structure~1`

This plugin checks the repository structure during the build.

Rationale:
As a build-breaker, the checks are automatically executed locally and in the CI process.

Covers:

* [feat~verify-repo-setup~1](#verify-repository-structure)

Needs: dsn

### Checking Maven Plugins
`req~verify-repo-structure~1`

This plugin checks if the project has configured a minimum set of maven plugins.

Covers:

* [feat~verify-repo-setup~1](#verify-repository-structure)

Needs: dsn

### Fixing Repository Structure
`req~fix-repo-structure~1`

This plugin provides a command that automatically creates or updates the files so that they fit 
the required project structure.

Covers:

* [feat~fix-repo-setup~1](#fix-repository-structure)

Needs: dsn