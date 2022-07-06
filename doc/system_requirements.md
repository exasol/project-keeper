# System Requirement Specification for Project Keeper

## Introduction

Project Keeper (PK) is a tool that unifies the structure of repositories of the Exasol integration team.

## About This Document

### Goal

Goals of this plugin:

* Speed up repository creation
* Keep repository structure and common files up to date
* Unify repository structure

## Stakeholders

* Exasol Integration Team members

## Features

Features are the highest level requirements in this document that describe the main functionality of PK.

### Self update

`feat~self-update~1`

PK can check if its own version equals the latest available version of PK.
If a newer version is available then PK performs a self-update.

Needs: dsn



### Verify Repository

`feat~verify-repo-setup~1`

PK can check if the repository structure is up to date.

Needs: req

#### Verify POM File

`req~verify-pom-file~1`

Needs: req

##### Verify Maven Plugins

`req~verify-mvn-plugins~1`

PK can verify that a project's pom.xml file contains commonly used plugins.

Covers:

* `req~verify-pom-file~1`

Needs: dsn

##### Verify Maven Dependencies

`req~verify-maven-dependencies~1`

PK can verify that a project's pom file contains certain mandatory dependencies.

Covers:

* `req~verify-pom-file~1`

Needs: dsn

#### Verify changes_x.x.x.md file

`req~verify-changes_x.x.x.md-file~1`

PK verifies the content of the `doc/changes/changes_X.X.X.md` file for the project's current version.

Needs: req

##### Verify Dependency Changes Section in changes_x.x.x.md File

`req~verify-dependency-section-in-changes_x.x.x.md-file~1`

PK verifies that the `## Dependency Updates` section in the `changes_X.X.X.md` file contains a list of the project dependencies that were added, updated, or removed since the last release of the project. PK formats the report in a clear bulleted lists.

Covers:

* `req~verify-changes_x.x.x.md-file~1`

Needs: dsn

#### Verify Changelog.md File

`req~verify-changelog-file~1`

PK verifies that the `changelog.md` file exists and contains links to all `changes_x.x.x.md` files.

Needs: dsn

#### Verify dependencies.md File

`req~verify-dependencies-file~1`

PK verifies that the repository contains a `dependencies.md` file that contains a Markdown table with all dependencies, and their licenses. The table contains URL to dependency and license websites. PK groups the table into sections for compile, runtime, test and plugin dependencies.

The dependencies table also includes the build plugins.

Covers:

* [feat~verify-repo-setup~1](#verify-repository)

Needs: dsn

#### Verify README.md File

`req~verify-readme~1`

PK verifies that the README.md file:

* Contains certain build badges (CI status, sonar cloud analysis, ...).
* Contains a link to the dependencies.md file.
* Contains a link to the changelog.md file.

Covers:

* [`feat~verify-repo-setup~1`](#verify-repository)

Needs: dsn

#### Verify LICENSE File

`req~verify-license-file~1`

PK verifies that the project has a `LICENSE` file.

If not, PK can create the file with a MIT license and copyright statement for the current year.

Covers:

* [`feat~verify-repo-setup~1`](#verify-repository)

Needs: dsn

#### Verify Gitignore File

`req~verify-gitignore-file~1`

PK verifies that the project has a `.gitignore` file.

It checks that the file contains certain required entries.

Needs: dsn

#### Verify Existence of Files

`req~verify-existence-of-files~1`

PK verifies that certain files exist in the repository. PK does not check content of these files.

Examples: `README.md`, `travis.yml`.

Covers:

* [`feat~verify-repo-setup~1`](#verify-repository)

Needs: dsn

#### Verify non Existence of Files

`req~verify-non-existence-of-files~1`

PK verifies that certain files do not exist in the repository.

Example: Broken-Link checker GitHub workflow

Rationale:

From time to time we rename a required file. In that case, PK should make sure that the old version of the file no longer exists in the repositories.

Covers:

* [`feat~verify-repo-setup~1`](#verify-repository)

Needs: dsn

### Fix Findings

`feat~fix-findings~1`

Wherever possible, PK can automatically fix the validation findings.

Rationale:

Fixing the findings using PK improves productivity.

Needs: dsn

### Configuration

`feat~configuration~1`

PK allows users to configure the project with different templates.

Rationale:

Different projects require different project structures. For example, projects that are released on maven central need more maven plugins than other projects.

Needs: dsn

### Maven Integration

`feat~mvn-integration~1`

PK can be integrated into the maven lifecycle. By that, it can break the Continuous Integration (CI) build if the project structure is invalid.

Needs: dsn

### Support for Golang Project

`feat~golang-project-support~1`

PK supports Golang projects.

Needs: req

#### Get Project Version

`req~golang-project-version~1`

PK can get the project version for Golang projects.

Needs: dsn

#### Get Licenses of Dependencies

`req~golang-dependency-licenses~1`

PK can retrieve the licenses of Golang dependencies.

Needs: dsn

#### Get Changed Dependency

`req~golang-changed-dependency~1`

PK can retrieve changed Golang dependencies that where added, updated or removed since the last release.

Covers:

* `feat~golang-project-support~1`

Needs: dsn
