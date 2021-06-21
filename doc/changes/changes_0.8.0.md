# Project keeper maven plugin 0.8.0, released 2021-06-22

Code name: Added Jar Plugin

## Summary

In this release we added a configuration for the maven-jar-plugin to disabling default (non-fat) jar on jar_artifact builds. In addition, we added a requirement specification and design and fixed some bugs.

## Features

* #127: Added maven-jar-plugin configuration for disabling default (non-fat) jar on jar_artifact builds

## Documentation

* #20: Added requirements and design

## Bug Fixes

* #144 Fixed that whitespace in linkReplacement was not stripped
* #108: Allowed configuration on javadoc plugin

## Dependency Updates

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:0.7.3` to `0.8.0`
* Added `org.itsallcode:openfasttrace-maven-plugin:1.2.0`
