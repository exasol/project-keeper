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

### Self Update

`feat~self-update~1`

PK can check if its own version equals the latest available version of PK. If a newer version is available then PK performs a self-update.

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

### Build Integration

PK can be integrated into the CI build so that it can break the Continuous Integration (CI) build if the project structure is invalid.

#### Maven Integration
`feat~mvn-integration~1`

PK can be integrated into the Maven lifecycle. By that, it can break the Continuous Integration (CI) build if the project structure is invalid.

Needs: dsn

#### Integration for Non-Maven Projects
`feat~non-mvn-integration~1`

PK can be integrated into non-Maven projects (e.g. Golang or NPM). By that, it can break the Continuous Integration (CI) build if the project structure is invalid.

Needs: dsn

### Support for Maven Projects

#### Support Building With Multiple Java Versions
`feat~mvn-multiple-java-versions~1`

PK generates CI build scripts that allow building and testing Java projects with Java versions 11 and 17.

Rationale:

More and more libraries and tools for Java don't support Java 11 anymore, only Java 17 and later. At the same time we still need to support building projects for Java 11 (e.g. because they run in Exasol's UDFs). Examples:

* `sonar-maven-plugin` has deprecated analysis with Java 11 and will require Java 17 soon.
* A dependency of [error-code-crawler-maven-plugin](https://github.com/exasol/error-code-crawler-maven-plugin/) is only published for Java 17. In order to fix [a vulnerability](https://github.com/exasol/error-code-crawler-maven-plugin/issues/95) we need to upgrade the complete project to Java 17 and thus all projects that use error-code-crawler-maven-plugin.

Needs: dsn

### Support for Golang Projects
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

PK can retrieve changed Golang dependencies that were added, updated or removed since the last release.

Covers:

* `feat~golang-project-support~1`

Needs: dsn

### Support for NPM Projects
`feat~npm-project-support~1`

Project Keeper supports projects using the Node Package Manager (NPM).

Needs: req

#### Get Project Version
`req~npm-project-version~1`

PK can get the project version for NPM projects.

Needs: dsn

#### Get Additional Information For Each Dependency
`req~npm-dependency-additional-information~1`

PK can retrieve the URL for downloading the artifacts of each NPM dependency.

Needs: dsn

#### Get Licenses of Dependencies
`req~npm-dependency-licenses~1`

PK can retrieve the licenses of NPM dependencies.

Needs: dsn

#### Get Changed Dependency
`req~npm-changed-dependency~1`

PK can retrieve changed NPM dependencies that were added, updated or removed since the last release.

Covers:
* `feat~npm-project-support~1`

Needs: dsn

### Automatic Dependency Update Process
`feat~automatic-dependency-update-process~1`

PK supports a process for automated dependency update. This speeds up fixing vulnerabilities in third party dependencies and creating releases.

Rationale:

The Exasol integration team maintains more than 130 projects that often require dependency updates due to security issues that are found in direct or transitive dependencies. In most cases the update requires pulling the latest source, updating the dependencies, updating the change log, running the tests locally, on success pushing the branch, running CI and creating a release as shown in the following bullet-list.

* Update dependencies
* Create change log entry
* Run local tests
* Push branch
* Run CI
* Release

Needs: req

#### Auto-update dependencies
`req~auto-update-dependencies~1`

PK automatically updates dependencies when the `dependencies_check.yml` workflow finds a new vulnerability.

Covers:
* [`feat~automatic-dependency-update-process~1`](#automatic-dependency-update-process)

Needs: dsn

#### Automatically create change log entry
`req~auto-create-changelog~1`

PK generates an entry in the changes file for fixed vulnerabilities.

Rationale:

The changes file entries for fixed vulnerabilities always have the same structure and can be easily automated to avoid manual work.

Covers:
* [`feat~automatic-dependency-update-process~1`](#automatic-dependency-update-process)

Needs: dsn

#### Automatically Create a Pull Request
`req~auto-create-pr~1`

PK creates a new Pull Request after upgrading dependencies.

Rationale:

A pull requests allows to
* automatically run tests using the updated dependencies to verify if the upgrade caused any problems
* review and approve changes
* manually modify files in case of problems

Covers:
* [`feat~automatic-dependency-update-process~1`](#automatic-dependency-update-process)

Needs: dsn

#### Automatic Release
`req~auto-release~1`

PK automatically builds a new release whenever the `main` branch is updated.

Rationale:

* This reduces manual work, it's not necessary any more to manually run release-droid.
* Optionally the user can indicate to apply the changes, but postpone creating a release.

Covers:
* [`feat~automatic-dependency-update-process~1`](#automatic-dependency-update-process)

Needs: dsn

### Customizable Workflows
`feat~customize-workflows~0`

PK allows customizing the `ci-build.yml` and `release.yml` workflows with project-specific build steps.

Rationale:
Currently some projects are already using customized workflows but needed to exclude them from PK generation. Allowing to customize workflows will simplify maintenance of GH workflows.

Needs: req

#### Customize Release Artifacts
`req~customize-release-artifacts~0`

PK allows customizing the list of files that are attached to new GitHub releases in the `release.yml` workflow.

Rationale:
Some projects need to release custom files like executable `.jar` files or `.js` extensions.

Needs: dsn

Covers:
* [`feat~customize-workflows~0`](#customizable-workflows)

#### Customize Build Process
`req~customize-build-process~0`

PK allows adding pre and post steps during the build process as well as customizing the actual build step.

Rationale:
Some projects need to
* install additional tools like Go, Node
* prepare files (e.g. `test_config.properties`) with configuration and credentials
* prepare test infrastructure with `terraform init && terraform apply`
* pass additional environment variables (e.g. AWS credentials) to the build step
* attach files to the workflow (e.g. `classes.lst` for the S3 virtual schema)
* run cleanup steps like `terraform destroy`

Needs: dsn

Covers:
* [`feat~customize-workflows~0`](#customizable-workflows)
