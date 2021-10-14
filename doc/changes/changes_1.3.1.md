# Project keeper maven plugin 1.3.1, released 2021-10-15

Code name:Fixed Changes File Generation

## Summary

In this release we fixed a bug that caused a crash when a released project switched to maven dependency management. Now PK prints a warning and lists all dependencies as new in that case.

## Bug Fixes

* #187: Fixed changes file generation for projects switching to maven

## Dependency Updates

### Compile Dependency Updates

* Updated `io.github.classgraph:classgraph:4.8.125` to `4.8.128`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:0.6.0` to `0.7.0`
* Updated `com.exasol:project-keeper-maven-plugin:1.3.0` to `1.3.1`
