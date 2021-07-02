# Project keeper maven plugin 0.9.0, released 2021-07-02

Code name: Migrated from Travis CI to GitHub Actions

## Summary

In this release we updated the template to migrate from Travis CI to GitHub Actions and added a validation for the README.md file.

When updating to this version you need to take the following manual steps:

* Check if `.github/workflows/ci-build.yml` requires project specific modifications (compare with `.travis.yml`)
* Remove `.travis.yml` file
* Run `mvn project-keeper:fix` and fix the non auto-fixable findings

## Features

* #147: Updated template to use GitHub Actions instead of travis
* #96: Added validation of README.md

## Dependency Updates

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:0.8.0` to `0.9.0`
