# Project keeper maven plugin 0.7.3, released 2021-06-15

Code name: Fixed dependency crawling

## Summary

Project-Keeper crawls the licenses of the dependencies of the project. In this release we fixed the dependency crawling for dependencies that contain transitive dependencies that are not available.

## Bug Fixes:

* #138: Fixed dependency crawling for dependencies with unreachable dependencies

## Dependency Updates

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:0.7.2` to `0.7.3`
