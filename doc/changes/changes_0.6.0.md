# Project keeper maven plugin 0.6.0, released 2021-XX-XX

Code name: Validation of dependency update section in changes file.

## Summary

This release adds validation for dependency changes in the changes file.

## Features

* #67: Added broken links checker workflow
* #25: Added validation for dependency update section in changelog

## Bugfixes

* #70: Fixed changes template
* #76: Dependencies with property versions break dependency section validation
* #74: Fixed a bug that the project was only valid after running fix twice
* #78: Fixed that plugins with no versions were ignored in changelog

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:0.2.2` to 0.4.0
* Added `org.apache.maven:maven-core:3.6.3`
* Added `org.eclipse.jgit:org.eclipse.jgit:5.10.0.202012080955-r`

### Test Dependency Updates

* Removed `org.apache.maven:maven-core:3.6.3`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:0.5.0` to 0.6.0
