# Project keeper maven plugin 1.3.0, released 2021-10-11

Code name: Test with Java 17

## Summary

This release adds a GitHub action that verifies that the project builds with Java 17. It also updates the Eclipse settings files for code formatter and save actions to Eclipse 2021-09.

## Features

* #51: Added validation for .gitignore
* #182: Added GitHub action for testing with Java 17

## Bug Fixes

* #174: Fixed verification for properties in pom with whitespace
## Dependency Updates

### Compile Dependency Updates

* Updated `io.github.classgraph:classgraph:4.8.115` to `4.8.125`
* Updated `org.apache.maven:maven-core:3.8.2` to `3.8.3`
* Updated `org.apache.maven:maven-plugin-api:3.8.2` to `3.8.3`
* Updated `org.eclipse.jgit:org.eclipse.jgit:5.12.0.202106070339-r` to `5.13.0.202109080827-r`

### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:0.1.0` to `1.0.0`
* Updated `com.exasol:maven-project-version-getter:0.1.0` to `1.0.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.7.2` to `5.8.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.7.2` to `5.8.1`
* Updated `org.mockito:mockito-core:3.12.4` to `4.0.0`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:1.2.0` to `1.3.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:2.7` to `3.0.0-M1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0-M3` to `3.0.0`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:1.6` to `3.0.1`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.2.0` to `3.3.1`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.6.0` to `3.6.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.7` to `2.8.1`
